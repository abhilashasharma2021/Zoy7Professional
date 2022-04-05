package com.zoyo7professional.activity;

import static com.zoyo7professional.ApiData.API.login;
import static com.zoyo7professional.ApiData.API.termsAdnCondition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.R;
import com.zoyo7professional.databinding.ActivityLoginBinding;
import com.zoyo7professional.databinding.ActivityTermAndConditionBinding;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TermAndConditionActivity extends AppCompatActivity {
    RequestQueue queue;
    ActivityTermAndConditionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        binding = ActivityTermAndConditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
      //  term();
    }


    public void term() {

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

                            JSONArray jsonArray=new JSONArray(data);
                            for (int j = 0; j <jsonArray.length() ; j++) {

                                JSONObject jsonObject=jsonArray.getJSONObject(j);
                                binding.text.setText(Html.fromHtml(Html.fromHtml(jsonObject.getString("tc")).toString()));

                            }




                        }

                        else {

                            Toast.makeText(TermAndConditionActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("action", termsAdnCondition);

                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);


    }
}