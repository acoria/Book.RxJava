package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.CustomApplication;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ChatClientActivityExtended extends AppCompatActivity {

    private static final String TAG = ChatClientActivityExtended.class.getSimpleName();

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView inputTextView;
    private ChatViewModel viewModel;
    private ArrayAdapter<String> arrayAdapter;
    private Observable<String> searchFieldObservable;
    private ChatModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client_extended);

        setupUi();
        chatModel = ((CustomApplication) getApplication()).getChatModel();
        setupViewModel();

        compositeDisposable.add(viewModel.getMessageList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    arrayAdapter.clear();
                    arrayAdapter.addAll(list);
                }));
        viewModel.subscribe();

    }

    private void setupViewModel() {
        viewModel = new ChatViewModel(chatModel.getChatMessages(), searchFieldObservable);
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
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);
    }

    private void onSendButtonClick() {
        chatModel.sendMessage(inputTextView.getText().toString());
        inputTextView.setText("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
        chatModel.onDestroy();
        compositeDisposable.dispose();
    }

}
