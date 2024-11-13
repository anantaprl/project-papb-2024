package ns.mobile.mtreportfrag;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TransactionListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReportsAdapter adapter;
    private List<Reports> reportsList;
    private List<Reports> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        recyclerView = view.findViewById(R.id.rvReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reportsList = new ArrayList<>();
        filteredList = new ArrayList<>(reportsList);
        adapter = new ReportsAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        addData();
        return view;
    }

    private void addData() {
        reportsList.add(new Reports("Transportation", "28/01/24", "- IDR 150,000", R.drawable.transportation, "Need"));
        reportsList.add(new Reports("Foods", "28/01/24", "- IDR 200,000", R.drawable.foods, "Need"));
        reportsList.add(new Reports("Beauty", "28/01/24", "- IDR 200,000", R.drawable.beauty, "Want"));
        reportsList.add(new Reports("Gift", "28/01/24", "- IDR 100,000", R.drawable.gift, "Want"));
        reportsList.add(new Reports("Income", "28/01/24", "+ IDR 100,000", R.drawable.income, "Need"));
        adapter.notifyDataSetChanged();
    }

    public void filterByCategory(String transactionType) {
        filteredList.clear();
        for (Reports report : reportsList) {
            if (report.transactionType.equals(transactionType)) {
                filteredList.add(report);
            }
        }
        adapter.notifyDataSetChanged();
    }

}

