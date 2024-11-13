package ns.mobile.mtreportfrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsVH> {
    private final Context ctx;
    private final List<Reports> data;

    public ReportsAdapter(Context ctx, List<Reports> data) {
        this.ctx = ctx;
        this.data = data;
    }

    public static class ReportsVH extends RecyclerView.ViewHolder {
        private final ImageView imgCategoryLogo;
        private final TextView tvCategoryName;
        private final TextView tvDate;
        private final TextView tvAmount;

        public ReportsVH(View itemView) {
            super(itemView);
            imgCategoryLogo = itemView.findViewById(R.id.imgCategoryLogo);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(final Reports report, final Context ctx) {
            imgCategoryLogo.setImageResource(report.categoryLogo);
            tvCategoryName.setText(report.category);
            tvDate.setText(report.date);
            tvAmount.setText(report.amount);

            if (report.amount.startsWith("-")) {
                tvAmount.setTextColor(ctx.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                tvAmount.setTextColor(ctx.getResources().getColor(android.R.color.holo_green_dark));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, report.category + " Transactions", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public ReportsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.rowview, parent, false);
        return new ReportsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsVH holder, int position) {
        Reports report = data.get(position);
        holder.bind(report, ctx);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
