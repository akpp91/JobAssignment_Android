package com.example.jobassignment_android.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    String BASE_URL ="https://www.elibrarycloud.com/";


    @GET("/json?key=m00&DB=elibrary_84&Email=technotime.cs@gmail.com")
    Call<JsonArray> getUserDetail();

    @GET("/json?key=m61&DB=elibrary_84&p=1")
    Call<JsonArray> getData();
}
