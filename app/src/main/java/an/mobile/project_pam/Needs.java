package an.mobile.project_pam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Needs extends AppCompatActivity {
    private EditText dateField, notesField, amountField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needs);

        dateField = findViewById(R.id.dateField);
        notesField = findViewById(R.id.notesField);
        amountField = findViewById(R.id.amountText);

        Button addTransBtn = findViewById(R.id.btnAddTransaction);
        addTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateField.getText().toString();
                String notes = notesField.getText().toString();
                String amount = amountField.getText().toString();

                if (date.isEmpty() || notes.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(Needs.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore firebase = FirebaseFirestore.getInstance();

                Map<String, Object> data = new HashMap<>();
                data.put("harga", amount);
                data.put("tanggal", date);
                data.put("notes", notes);

                firebase.collection("datatracker").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Needs.this, "Success", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Needs.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });



//                new AddTransactionTask(date, notes, amount).execute("0");
                Intent intent = new Intent(Needs.this, Transaksi.class);
                startActivity(intent);
            }
        });
    }

    private class AddTransactionTask extends AsyncTask<String, Void, String> {
        private String date, notes, amount;

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

                // JSON payload
                String jsonInputString = "{\"date\": \"" + date + "\", \"notes\": \"" + notes + "\", \"amount\": \"" + amount + "\"}";

                OutputStream os = conn.getOutputStream();
                os.write(jsonInputString.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK ? "Success" : "Failed";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if ("Success".equals(result)) {
                Toast.makeText(Needs.this, "Transaction added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Needs.this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
