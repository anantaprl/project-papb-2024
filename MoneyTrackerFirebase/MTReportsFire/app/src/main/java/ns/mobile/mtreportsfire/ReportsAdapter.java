package ns.mobile.mtreportsfire;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsVH> {
    private final Context ctx;
    private final List<Reports> data = new ArrayList<>();

    private DatabaseReference needsRef;
    private DatabaseReference wantsRef;

    public ReportsAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setNeedsRef(DatabaseReference needsRef){
        this.needsRef = needsRef;
    }

    public void setWantsRef(DatabaseReference wantsRef){
        this.wantsRef = wantsRef;
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
            int imageResId = ctx.getResources().getIdentifier(
                    report.getCategoryLogo(),
                    "drawable",
                    ctx.getPackageName()
            );

            if (imageResId == 0) {
                Log.e("ReportsAdapter", "Resource not found for: " + report.getCategoryLogo());
            }

            Glide.with(ctx)
                    .load(imageResId)
                    .into(imgCategoryLogo);

            tvCategoryName.setText(report.getCategory());
            tvDate.setText(report.getDate());
            tvAmount.setText(report.getAmount());

            if (report.getAmount().startsWith("-")) {
                tvAmount.setTextColor(ctx.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                tvAmount.setTextColor(ctx.getResources().getColor(android.R.color.holo_green_dark));
            }

            itemView.setOnClickListener(v ->
                    Toast.makeText(ctx, report.getCategory() + " Transactions", Toast.LENGTH_SHORT).show()
            );
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

    public void setData(List<Reports> reportsList) {
        Log.d("ReportsAdapter", "Updating data with " + reportsList.size() + " items");
        data.clear();
        data.addAll(reportsList);
        notifyDataSetChanged();
    }
}

