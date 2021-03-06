package com.example.vtewe.rxjava.rxjavaforandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.Paint.PaintActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt11_chatClient.ChatClientActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.ChatClientActivityExtended;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt13_fanAnimation.FanAnimationActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.MapActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt6_fileBrowser.FileBrowserActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt6_fileBrowser.fb_CoffeeBreak.FB_CoffeeBreak;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt4_disposables.DisposablesActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt5_subjects.SubjectsActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores.FileBrowserActivityWithStore;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores.first_mini_store.FileBrowserActivityWithMiniStore;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_coffeeBreak.AlertDialogActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.TicTacToeActivity;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.ConnectFourActivity;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivityForRxJava extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_rx_java);

        compositeDisposable.add(RxView.clicks(findViewById(R.id.my_own_observable))
                .subscribe(event -> startAnActivity(MyOwnObservableActivity.class)));
//        RxView.clicks(findViewById(R.id.newsarticle))
//                .subscribe(event -> startAnActivity(.class));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.disposables))
                .subscribe(event -> startAnActivity(DisposablesActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.subjects))
                .subscribe(event -> startAnActivity(SubjectsActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.filebrowser))
                .subscribe(event -> startAnActivity(FileBrowserActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.fb_coffee_break))
                .subscribe(event -> startAnActivity(FB_CoffeeBreak.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.filebrowser_ministore))
                .subscribe(event -> startAnActivity(FileBrowserActivityWithMiniStore.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.filebrowser_store))
                .subscribe(event -> startAnActivity(FileBrowserActivityWithStore.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.tictactoe))
                .subscribe(event -> startAnActivity(TicTacToeActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.alertDialog))
                .subscribe(event -> startAnActivity(AlertDialogActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.connectFour))
                .subscribe(event -> startAnActivity(ConnectFourActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.chatClient))
                .subscribe(event -> startAnActivity(ChatClientActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.chatClientExtended))
                .subscribe(event -> startAnActivity(ChatClientActivityExtended.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.fanAnimation))
                .subscribe(event -> startAnActivity(FanAnimationActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.map))
                .subscribe(event -> startAnActivity(MapActivity.class)));
        compositeDisposable.add(RxView.clicks(findViewById(R.id.paint))
                .subscribe(event -> startAnActivity(PaintActivity.class)));
    }

    void startAnActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
