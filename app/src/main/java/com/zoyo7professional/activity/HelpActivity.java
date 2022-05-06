package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.help;
import static com.zoyo7professional.ApiData.API.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.databinding.ActivityAllRequestBinding;
import com.zoyo7professional.databinding.ActivityHelpBinding;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectionInterface;
import com.zoyo7professional.utilities.InternetConnection.InternetConnectivity;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {
ActivityHelpBinding binding;
    ProgressDialog dialog;
    RequestQueue queue;
    String user_Id="",user_Email="",stMail="",stQuery="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user_Id = SharedHelper.getKey(HelpActivity.this, AppConstats.USER_ID);
        user_Email = SharedHelper.getKey(HelpActivity.this, AppConstats.EMAIL_ID);

        binding.ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, MainActivity.class));
                finish();
            }
        });


        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stMail = binding.etMail.getText().toString().trim();
                stQuery = binding.etQuery.getText().toString().trim();


                if (stMail.isEmpty()){
                    binding.etMail.setError("Registered Mail Must Required !");
                    binding.etMail.requestFocus();
                } else if (stQuery.isEmpty()) {
                    binding.etQuery.setError("Query Must Required! ");
                    binding.etQuery.requestFocus();
                }

          else {
                    InternetConnectionInterface connectivity = new InternetConnectivity();
                    if (connectivity.isConnected(getApplicationContext())) {
                        help();
                    } else {
                        Toast.makeText(HelpActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }


    public void help() {

        binding.progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jfdkjgf", response);


                try {

                    binding.progressBar.setVisibility(View.GONE);

                    JSONObject object = new JSONObject(response);

                    if (object.has("result")) {

                        String result = object.getString("result");


                        if (result.equals("true")) {
                            String data = object.getString("data");

                            JSONObject jsonObject = new JSONObject(data);

                           binding.etQuery.setText("");
                           binding.etMail.setText("");
                           Toast.makeText(HelpActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(HelpActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("kgjfdkjg", error.getMessage() + "");
                binding.progressBar.setVisibility(View.GONE);

                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if (response != null && response.data != null) {
                    String errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> map = new HashMap<>();
                map.put("action", help);
                map.put("email", stMail);
                map.put("query", stQuery);
                map.put("user_id", user_Id);

                return map;
            }
        };


        queue.add(request);


    }
}