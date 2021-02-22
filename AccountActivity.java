package com.example.krypty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//comeeressdf

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Initialize
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Select Nav Button
        bottomNavigationView.setSelectedItemId(R.id.account);

        //ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    //Interact Page
                    case R.id.interact:
                        startActivity(new Intent(getApplicationContext(),InteractActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    //Market Page
                    case R.id.market:
                        startActivity(new Intent(getApplicationContext(),MarketActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    //Home Page
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    //Account Page
                    case R.id.account:
                        return true;
                }
                return false;
            }
        });

    }
}