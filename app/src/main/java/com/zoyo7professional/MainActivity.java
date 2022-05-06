package com.zoyo7professional;

import static com.zoyo7professional.ApiData.API.acceptOrder;
import static com.zoyo7professional.ApiData.API.myOrders;
import static com.zoyo7professional.ApiData.API.ordersDetails;

import static com.zoyo7professional.ApiData.API.showProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.activity.AllRequestActivity;
import com.zoyo7professional.activity.HelpActivity;
import com.zoyo7professional.activity.MyBookingHistory;
import com.zoyo7professional.activity.MyWalletActivity;
import com.zoyo7professional.activity.RequestCancelActivity;
import com.zoyo7professional.activity.SplashActivity;
import com.zoyo7professional.adaper.ShowOrdersAdapter;
import com.zoyo7professional.fragment.EditProfileFragment;
import com.zoyo7professional.fragment.HomeFragment;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    NavigationView navView;
    public static DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView ivEdit;
    BottomNavigationView bottomNavigation;
    RelativeLayout rlHome, rlBooking, rlHelp, rlWallet, rlLogout, rlEdit;
    RequestQueue queue;
    String user_Id = "", CLICK_STATUS = "";
    TextView txtname, txRating;
    ImageView ivProfile;
    RatingBar ratingStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        ratingStar = findViewById(R.id.ratingStar);
        txRating = findViewById(R.id.txRating);
        txtname = findViewById(R.id.txtname);
        rlEdit = findViewById(R.id.rlEdit);
        ivProfile = findViewById(R.id.ivProfile);
        ivEdit = findViewById(R.id.ivEdit);
        rlWallet = findViewById(R.id.rlWallet);
        rlHome = findViewById(R.id.rlHome);
        rlBooking = findViewById(R.id.rlBooking);
        rlHelp = findViewById(R.id.rlHelp);
        rlLogout = findViewById(R.id.rlLogout);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        queue = SingletonRequestQueue.getInstance(MainActivity.this).getRequestQueue();
        user_Id = SharedHelper.getKey(MainActivity.this, AppConstats.USER_ID);
       /* CLICK_STATUS = SharedHelper.getKey(MainActivity.this, AppConstats.CLICK_STATUS);

        if (CLICK_STATUS.equals("2")) {
            Menu menuItem = bottomNavigation.getMenu();
            menuItem.getItem(2).setChecked(true);
        } else if (CLICK_STATUS.equals("1")) {
            Menu menuItem = bottomNavigation.getMenu();
            menuItem.getItem(2).setChecked(true);
        } else {
            Menu menuItem = bottomNavigation.getMenu();
            menuItem.getItem(0).setChecked(true);
        }*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menuItem = bottomNavigation.getMenu();
                menuItem.getItem(2).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EditProfileFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });


        bottomNavigation.setOnNavigationItemSelectedListener(this);
        rlHome.setOnClickListener(this);
        rlBooking.setOnClickListener(this);
        rlHelp.setOnClickListener(this);
        rlWallet.setOnClickListener(this);
        rlLogout.setOnClickListener(this);


        InternetConnectionInterface connectivity = new InternetConnectivity();
        if (connectivity.isConnected(getApplicationContext())) {
            if (savedInstanceState == null) {
                show_Profile();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
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
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.rlWallet:
                startActivity(new Intent(MainActivity.this, MyWalletActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
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
                startActivity(new Intent(MainActivity.this, AllRequestActivity.class));
               // SharedHelper.putKey(MainActivity.this, AppConstats.CLICK_STATUS, "1");

                break;

            case R.id.navAccount:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EditProfileFragment()).commit();
               // SharedHelper.putKey(MainActivity.this, AppConstats.CLICK_STATUS, "2");

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

                SharedHelper.putKey(getApplicationContext(), AppConstats.USER_ID, "");
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
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

    public void show_Profile() {

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("bjkhjkljk", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONObject jsonData = new JSONObject(data);

                            txtname.setText(jsonData.getString("full_name"));
                            txRating.setText(jsonData.getString("avg_rating"));
                            ratingStar.setRating(Float.parseFloat(jsonData.getString("avg_rating")));
                            String path = jsonObject.getString("path");
                            String image = jsonData.getString("profile_image");


                            if (!jsonData.getString("profile_image").equals("")) {
                                try {
                                    Picasso.with(MainActivity.this).load(path + image).into(ivProfile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }


                        } else {
                            Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", showProfile);
                map.put("id", user_Id);


                return map;
            }
        };
        queue.add(request);
    }


}