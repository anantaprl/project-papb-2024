package sf.mobile.wantsscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class ManageWantsFragment extends Fragment {

    private RecyclerView recyclerView;
    private WantsAdapter wantsAdapter;
    private List<WantsItem> wantsList; // Daftar yang akan ditampilkan
    private List<WantsItem> originalWantsList; // Daftar asli
    private Button btnExpense;
    private Button btnIncome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menghubungkan layout fragment
        View view = inflater.inflate(R.layout.fragment_manage_wants, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        btnExpense = view.findViewById(R.id.btn_expense);
        btnIncome = view.findViewById(R.id.btn_income);

        // Inisialisasi daftar data
        originalWantsList = new ArrayList<>();
        wantsList = new ArrayList<>();

        String url = "https://www.jsonkeeper.com/b/37HE";

        // Buat StringRequest baru
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("JSON Response", response); // Tambahkan log ini untuk memeriksa respons JSON
                    Gson gson = new Gson();
                    ApiModel users = gson.fromJson(response, ApiModel.class);
                    for (DataItem user : users.getOriginalWantsList()) {
                        int image = getImage(user); // Menggunakan getImage untuk mendapatkan ID sumber daya gambar
                        originalWantsList.add(new WantsItem(
                                user.getName(),
                                user.getDate(),
                                user.getAmount(),
                                image
                        ));
                    }
                    // Mengatur adapter dengan daftar kosong
                    wantsAdapter = new WantsAdapter(wantsList);
                    recyclerView.setAdapter(wantsAdapter);
                },
                error -> Log.e("Volley Error", error.toString()));

        // Tambahkan request ke RequestQueue
        VolleySingleton.getInstance(requireContext()).getRequestQueue().add(stringRequest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Gunakan dimens.xml untuk ukuran jarak
        recyclerView.addItemDecoration(new SpaceItemDecoration(spaceInPixels));

        // Listener untuk tombol Expense
        btnExpense.setOnClickListener(v -> {
            wantsList.clear();
            wantsList.addAll(originalWantsList); // Isi kembali dengan originalWantsList
            wantsAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
        });

        // Listener untuk tombol Income
        btnIncome.setOnClickListener(v -> {
            wantsList.clear(); // Kosongkan daftar
            wantsAdapter.notifyDataSetChanged(); // Perbarui adapter
            recyclerView.setVisibility(View.VISIBLE);
        });

        return view;
    }

    private static int getImage(DataItem user) {
        String imageResource = user.getImageResource();
        if (imageResource == null) {
            return R.drawable.default_image;
        }

        switch (imageResource) {
            case "beauty":
                return R.drawable.beauty;
            case "social_life":
                return R.drawable.social_life;
            case "pet":
                return R.drawable.pet;
            case "gift":
                return R.drawable.gift;
            case "homesupply":
                return R.drawable.homesupply;
            default:
                return R.drawable.default_image; // Gambar default jika tidak ada kecocokan
        }
    }
}
