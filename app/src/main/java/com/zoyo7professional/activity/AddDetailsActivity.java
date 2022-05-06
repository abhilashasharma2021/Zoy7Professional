package com.zoyo7professional.activity;


import static com.zoyo7professional.ApiData.API.myBookingHistory;
import static com.zoyo7professional.ApiData.API.selectecity;
import static com.zoyo7professional.ApiData.API.selectstates;
import static com.zoyo7professional.ApiData.API.skills;
import static com.zoyo7professional.ApiData.ApiNetworking.update_profile;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.ApiData.ApiNetworking;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.adaper.MyBookingHistoryAdapter;
import com.zoyo7professional.adaper.ShowSkillsAdapter;
import com.zoyo7professional.databinding.ActivityAddDetailsBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.model.ShowSkillsData;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.ImageUtils;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class AddDetailsActivity extends AppCompatActivity {
    ActivityAddDetailsBinding binding;

    String strName = "", strEmail = "", strCity = "", strState = "", user_Id = "", strGender = "", strAddress = "", strPin = "";
    String stProfile = "", st_SkillId = "",st_SkillName="";
    ArrayList<String> arrayListStateID;
    ArrayList<String> arrayListState;
    ArrayAdapter<String> adapterState;

    ArrayList<String> arrayListCityID;
    ArrayList<String> arrayListCity;
    ArrayAdapter<String> adapterCity;

    private File front_gallery_file;
    public static ProgressBar progressBarDialog;
    public static Dialog dialog;
    RecyclerView rvSkills;
    public static Button btSave;
    public static ArrayList<String> Arr_skillsId;
    public static ArrayList<String> Arr_skillsName;
    ArrayList<ShowSkillsData> showSkillsArrayList = new ArrayList<>();
/*

    public static final int MAX_LINES = 3;
    public static final String TWO_SPACES = " ";
*/

    String myReallyLongText = "Bacon ipsum dolor amet porchetta venison ham fatback alcatra tri-tip, turducken strip steak sausage rump burgdoggen pork loin. Spare ribs filet mignon salami, strip steak ball tip shank frankfurter corned beef venison. Pig pork belly pork chop andouille. Porchetta pork belly ground round, filet mignon bresaola chuck swine shoulder leberkas jerky boudin. Landjaeger pork chop corned beef, tri-tip brisket rump pastrami flank.";

    RequestQueue queue;
  TextView txt_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);

        binding = ActivityAddDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = SingletonRequestQueue.getInstance(this).getRequestQueue();

        user_Id = SharedHelper.getKey(getApplicationContext(), AppConstats.USER_ID);


        binding.rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = binding.rdGroup.findViewById(checkedId);
                int index = binding.rdGroup.indexOfChild(radioButton);

                Log.e("wedgdsgdf", index + "");
                switch (index) {
                    case 0:

                        strGender = "male";
                        break;
                    case 1:
                        strGender = "Female";
                        break;
                }
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(AddDetailsActivity.this,OtpVerifyActivity.class));
               finish();
            }
        });
        binding.txSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_chooseSkills();
            }
        });


        binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strCity = arrayListCityID.get(i);

                ((TextView) adapterView.getChildAt(0)).setTextSize(15);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                strState = arrayListStateID.get(i);
                ((TextView) adapterView.getChildAt(0)).setTextSize(15);
                choose_city(strState);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        choose_state();


        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strEmail = binding.etEmail.getText().toString().trim();
                strName = binding.etName.getText().toString().trim();
                strAddress = binding.etAddress.getText().toString().trim();
                strPin = binding.etPin.getText().toString().trim();


                if (TextUtils.isEmpty(strName)) {

                    Toast.makeText(AddDetailsActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strEmail)) {

                    Toast.makeText(AddDetailsActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strAddress)) {

                    Toast.makeText(AddDetailsActivity.this, "Please enter  address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(strPin)) {

                    Toast.makeText(AddDetailsActivity.this, "Please enter  your 6 digit pincode", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(st_SkillId)) {

                    Toast.makeText(AddDetailsActivity.this, "Please choose your skill", Toast.LENGTH_SHORT).show();
                }

                else {
                    createProfile(strName, strEmail, strGender, strAddress, strPin,st_SkillId);
                }

            }
        });


        binding.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(AddDetailsActivity.this)
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(AddDetailsActivity.this, filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, AddDetailsActivity.this);
                            Glide.with(AddDetailsActivity.this).load(front_gallery_file).into(binding.ivProfile);
                            stProfile = front_gallery_file.toString();

                            Log.e("check image", stProfile);
                            Log.e("check image", front_gallery_file.getAbsolutePath());

                        });
            }
        });

    }

    public void dialog_chooseSkills() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choose_skills_layout);
        dialog.setCancelable(true);
        btSave = dialog.findViewById(R.id.btSave);
        rvSkills = dialog.findViewById(R.id.rvSkills);
        progressBarDialog = dialog.findViewById(R.id.progressBarDialog);
        Arr_skillsId = new ArrayList<>();
        Arr_skillsName = new ArrayList<>();
        show_Skills();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    public void createProfile(String name, String email, String strGender, String address, String pin,String st_SkillId) {


        AndroidNetworking.initialize(AddDetailsActivity.this);
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(AddDetailsActivity.this, okHttpClient);


        Log.e("hjhjh", user_Id);
        Log.e("hjhjh", stProfile);
        AndroidNetworking.upload(ApiNetworking.BASEURL)
                .addMultipartParameter("action",update_profile)
                .addMultipartParameter("id",user_Id)
                .addMultipartParameter("full_name",name)
                .addMultipartParameter("email_id",email)
                .addMultipartParameter("state_id", strState)
                .addMultipartParameter("city_id", strCity)
                .addMultipartParameter("gender", strGender)
                .addMultipartParameter("address", address)
                .addMultipartFile("profile_image[]", front_gallery_file)
                .addMultipartParameter("pincode", pin)
                .addMultipartParameter("skills_id", st_SkillId)
                .setOkHttpClient(okHttpClient)
                .setTag("update profile successfully")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("xfdvsdv", response.toString());

                        try {
                            if (response.getString("result").equals("true")) {

                                String data = response.getString("data");

                                Log.e("checkdata", data);
                                JSONObject jsonObject = new JSONObject(data);
                                if ( !jsonObject.getString("city").equals("")){
                                    SharedHelper.putKey(AddDetailsActivity.this, AppConstats.CITY_NAME, jsonObject.getString("city"));
                                }
                                SharedHelper.putKey(AddDetailsActivity.this, AppConstats.CITY_ID, jsonObject.getString("city_id"));
                                SharedHelper.putKey(AddDetailsActivity.this, AppConstats.STATE_ID, jsonObject.getString("state_id"));
                                SharedHelper.putKey(AddDetailsActivity.this, AppConstats.EMAIL_ID, jsonObject.getString("email_id"));
                                SharedHelper.putKey(AddDetailsActivity.this, AppConstats.MOBILE_NO, jsonObject.getString("mobile_no"));

                                startActivity(new Intent(AddDetailsActivity.this, MainActivity.class));
                                Toast.makeText(AddDetailsActivity.this, "Successfully Add Details", Toast.LENGTH_SHORT).show();
                                binding.progressBar.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(AddDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                binding.progressBar.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                            Log.e("kdvkcjvkjv", e.getMessage());


                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dsmlkdlk", anError.getMessage());

                    }
                });
    }

    public void choose_state() {


        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("djfkdj", response);


                arrayListStateID = new ArrayList<>();
                arrayListState = new ArrayList<>();
                arrayListStateID.add("");
                arrayListState.add("Select State");
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                if (!object.getString("state").equals("null")) {
                                    arrayListStateID.add(object.getString("id"));
                                    arrayListState.add(object.getString("state"));

                                }
                            }

                            adapterState = new ArrayAdapter<>(AddDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListState);
                            adapterState.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            binding.spState.setAdapter(adapterState);
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
                map.put("action", selectstates);


                return map;
            }
        };
        queue.add(request);
    }

    public void choose_city(String strState) {


        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("fdvfdv", response);


                arrayListCityID = new ArrayList<>();
                arrayListCity = new ArrayList<>();
                arrayListCityID.add("");
                arrayListCity.add("Select City");
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                if (!object.getString("city").equals("null")) {
                                    arrayListCityID.add(object.getString("id"));
                                    arrayListCity.add(object.getString("city"));

                                }
                            }

                            adapterCity = new ArrayAdapter<>(AddDetailsActivity.this, android.R.layout.simple_list_item_1, arrayListCity);
                            adapterCity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            binding.spCity.setAdapter(adapterCity);
                        }

                    }


                } catch (Exception ex) {
                    Log.e("fvfdv", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("trhtrh", error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", selectecity);
                map.put("state_id", strState);


                return map;
            }
        };
        queue.add(request);
    }

    public void show_Skills() {


        progressBarDialog.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hjhjh", response);
                try {
                    progressBarDialog.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {


                            String data = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            showSkillsArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ShowSkillsData skillsData = new ShowSkillsData();
                                JSONObject jsonData = jsonArray.getJSONObject(i);
                                skillsData.setSkillId(jsonData.getString("id"));
                                skillsData.setSkillName(jsonData.getString("skill"));
                                showSkillsArrayList.add(skillsData);


                            }

                            rvSkills.setHasFixedSize(true);
                            rvSkills.setLayoutManager(new GridLayoutManager(AddDetailsActivity.this,2));
                            rvSkills.setAdapter(new ShowSkillsAdapter(AddDetailsActivity.this, showSkillsArrayList));

                        }


                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarDialog.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action", skills);
                return map;
            }
        };
        queue.add(request);

    }


    public void GetSkillId() {
        for (int i = 0; i < Arr_skillsId.size(); i++) {
            if (st_SkillId.equals("")) {
                st_SkillId = Arr_skillsId.get(i);

            }
            else {
                st_SkillId = st_SkillId + "," + Arr_skillsId.get(i);

            }
            Log.e("ghdfhfgxjh", st_SkillId);

        }

    }
    public void GetSkillName() {
        for (int i = 0; i < Arr_skillsName.size(); i++) {
            if (st_SkillName.equals("")) {
                st_SkillName = Arr_skillsName.get(i);

            }
            else {
                st_SkillName = st_SkillName + "," + Arr_skillsName.get(i);
                binding.txSkill.setText(st_SkillName);

            }
            Log.e("name", st_SkillName);

        }

    }
}