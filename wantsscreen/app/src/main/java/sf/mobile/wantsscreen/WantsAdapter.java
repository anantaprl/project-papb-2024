package sf.mobile.wantsscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WantsAdapter extends RecyclerView.Adapter<WantsAdapter.WantViewHolder> {

    private List<WantsItem> wantsList;
    private RecyclerViewListener rvListener;

    public WantsAdapter(List<WantsItem> wantsList) {
        this.wantsList = wantsList;
    }

    public void setDeleteBtnListener(RecyclerViewListener listener){
        rvListener = listener;
    }
    @NonNull
    @Override
    public WantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wants_item_layout, parent, false);
        return new WantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WantViewHolder holder, int position) {
        WantsItem currentWant = wantsList.get(position);
        holder.tvWantName.setText(currentWant.getName());
        holder.tvWantDate.setText(currentWant.getDate());
        holder.tvWantAmount.setText(currentWant.getAmount());
        holder.imgWantIcon.setImageResource(currentWant.getImageResId());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvListener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return wantsList.size();
    }

    public static class WantViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgWantIcon, deleteBtn;
        public TextView tvWantName, tvWantDate, tvWantAmount;

        public WantViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWantIcon = itemView.findViewById(R.id.img_beauty); // Sesuaikan ID jika diperlukan
            tvWantName = itemView.findViewById(R.id.tvBeauty); // Sesuaikan ID jika diperlukan
            tvWantDate = itemView.findViewById(R.id.tvDateBeauty); // Sesuaikan ID jika diperlukan
            tvWantAmount = itemView.findViewById(R.id.tvAmountBeauty); // Sesuaikan ID jika diperlukan
            deleteBtn = itemView.findViewById(R.id.delete_item);
        }
    }
}

