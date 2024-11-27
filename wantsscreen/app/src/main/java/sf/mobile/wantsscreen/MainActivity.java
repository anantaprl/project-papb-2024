package sf.mobile.wantsscreen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import sf.mobile.wantsscreen.base.VolleySingleton;
import sf.mobile.wantsscreen.model.ApiModel;
import sf.mobile.wantsscreen.model.DataItem;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WantsAdapter wantsAdapter;
    private List<WantsItem> wantsList; // Daftar yang akan ditampilkan
    private List<WantsItem> originalWantsList; // Daftar asli
    private Button btnExpense;
    private Button btnIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Pastikan layout activity Anda

        originalWantsList = new ArrayList<>();

        String url = "https://www.jsonkeeper.com/b/54N0";

        // Create a new StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Parse JSON response with Gson
                    Gson gson = new Gson();
                    ApiModel users = gson.fromJson(response, ApiModel.class);
                    for (DataItem user : users.getOriginalWantsList()) {
                        int image = getImage(user);
                        originalWantsList.add(new WantsItem(
                                user.getName(),
                                user.getDate(),
                                user.getAmount(),
                                image
                        ));
                    }
                    wantsList = new ArrayList<>(originalWantsList);
                    wantsAdapter = new WantsAdapter(wantsList);
                    recyclerView.setAdapter(wantsAdapter);
                },
                error -> Log.e("Volley Error", error.toString()));

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(this).getRequestQueue().add(stringRequest);


        recyclerView = findViewById(R.id.recycler_view); // ID dari RecyclerView di layout Anda
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tambahkan ItemDecoration untuk menambahkan jarak antar item
        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spaceInPixels));

        btnExpense = findViewById(R.id.btn_expense); // Temukan tombol Expense
        btnIncome = findViewById(R.id.btn_income);

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
    }

    private static int getImage(DataItem user) {
        int image = 0;
        if (Objects.equals(user.getImageResource(), "beauty")) {
            image = R.drawable.beauty;
        } else if (Objects.equals(user.getImageResource(), "social_life")) {
            image = R.drawable.social_life;
        } else if (Objects.equals(user.getImageResource(), "pet")) {
            image = R.drawable.pet;
        } else if (Objects.equals(user.getImageResource(), "gift")) {
            image = R.drawable.gift;
        } else if (Objects.equals(user.getImageResource(), "homesupply")) {
            image = R.drawable.homesupply;
        }
        return image;
    }


}
