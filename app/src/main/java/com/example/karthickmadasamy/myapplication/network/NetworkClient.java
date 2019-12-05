package com.example.karthickmadasamy.myapplication.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit technique used to create an HTTP request and also to process the HTTP response from a REST API.
 * Created by Karthick.Madasamy on 12/4/2019.
 */

public class NetworkClient {
    private static Retrofit retrofitService;
    private static final String BASE_URL="https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/";

    public static Retrofit getRetrofitService(){
        if(retrofitService ==null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient = builder.build();
            retrofitService = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofitService;
    }
}
