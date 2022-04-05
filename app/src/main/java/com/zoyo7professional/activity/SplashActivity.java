package com.zoyo7professional.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
Button btContinue;
    String userId = "";
    RelativeLayout relative;
    Button click;
    Handler handler = new Handler(Looper.myLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        setContentView(R.layout.activity_splash);
        userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);

        relative = findViewById(R.id.relative);
        btContinue=findViewById(R.id.btContinue);
        click=findViewById(R.id.click);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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

                                    userId = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);
                                    if (userId.equals("")) {
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    relative.setVisibility(View.VISIBLE);


                                    click.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(SplashActivity.this, MainActivity.class));

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
}