package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data.ChatStore;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatClientActivityExtended extends AppCompatActivity {

    private static final String TAG = ChatClientActivityExtended.class.getSimpleName();

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BehaviorSubject<String> messageBehaviorSubject = BehaviorSubject.create();
    Observable<ChatMessage> messageObservable;
    private WebSocket webSocket;
    private TextView inputTextView;
    private Gson gson = new Gson();
    private ChatViewModel viewModel;
    private ArrayAdapter<String> arrayAdapter;
    private ChatStore chatStore = new ChatStore();
    private Observable<String> searchFieldObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client_extended);

        createMessageObservable();

        setupUi();
        setupViewModel();
        setupWebSocket();

        compositeDisposable.add(messageObservable
                .subscribe(chatStore::put));
        compositeDisposable.add(viewModel.getMessageList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    arrayAdapter.clear();
                    arrayAdapter.addAll(list);
                }));
        viewModel.subscribe();
    }

    private void createMessageObservable() {
        messageObservable = messageBehaviorSubject
                .doOnDispose(() -> webSocket.cancel())
                .map(json -> gson.fromJson(json, ChatMessage.class));
    }

    private void setupViewModel() {
        viewModel = new ChatViewModel(chatStore.getAll(),searchFieldObservable);
    }

    private void setupUi() {
        //button
        inputTextView = findViewById(R.id.edit_text);
        compositeDisposable.add(RxView.clicks(findViewById(R.id.send_button))
                .subscribe(ignore -> onSendButtonClick()));

        //search
        searchFieldObservable = RxTextView.textChanges(findViewById(R.id.search_field))
            .map(CharSequence::toString);

        //list
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
}
