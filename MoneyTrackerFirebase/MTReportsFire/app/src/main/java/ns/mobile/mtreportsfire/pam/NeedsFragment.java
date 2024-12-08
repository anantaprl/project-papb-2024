package ns.mobile.mtreportsfire.pam;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ns.mobile.mtreportsfire.R;

public class NeedsFragment extends AppCompatActivity {
    private EditText dateField, notesField, amountField;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Button addTransBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needs);
        initializeViews();
        setupAddTransactionButton();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("datatracker");
    }


    private void initializeViews() {
        dateField = findViewById(R.id.dateField);
        notesField = findViewById(R.id.notesField);
        amountField = findViewById(R.id.amountText);
        addTransBtn = findViewById(R.id.btnAddTransaction);
    }

    private void setupAddTransactionButton() {
        addTransBtn.setOnClickListener(v -> {
            String date = dateField.getText().toString();
            String notes = notesField.getText().toString();
            String amount = amountField.getText().toString();

            if (date.isEmpty() || notes.isEmpty() || amount.isEmpty()) {
                Toast.makeText(NeedsFragment.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

            } else {
                saveToFirebase(date, notes, amount);
            }
        });
    }

    private void saveToFirebase(String date, String notes, String amount) {

        // Menambahkan data ke Realtime Database
        ref.push()
                .setValue(new Expense(notes, date, amount))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firebase", "Data was written successfully!");
                        Toast.makeText(NeedsFragment.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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
            if (NeedsFragment.this == null) return;

            if ("Success".equals(result)) {
                Toast.makeText(NeedsFragment.this, "Transaction added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NeedsFragment.this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        }
    }
}