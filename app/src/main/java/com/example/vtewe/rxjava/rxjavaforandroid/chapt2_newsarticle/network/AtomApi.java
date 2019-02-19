package com.example.vtewe.rxjava.rxjavaforandroid.chapt2_newsarticle.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface AtomApi {
    @GET
    Call<ResponseBody> getFeed(@Url String url);
}
