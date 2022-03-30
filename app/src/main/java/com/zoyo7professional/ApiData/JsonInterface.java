package com.zoyo7professional.ApiData;

import com.zoyo7professional.RetrofitModel.LoginData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JsonInterface {


    @FormUrlEncoded
    @POST(API.login)
    Call<LoginData> login(@FieldMap Map<String, String> params);



}
