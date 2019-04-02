package com.example.vtewe.rxjava.rxjavaforandroid.chapt11_chatClient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatClientActivity extends AppCompatActivity {

    private static final String TAG = ChatClientActivity.class.getSimpleName();

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BehaviorSubject<String> behaviorSubject;
    private WebSocket webSocket;
    private TextView inputTextView;
    private Gson gson = new Gson();
    private ChatViewModel viewModel;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        setupUi();
        setupViewModel();
        setupWebSocket();

        compositeDisposable.add(viewModel.getMessageList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(list -> {
                arrayAdapter.clear();
                arrayAdapter.addAll(list);
            }));
        viewModel.subscribe();
    }

    private void setupViewModel() {
        createBehaviourSubject();
        viewModel = new ChatViewModel(behaviorSubject.hide());
    }

    private void setupUi() {
        inputTextView = findViewById(R.id.edit_text);
        compositeDisposable.add(RxView.clicks(findViewById(R.id.send_button))
            .subscribe(ignore -> onSendButtonClick()));

        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
    }

    private void onSendButtonClick() {
        prepareAndSendMessage(inputTextView.getText().toString());
        inputTextView.setText("");
    }

    private void prepareAndSendMessage(String message){
        ChatMessage chatMessage = new ChatMessage(message);
        String json = gson.toJson(chatMessage);
        Log.d(TAG,"json: " + json);
        webSocket.send(json);
    }

    BehaviorSubject<String> createBehaviourSubject() {
        behaviorSubject = BehaviorSubject.create();
        behaviorSubject.doOnDispose(() -> webSocket.cancel());
        return behaviorSubject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
        compositeDisposable.dispose();
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
                behaviorSubject.onNext(text);
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

    private void testMessage(WebSocket webSocket) {
        ChatMessage chatMessage = new ChatMessage("Hello World!");
        Gson gson = new Gson();
        String chatMessageJsonString = gson.toJson(chatMessage);
        webSocket.send(chatMessageJsonString);
    }
}
