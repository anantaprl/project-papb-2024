package an.mobile.project_pam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Transaksi extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransaksiAdapter adapter;
    private List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi);

        recyclerView = findViewById(R.id.rvTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseList = new ArrayList<>();
        adapter = new TransaksiAdapter(expenseList);
        recyclerView.setAdapter(adapter);

        Button addTransBtn = findViewById(R.id.btnAddTransaction);
        addTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transaksi.this, Needs.class);
                startActivity(intent);
            }
        });

        TextView tvAmount = findViewById(R.id.tvAmount);
        TextView tvBudget = findViewById(R.id.tvBudget);
        tvAmount.setText("IDR 700,000");
        tvBudget.setText("Budget IDR 800,000");

        FirebaseFirestore firebase = FirebaseFirestore.getInstance();

        firebase.collection("datatracker").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // If the request is successful
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String harga = document.getString("harga");
                    String date = document.getString("tanggal");
                    String notes = document.getString("notes");
                    Expense expense = new Expense(notes, date, harga);
                    adapter.addExpense(expense);
                }
            } else {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch data from API
        new FetchTransactionsTask().execute("url");
    }

    private class FetchTransactionsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.isEmpty()) {
                // Parse JSON response
                List<Expense> expenses = new Gson().fromJson(result, new TypeToken<List<Expense>>() {}.getType());
                expenseList.clear();
                expenseList.addAll(expenses);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Transaksi.this, "Failed to load transactions", Toast.LENGTH_SHORT).show();
            }
        }
    }
}