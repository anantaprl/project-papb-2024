package ns.mobile.mtreportsfire;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {
    private RecyclerView rvReports;
    private ReportsAdapter reportsAdapter;
    private FirebaseDatabase db;

    private DatabaseReference needsRef;
    private DatabaseReference wantsRef;

    private Button btNeeds;
    private Button btWants;

    //private ImageButton btClearNeeds;
    //private ImageButton btClearWants;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Inisialisasi Tombol Hapus (Hanya digunakan ketika ingin hapus semua data di Reports)
        //btClearNeeds = findViewById(R.id.btBeforeCalendar);
        //btClearWants = findViewById(R.id.btAfterCalendar);

        //btClearNeeds.setOnClickListener(v -> clearData(needsRef));
        //btClearWants.setOnClickListener(v -> clearData(wantsRef));

        // Inisialisasi RecyclerView
        rvReports = findViewById(R.id.rvReports);
        rvReports.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi tombol
        btNeeds = findViewById(R.id.btNeeds);
        btWants = findViewById(R.id.btWants);
        Spinner spinnerCategory = findViewById(R.id.spCategory);
        Button btnBack = findViewById(R.id.btnBack);

        // Inisialisasi spinners dropdown
        Spinner spinner = findViewById(R.id.spPeriod);
        String[] items = new String[]{"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> adapterSpinner1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapterSpinner1);

        Spinner spinner2 = findViewById(R.id.spCategory);
        String[] itemCategory = new String[]{"All Transactions", "Income", "Expense", "Transportation", "Foods", "Groceries", "Education", "Clothes", "Beauty", "Gift"};
        ArrayAdapter<String> adapterSpinner2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemCategory);
        spinnerCategory.setAdapter(adapterSpinner2);

        // Inisialisasi adapter
        reportsAdapter = new ReportsAdapter(this);
        rvReports.setAdapter(reportsAdapter);

        // Inisialisasi database firebase
        db = FirebaseDatabase.getInstance();
        needsRef = db.getReference("needs");
        wantsRef = db.getReference("wants");
        
        wantsRef.push().setValue(new Reports("Pet", LocalDate.now().toString(),"- IDR 50.000","pet"));
        wantsRef.push().setValue(new Reports("Beauty", LocalDate.now().toString(),"- IDR 200,000","beauty"));
        wantsRef.push().setValue(new Reports("Gift", LocalDate.now().toString(),"- IDR 100,000","gift"));

        needsRef.push().setValue(new Reports("Transportation", LocalDate.now().toString(),"- IDR 150.000","transportation"));
        needsRef.push().setValue(new Reports("Foods", LocalDate.now().toString(),"- IDR 200,000","foods"));
        needsRef.push().setValue(new Reports("Income", LocalDate.now().toString(),"+ IDR 100,000","income"));
        needsRef.push().setValue(new Reports("Social", LocalDate.now().toString(),"- IDR 10.000","social_life"));

        // Ambil needs dulu
        fetchReportsFromFirebase(needsRef);

        // Set ButtonClickListeners
        btNeeds.setOnClickListener(v -> {
            fetchReportsFromFirebase(needsRef);
            highlightActiveButton(btNeeds, btWants);
        });

        btWants.setOnClickListener(v -> {
            fetchReportsFromFirebase(wantsRef);
            highlightActiveButton(btWants, btNeeds);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void fetchReportsFromFirebase(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
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

    private void highlightActiveButton(Button activeButton, Button inactiveButton) {
        activeButton.setAlpha(0.5f);
        inactiveButton.setAlpha(1.0f);
    }

// METODE INI HANYA DIGUNAKAN KETIKA INGIN HAPUS DATA DI HALAMAN REPORTS
//    private void clearData(DatabaseReference ref) {
//        ref.removeValue().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(ReportsActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ReportsActivity.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}

