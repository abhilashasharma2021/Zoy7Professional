package com.zoyo7professional.fragment;

import static com.zoyo7professional.ApiData.API.otpVerify;
import static com.zoyo7professional.ApiData.API.selectecity;
import static com.zoyo7professional.ApiData.API.selectstates;
import static com.zoyo7professional.ApiData.API.showProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.squareup.picasso.Picasso;
import com.zoyo7professional.ApiData.API;
import com.zoyo7professional.MainActivity;
import com.zoyo7professional.R;
import com.zoyo7professional.activity.AddDetailsActivity;
import com.zoyo7professional.activity.OtpVerifyActivity;
import com.zoyo7professional.databinding.FragmentEditProfileBinding;
import com.zoyo7professional.databinding.FragmentHomeBinding;
import com.zoyo7professional.others.AppConstats;
import com.zoyo7professional.others.SharedHelper;
import com.zoyo7professional.utilities.ImageUtils;
import com.zoyo7professional.utilities.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import iam.thevoid.mediapicker.rxmediapicker.Purpose;
import iam.thevoid.mediapicker.rxmediapicker.RxMediaPicker;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;


public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private View view;
    String user_Id="";
    RequestQueue queue;
    private File front_gallery_file;
    String stProfile = "",strGender = "", strCity = "", strState = "",strName = "", strEmail = "",strAddress = "", strPin = "";

    ArrayList<String> arrayListStateID;
    ArrayList<String> arrayListState;
    ArrayAdapter<String> adapterState;

    ArrayList<String> arrayListCityID;
    ArrayList<String> arrayListCity;
    ArrayAdapter<String> adapterCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        user_Id = SharedHelper.getKey(getActivity(), AppConstats.USER_ID);

        Log.e("jxhjhh", user_Id.toString());



        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();


            }
        });

        queue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        show_Profile(user_Id);


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

        binding.txEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.txState.setVisibility(View.GONE);
                binding.txCity.setVisibility(View.GONE);
                choose_state();

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
                ((TextView) adapterView.getChildAt(0)).setTextSize(15);

                strState = arrayListStateID.get(i);

                choose_city(strState);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







        binding.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxMediaPicker.builder(getActivity())
                        .pick(Purpose.Pick.IMAGE)
                        .take(Purpose.Take.PHOTO)
                        .build()
                        .subscribe(filepath -> {
                            Bitmap bitmap = ImageUtils.imageCompress(ImageUtils.getRealPath(getActivity(), filepath));
                            front_gallery_file = ImageUtils.bitmapToFile(bitmap, getActivity());
                            Glide.with(getActivity()).load(front_gallery_file).into(binding.ivProfile);
                            stProfile = front_gallery_file.toString();

                            Log.e("check image", stProfile);
                            Log.e("check image", front_gallery_file.getAbsolutePath());

                        });
            }
        });


        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = binding.etName.getText().toString().trim();
                strEmail = binding.etEmail.getText().toString().trim();
                strAddress = binding.etAddress.getText().toString().trim();
                strPin = binding.etPin.getText().toString().trim();

                update_profile(strName, strEmail, strGender, strAddress, strPin);


            }
        });




        return view;




    }

    public void show_Profile(String user_Id){


        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, API.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("bjkhjkljk", response);
                try {
                    binding.progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.has("result")) {

                        String msg = jsonObject.getString("result");
                        if (msg.equals("true")) {

                            String data = jsonObject.getString("data");

                            JSONObject jsonData = new JSONObject(data);

                            binding.etName.setText(jsonData.getString("full_name"));
                            binding.etEmail.setText(jsonData.getString("email_id"));
                            binding.txMobile.setText(jsonData.getString("mobile_no"));
                            binding.etAddress.setText(jsonData.getString("address"));
                            binding.etPin.setText(jsonData.getString("pincode"));
                            String path=jsonObject.getString("path");
                            String image=jsonData.getString("profile_image");
                            binding.txState.setText(jsonData.getString("state"));
                            binding.txCity.setText(jsonData.getString("city"));



                            if (jsonData.getString("gender").equals("male")){
                                binding.rdMale.setChecked(true);
                            }else {
                                binding.rdFemale.setChecked(true);
                            }
                            Log.e("show",jsonObject.getString("path")+jsonData.getString("profile_image") );


                            if (!jsonData.getString("profile_image").equals("")){
                                try {
                                    Picasso.get().load(path+image).into(binding.ivProfile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else {

                            }


                        }
                        else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }


                } catch (Exception ex) {
                    Log.e("jgvkdfj", ex.getMessage());

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("thrtfghfg",error.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("action",showProfile);
                map.put("id",user_Id);


                return map;
            }
        };
        queue.add(request);
    }

    public void update_profile(String strName, String strEmail, String strGender, String strAddress, String strPin){

        AndroidNetworking.initialize(getActivity());
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        AndroidNetworking.initialize(getActivity(), okHttpClient);


        Log.e("hjhjh", user_Id);
        Log.e("hjhjh", stProfile);
        AndroidNetworking.upload("https://zoy7.loopdevelopers.in/adminZoy7/api/v2/process.php")
                .addMultipartParameter("action", "update_profile")
                .addMultipartParameter("id", user_Id)
                .addMultipartParameter("full_name", strName)
                .addMultipartParameter("email_id", strEmail)
                .addMultipartParameter("state_id", strState)
                .addMultipartParameter("city_id", strCity)
                .addMultipartParameter("gender", strGender)
                .addMultipartParameter("address", strAddress)
                .addMultipartFile("profile_image[]", front_gallery_file)
                .addMultipartParameter("pincode", strPin)
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

                                binding.etName.setText(jsonObject.getString("full_name"));
                                binding.etEmail.setText(jsonObject.getString("email_id"));
                                binding.txMobile.setText(jsonObject.getString("mobile_no"));
                                binding.etAddress.setText(jsonObject.getString("address"));
                                binding.etPin.setText(jsonObject.getString("pincode"));


                                String path=response.getString("path");
                                String image=jsonObject.getString("profile_image");

                                Log.e("update",response.getString("path")+jsonObject.getString("profile_image") );


                                if (jsonObject  .getString("gender").equals("male")){
                                    binding.rdMale.setChecked(true);
                                }else {
                                    binding.rdFemale.setChecked(true);
                                }

                                if (!jsonObject.getString("profile_image").equals("")){
                                    try {
                                        Glide.with(getActivity()).load(response.getString("path")+jsonObject.getString("profile_image")).into(binding.ivProfile);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                binding.progressBar.setVisibility(View.GONE);

                            }
                            else {
                                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
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

                            adapterState = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListState);
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
                Log.e("thrtfghfg", error.getMessage());

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

                            adapterCity = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListCity);
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


}

