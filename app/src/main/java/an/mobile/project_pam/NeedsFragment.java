package an.mobile.project_pam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NeedsFragment extends Fragment {
    private EditText dateField, notesField, amountField;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.needs, container, false);

        initializeViews();
        setupAddTransactionButton();

        return rootView;
    }

    private void initializeViews() {
        dateField = rootView.findViewById(R.id.dateField);
        notesField = rootView.findViewById(R.id.notesField);
        amountField = rootView.findViewById(R.id.amountText);
    }

    private void setupAddTransactionButton() {
        Button addTransBtn = rootView.findViewById(R.id.btnAddTransaction);
        addTransBtn.setOnClickListener(v -> {
            String date = dateField.getText().toString();
            String notes = notesField.getText().toString();
            String amount = amountField.getText().toString();

            if (date.isEmpty() || notes.isEmpty() || amount.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            saveToFirebase(date, notes, amount);
        });
    }

    private void saveToFirebase(String date, String notes, String amount) {
        FirebaseFirestore firebase = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("harga", amount);
        data.put("tanggal", date);
        data.put("notes", notes);

        firebase.collection("datatracker")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    // Return to TransaksiFragment
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                );
    }

    private class AddTransactionTask extends AsyncTask<String, Void, String> {
        private final String date, notes, amount;

        public AddTransactionTask(String date, String notes, String amount) {
            this.date = date;
            this.notes = notes;
            this.amount = amount;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{\"date\": \"" + date + "\", \"notes\": \"" + notes + "\", \"amount\": \"" + amount + "\"}";

                OutputStream os = conn.getOutputStream();
                os.write(jsonInputString.getBytes());
                os.flush();
                os.close();

                return conn.getResponseCode() == HttpURLConnection.HTTP_OK ? "Success" : "Failed";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (getContext() == null) return;

            if ("Success".equals(result)) {
                Toast.makeText(getContext(), "Transaction added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        }
    }
}