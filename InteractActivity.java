package com.example.krypty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//comment test

public class InteractActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);

        //Initialize
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        //Select Nav Button
        bottomNavigationView.setSelectedItemId(R.id.interact);

        //ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    //Interact Page
                    case R.id.interact:
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
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}