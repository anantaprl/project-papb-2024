package ns.mobile.mtreport;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {
    private RecyclerView rvReports;
    private ReportsAdapter reportsAdapter;
    private List<Reports> data;
    private List<Reports> filteredData;

    private String currentFilter = "All";
    private String currentCategory = "All Transactions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        this.rvReports = findViewById(R.id.rvReports);

        List<Reports> data = new ArrayList<>();
        data.add(new Reports("Transportation", "28/01/24", "- IDR 150,000", R.drawable.transportation, "Need"));
        data.add(new Reports("Foods", "28/01/24", "- IDR 200,000", R.drawable.foods, "Need"));
        data.add(new Reports("Beauty", "28/01/24", "- IDR 200,000", R.drawable.beauty, "Want"));
        data.add(new Reports("Gift", "28/01/24", "- IDR 100,000", R.drawable.gift, "Want"));
        data.add(new Reports("Income", "28/01/24", "+ IDR 100,000", R.drawable.income, "Need"));

        this.data = data;
        filteredData = new ArrayList<>(data);
        ReportsAdapter adapter = new ReportsAdapter(this, filteredData);
        this.reportsAdapter = adapter;
        this.rvReports.setAdapter(this.reportsAdapter);
        this.rvReports.setLayoutManager(new LinearLayoutManager(this));

        // Dropdown for Period
        Spinner spinner = findViewById(R.id.spinner);
        String[] items = new String[]{"Daily", "Weekly", "Monthly", "Yearly"};
        ArrayAdapter<String> adapterSpinner1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapterSpinner1);

        // Dropdown for Category
        Spinner spinner2 = findViewById(R.id.spinner2);
        String[] itemCategory = new String[]{"All Transactions", "Income", "Expense", "Transportation", "Foods", "Groceries", "Education", "Clothes", "Beauty", "Gift"};
        ArrayAdapter<String> adapterSpinner2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemCategory);
        spinner2.setAdapter(adapterSpinner2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentCategory = itemCategory[position];
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Button btNeeds = findViewById(R.id.btNeeds);
        Button btWants = findViewById(R.id.btWants);

        btNeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "Need";
                applyFilters();
            }
        });

        btWants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilter = "Want";
                applyFilters();
            }
        });

        Button backButton = findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void applyFilters() {
        filteredData.clear();
        for (Reports report : data) {
            if (currentFilter.equals("All") || report.transactionType.equals(currentFilter)) {
                if (currentCategory.equals("All Transactions")) {
                    filteredData.add(report);
                } else if (currentCategory.equals("Income") && report.amount.startsWith("+")) {
                    filteredData.add(report);
                } else if (currentCategory.equals("Expense") && report.amount.startsWith("-")) {
                    filteredData.add(report);
                } else if (report.category.equals(currentCategory)) {
                    filteredData.add(report);
                }
            }
        }
        reportsAdapter.notifyDataSetChanged();
    }
}

