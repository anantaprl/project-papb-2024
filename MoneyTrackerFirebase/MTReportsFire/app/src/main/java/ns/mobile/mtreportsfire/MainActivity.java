package ns.mobile.mtreportsfire;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import ns.mobile.mtreportsfire.pam.Transaksi;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.navDashboard){
                    Toast.makeText(MainActivity.this, "Dashboard Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navNeeds){
                    Intent intent = new Intent(MainActivity.this, Transaksi.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Needs Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navWants){
                    Intent intent = new Intent(MainActivity.this, ns.mobile.mtreportsfire.wants.MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Wants Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navBudget){
                    Toast.makeText(MainActivity.this, "Budget Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navCategories){
                    Toast.makeText(MainActivity.this, "Categories Clicked", Toast.LENGTH_SHORT).show();
                }

                if(itemId == R.id.navReports){
                    Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
                    startActivity(intent);
                }

                if(itemId == R.id.navReminder){
                    Toast.makeText(MainActivity.this, "Reminder Clicked", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.close();
                return false;
            }
        });
    }

}