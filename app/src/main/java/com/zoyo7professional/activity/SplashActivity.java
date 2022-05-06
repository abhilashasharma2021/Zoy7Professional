package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.profile_status;
import static com.zoyo7professional.ApiData.API.skills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.adaper.ShowSkillsAdapter;
import com.zoyo7professional.model.ShowSkillsData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
Button btContinue;
    String userId = "";
    RelativeLayout relative;
    Button click;
    RequestQueue queue;
    Handler handler = new Handler(Looper.myLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        setContentView(R.layout.activity_splash);
        userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        relative = findViewById(R.id.relative);
        btContinue=findViewById(R.id.btContinue);
        click=findViewById(R.id.click);

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                manage_Status(userId);
                Dexter.withContext(SplashActivity.this)
                        .withPermissions(
                               Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA

                        ).withListener(new MultiplePermissionsListener() {


                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                       handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InternetConnectionInterface connectivity = new InternetConnectivity();
                                if (connectivity.isConnected(getApplicationContext())) {

                                    if (userId.equals("")) {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();
                                    }
                                    else if (userId.equals("1")){
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    relative.setVisibility(View.VISIBLE);


                                    click.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(SplashActivity.this, IntroViewPagerActivity.class));

                                            finish();
                                        }
                                    });


                                    Toast.makeText(SplashActivity.this, "Not Connected to internet", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, 1500);




                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();



            }
        });


    }
    public void manage_Status(String userId) {



        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hjhjh", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {
                            String data = jsonObject.getString("data");
                            if (data.equals("0")){
                               startActivity(new Intent(SplashActivity.this,AddDetailsActivity.class));
                            }else if (data.equals("1")){
                                startActivity(new Intent(SplashActivity.this,MainActivity.class));

                            }

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
                map.put("action", profile_status);
                map.put("id", userId);
                return map;
            }
        };
        queue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}