package ns.mobile.mtreportsfire;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {
    private RecyclerView rvReports;
    private ReportsAdapter reportsAdapter;
    private FirebaseDatabase db;
    private DatabaseReference MTAppDb;

    public static final
    String FirebaseURL =
            "https://mtreportsfire-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        rvReports = findViewById(R.id.rvReports);
        rvReports.setLayoutManager(new LinearLayoutManager(this));

        Spinner spinner = findViewById(R.id.spPeriod);
        String[] items = new String[]{"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> adapterSpinner1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapterSpinner1);

        Spinner spinner2 = findViewById(R.id.spCategory);
        String[] itemCategory = new String[]{"All Transactions", "Income", "Expense", "Transportation", "Foods", "Groceries", "Education", "Clothes", "Beauty", "Gift"};
        ArrayAdapter<String> adapterSpinner2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemCategory);
        spinner2.setAdapter(adapterSpinner2);

        reportsAdapter = new ReportsAdapter(this);
        rvReports.setAdapter(reportsAdapter);

        this.db = FirebaseDatabase.getInstance(FirebaseURL);
        this.MTAppDb = this.db.getReference("reports");
        this.reportsAdapter.setMTAppDb(this.MTAppDb);

        fetchReportsFromFirebase();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void fetchReportsFromFirebase() {
        this.MTAppDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Reports> reportsList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Reports report = data.getValue(Reports.class);
                    if (report != null) {
                        reportsList.add(report);
                    }
                }
                reportsAdapter.setData(reportsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReportsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

