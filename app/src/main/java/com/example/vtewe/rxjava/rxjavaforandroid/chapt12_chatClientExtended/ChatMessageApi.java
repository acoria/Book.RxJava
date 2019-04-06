package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ChatMessageApi {
    @GET("/messages")
    Observable<List<String>> messages();
}
