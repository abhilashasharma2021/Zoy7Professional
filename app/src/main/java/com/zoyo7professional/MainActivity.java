package com.zoyo7professional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.zoyo7professional.activity.HelpActivity;
import com.zoyo7professional.activity.MyBookingHistory;
import com.zoyo7professional.activity.SplashActivity;
import com.zoyo7professional.fragment.HomeFragment;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    NavigationView navView;
    public static DrawerLayout drawerLayout;
    Toolbar toolbar;
    BottomNavigationView bottomNavigation;
    RelativeLayout rlHome, rlBooking,rlHelp,rlTerm,rlLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        rlTerm = findViewById(R.id.rlTerm);
        rlHome = findViewById(R.id.rlHome);
        rlBooking = findViewById(R.id.rlBooking);
        rlHelp = findViewById(R.id.rlHelp);
        rlLogout = findViewById(R.id.rlLogout);
        bottomNavigation = findViewById(R.id.bottomNavigation);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        bottomNavigation.setOnNavigationItemSelectedListener(this);
        rlHome.setOnClickListener(this);
        rlBooking.setOnClickListener(this);
        rlHelp.setOnClickListener(this);
        rlTerm.setOnClickListener(this);
        rlLogout.setOnClickListener(this);


        InternetConnectionInterface connectivity = new InternetConnectivity();
        if (connectivity.isConnected(getApplicationContext())) {
            if (savedInstanceState==null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
            }
        } else {


            Toast.makeText(this, "Not connected to internet!!!", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rlHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.rlBooking:
                 startActivity(new Intent(MainActivity.this, MyBookingHistory.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.rlHelp:
               startActivity(new Intent(MainActivity.this, HelpActivity.class));

            case R.id.rlTerm:
                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));

            case R.id.rlLogout:
               logout();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navHome:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
                break;

            case R.id.navAppointment:
/*
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HelpFragment()).commit();
*/
                break;

            case R.id.navAccount:
/*
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HelpFragment()).commit();
*/
                break;


        }
        return true;
    }


    public void logout() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
               /* SharedHelper.putKey(getApplicationContext(), Appconstant.USERID, "");
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();*/
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}