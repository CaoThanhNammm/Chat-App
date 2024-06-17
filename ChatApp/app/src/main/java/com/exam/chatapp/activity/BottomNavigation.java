package com.exam.chatapp.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.exam.chatapp.R;
import com.exam.chatapp.fragment.FragmentHome;
import com.exam.chatapp.fragment.FragmentContact;
import com.exam.chatapp.fragment.FragmentProfile;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.firebase.UserFirebaseClientService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

public class BottomNavigation extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment selectedFragment;
    private SessionManager sessionManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bottom_navigation_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mapping();
        ChangeToOtherFrame();
        backToScreenPhone();
    }

    private void backToScreenPhone(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý sự kiện nút lùi
                new AlertDialog.Builder(BottomNavigation.this)
                        .setMessage("Bạn có chắc chắn muốn thoát không?")
                        .setCancelable(false)
                        .setPositiveButton("Có", (dialog, id) -> {
                            finishAffinity();
                            UserFirebaseClientService.getInstance(BottomNavigation.this).updateAvaibility(sessionManager.getId(), 0);
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void ChangeToOtherFrame() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemClick = item.getItemId();
                if (itemClick == R.id.nav1) {
                    selectedFragment = new FragmentHome();
                } else if (itemClick == R.id.nav2) {
                    selectedFragment = new FragmentContact();
                } else if (itemClick == R.id.nav3) {
                    selectedFragment = new FragmentProfile();
                }
                if (selectedFragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, selectedFragment);
                    transaction.commit();
                }
                return true;
            }
        });
        if (selectedFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.nav_host_fragment, selectedFragment);
            transaction.commit();
        }
    }

    private void mapping() {
        db = FirebaseFirestore.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        fragmentManager = getSupportFragmentManager();
        selectedFragment = new FragmentHome();
        sessionManager = new SessionManager(this);
        UserFirebaseClientService.getInstance(this).updateAvaibility(sessionManager.getId(), 1);
    }

}