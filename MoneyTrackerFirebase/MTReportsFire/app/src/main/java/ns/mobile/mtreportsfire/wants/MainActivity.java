package ns.mobile.mtreportsfire.wants;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ns.mobile.mtreportsfire.R;
import ns.mobile.mtreportsfire.wants.firebase.DatabaseListener;
import ns.mobile.mtreportsfire.wants.firebase.RealtimeDatabase;
import ns.mobile.mtreportsfire.wants.model.DataItem;


public class MainActivity extends AppCompatActivity {

    private static final int ADD_TRANSACTION_REQUEST = 1; // Request code untuk AddTransactionActivity

    private RecyclerView recyclerView;
    private WantsAdapter wantsAdapter;
    private List<WantsItem> wantsList; // Daftar yang akan ditampilkan
    private List<WantsItem> originalWantsList; // Daftar asli
    private Button btnExpense;
    private Button btnIncome;
    RealtimeDatabase db;
    private Button btnAddTransaction; // Tombol Add Transaction

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.want_activity_main); // Pastikan layout activity Anda
        db = new RealtimeDatabase();
        originalWantsList = new ArrayList<>();
        wantsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view); // ID dari RecyclerView di layout Anda
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tambahkan ItemDecoration untuk menambahkan jarak antar item
        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spaceInPixels));
        wantsAdapter = new WantsAdapter(wantsList);
        recyclerView.setAdapter(wantsAdapter);



        db.listenData(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wantsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    DataItem item = snapshot1.getValue(DataItem.class);
                    Log.d("FIREBASE", "onDataChange: "+item.getName());
                    wantsList.add(new WantsItem(item.getUid(),item.getName(), item.getDate(), item.getAmount(), getImage(item.getImageResource())));
                    wantsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        btnExpense = findViewById(R.id.btn_expense); // Temukan tombol Expense
        btnIncome = findViewById(R.id.btn_income);
        btnAddTransaction = findViewById(R.id.btn_AddTransaction); // Inisialisasi tombol Add Transaction

        // Atur listener untuk tombol Expense
        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set kembali wantsList menjadi originalWantsList dan tampilkan RecyclerView
                wantsList.clear(); // Kosongkan wantsList
                wantsList.addAll(originalWantsList); // Tambahkan semua item dari originalWantsList
                wantsAdapter.notifyDataSetChanged(); // Perbarui adapter
                recyclerView.setVisibility(View.VISIBLE); // Tampilkan RecyclerView
            }
        });

        // Atur listener untuk tombol Income
        btnIncome.setOnClickListener(v -> {
            // Kosongkan daftar dan tampilkan RecyclerView
            wantsList.clear(); // Mengosongkan daftar
            wantsAdapter.notifyDataSetChanged(); // Memberitahu adapter bahwa data telah berubah
            recyclerView.setVisibility(View.VISIBLE); // Tampilkan RecyclerView
        });

        btnAddTransaction.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            startActivityForResult(intent, ADD_TRANSACTION_REQUEST);
        });

        wantsAdapter.setDeleteBtnListener(new RecyclerViewListener() {
            @Override
            public void onDeleteClick(int position) {
                WantsItem target = wantsList.get(position);
                db.deleteItem(new DataItem(
                        target.getUid(),
                        target.getName(),
                        target.getDate(),
                        target.getAmount(),
                        ""
                ), new DatabaseListener() {
                    @Override
                    public void onSuccess(Void a) {
                        Toast.makeText(MainActivity.this, "Berhasil Menghapus" , Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("FIREBASE", "onError: "+e.getMessage());
                    }
                });
            }
        });
    }

    private static int getImage(String imageSource) { // untuk menentukan gambar berdasarkan tipe kategori
        int image = 0;
        if (Objects.equals(imageSource, "beauty")) {
            image = R.drawable.beauty;
        } else if (Objects.equals(imageSource, "social_life")) {
            image = R.drawable.social_life;
        } else if (Objects.equals(imageSource, "pet")) {
            image = R.drawable.pet;
        } else if (Objects.equals(imageSource, "gift")) {
            image = R.drawable.gift;
        } else if (Objects.equals(imageSource, "homesupply")) {
            image = R.drawable.homesupply;
        }
        return image;
    }


}
