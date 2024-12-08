package ns.mobile.mtreportsfire.pam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ns.mobile.mtreportsfire.R;

public class Transaksi extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransaksiAdapter adapter;
    private List<Expense> expenseList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaksi);
        databaseReference = FirebaseDatabase.getInstance().getReference("datatracker");
        // Initialize RecyclerView
        setupRecyclerView();

        // Initialize Views
        setupViews();

        FirebaseApp.initializeApp(this);

        // Load Data
        loadTransactionData();

        // Setup Add Transaction Button
        Button addTransBtn = findViewById(R.id.btnAddTransactions);
        addTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch NeedsFragment
                Intent intent = new Intent(Transaksi.this, NeedsFragment.class);
                startActivity(intent);

            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rvTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseList = new ArrayList<>();
        adapter = new TransaksiAdapter(expenseList);
        recyclerView.setAdapter(adapter);
    }

    private void setupViews() {
        TextView tvAmount = findViewById(R.id.tvAmount);
        TextView tvBudget = findViewById(R.id.tvBudget);
        tvAmount.setText("IDR 700,000");
        tvBudget.setText("Budget IDR 800,000");
    }

    private void loadTransactionData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expenseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String harga = snapshot.child("amount").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String notes = snapshot.child("category").getValue(String.class);

                    Expense expense = new Expense(notes, date, harga);
                    expenseList.add(expense);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetch data from API
        loadTransactionData();
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
                List<Expense> expenses = new Gson().fromJson(result, new TypeToken<List<Expense>>() {
                }.getType());
                expenseList.clear();
                expenseList.addAll(expenses);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Transaksi.this, "Failed to load transactions", Toast.LENGTH_SHORT).show();
            }
        }
    }
}