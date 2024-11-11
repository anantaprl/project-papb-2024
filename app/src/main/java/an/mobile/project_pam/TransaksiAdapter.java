package an.mobile.project_pam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public TransaksiAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    // Tambahkan metode ini untuk menambahkan data baru
    public void addExpense(Expense expense) {
        expenseList.add(expense);
        notifyItemInserted(expenseList.size() - 1);  // Notifikasi untuk item yang baru ditambahkan
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaksi_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.tvCategory.setText(expense.getCategory());
        holder.tvDate.setText(expense.getDate());
        holder.tvAmount.setText(expense.getAmount());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDate, tvAmount;

        ExpenseViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmount = view.findViewById(R.id.tvAmount);
        }
    }
}