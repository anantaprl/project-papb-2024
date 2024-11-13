package ns.mobile.mtreportfrag;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ReportsActivity extends AppCompatActivity implements MainInfoFragment.OnFilterSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_main_info, new MainInfoFragment())
                    .replace(R.id.container_transaction_list, new TransactionListFragment())
                    .commit();
        }
    }

    @Override
    public void onFilterSelected(String transactionType) {
        TransactionListFragment transactionListFragment = (TransactionListFragment)
                getSupportFragmentManager().findFragmentById(R.id.container_transaction_list);

        if (transactionListFragment != null) {
            transactionListFragment.filterByCategory(transactionType);
        }
    }
}




