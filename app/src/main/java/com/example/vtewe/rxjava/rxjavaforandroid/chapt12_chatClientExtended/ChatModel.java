package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data.ChatMessage;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data.ChatStore;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatModel {

    private static final String TAG = ChatModel.class.getSimpleName();
    private Gson gson = new Gson();
    private Retrofit retrofit;
    private ChatMessageApi chatMessageApi;
    private ChatStore chatStore = new ChatStore();
    private WebSocket webSocket;
    private Observable<ChatMessage> messageObservable;
    private BehaviorSubject<String> messageBehaviorSubject = BehaviorSubject.create();
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public void onCreate(){
        setupRetrofit();
        setupWebSocket();

        createMessageObservable();

        subscriptions.add(messageObservable
                .subscribe(chatStore::put));

        //initialize with existing values
        loadExistingMessagesIntoStore();

    }

    public void sendMessage(String message){
        ChatMessage chatMessage = new ChatMessage(message);
        chatStore.put(chatMessage);

        String json = gson.toJson(chatMessage);
        Log.d(TAG,"json: " + json);
        webSocket.send(json);
    }

    private void createMessageObservable() {
        messageObservable = messageBehaviorSubject
                .doOnDispose(() -> webSocket.cancel())
                .map(json -> gson.fromJson(json, ChatMessage.class))
                .map(chatMessage -> chatMessage.setPending(false));
    }

    private void setupRetrofit() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.0.73:3000")
                .build();

        chatMessageApi = retrofit.create(ChatMessageApi.class);
    }

    private void loadExistingMessagesIntoStore() {
        subscriptions.add(chatMessageApi.messages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageListAsJson -> {
                    for(String json : messageListAsJson) {
                        ChatMessage chatMessage = gson.fromJson(json, ChatMessage.class);
                        chatStore.put(chatMessage.setPending(false));
                    }
                }
                ,error -> Log.d(TAG,error.getMessage())));
    }

    void setupWebSocket(){
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
//                .url("ws://echo.websocket.org")
                .url("ws://192.168.0.73:3000")
                .build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.d(TAG,"open");
//                testMessage(webSocket);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                Log.d(TAG,"Receiving : " + text);
                messageBehaviorSubject.onNext(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                Log.d(TAG,"Receiving bytes : " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d(TAG,"Closing : " + code + " / " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.d(TAG,"Error : " + t.getMessage());
            }
        });
    }

    public Observable<Collection<ChatMessage>> getChatMessages() {
        return chatStore.getStream();
    }

    public void onDestroy(){
        subscriptions.clear();
    }
}
