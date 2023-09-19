package com.example.jobassignment_android.utils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    String BASE_URL ="https://www.elibrarycloud.com/json?key=m00&DB=elibrary_84&Email=technotime.cs@gmail.com";


    @GET()
    Call<JsonObject> getUserDetail();


}
