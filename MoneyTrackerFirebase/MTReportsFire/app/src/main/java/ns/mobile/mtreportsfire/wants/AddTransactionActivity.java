package ns.mobile.mtreportsfire.wants;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ns.mobile.mtreportsfire.R;
import ns.mobile.mtreportsfire.wants.firebase.DatabaseListener;
import ns.mobile.mtreportsfire.wants.firebase.RealtimeDatabase;
import ns.mobile.mtreportsfire.wants.model.DataItem;


public class AddTransactionActivity extends AppCompatActivity {

    private RealtimeDatabase db;
    private List<String> mList = Arrays.asList("beauty", "social_life", "pet", "gift", "homesupply"); // list data untuk spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        db = new RealtimeDatabase();

        // Deklarasi komponen UI
        EditText etAmountAT = findViewById(R.id.etAmountAT);
        EditText etDateAT = findViewById(R.id.etDateAT);
        EditText etNotesAT = findViewById(R.id.etNotesAT);
        Spinner imageBeauty = (Spinner) findViewById(R.id.imageBtAT);
        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, mList);
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        imageBeauty.setAdapter(mArrayAdapter);

        etDateAT.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTransactionActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Format tanggal ke dd/MM/yyyy
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        etDateAT.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });


        Button btnAddTransaction = findViewById(R.id.btAT);
        btnAddTransaction.setOnClickListener(v -> {
            // Validasi input
            if (etAmountAT.getText().toString().isEmpty() || etDateAT.getText().toString().isEmpty() || etNotesAT.getText().toString().isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil data input dari user
            double amount = Double.parseDouble(etAmountAT.getText().toString());
            // Create a NumberFormat instance for Indonesian Locale
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            rupiahFormat.setMaximumFractionDigits(0); //untuk mengatur angka di belakang koma, contoh 10.000,00 jadi 10.000

            String formattedAmount = rupiahFormat.format(amount).replace("Rp", "IDR ");// Format the double value to Rupiah
            String date = etDateAT.getText().toString();
            String notes = etNotesAT.getText().toString();
            String category = mList.get(imageBeauty.getSelectedItemPosition());

            db.writeItem(new DataItem(
                    UUID.randomUUID().toString(),
                    notes,
                    date,
                    formattedAmount,
                    category
            ), new DatabaseListener() {

                @Override
                public void onSuccess(Void a) {
                    Toast.makeText(AddTransactionActivity.this, "Success add data", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(AddTransactionActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            finish(); // Kembali ke MainActivity
        });

    }
}
