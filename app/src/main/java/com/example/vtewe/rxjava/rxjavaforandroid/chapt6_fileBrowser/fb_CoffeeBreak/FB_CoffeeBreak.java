package com.example.vtewe.rxjava.rxjavaforandroid.chapt6_fileBrowser.fb_CoffeeBreak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class FB_CoffeeBreak extends AppCompatActivity {

    FB_CoffeeBreakViewModel viewModel;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb__coffee_break);

        setTitle("Fun with ViewModels");

        viewModel = new FB_CoffeeBreakViewModel(RxView.clicks(findViewById(R.id.btn_change_color)));
        //potential API call results should still be received (but not be displayed yet in the UI)
        viewModel.subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //as soon as the view model connects back to the UI, the latest value is send immediately
        makeViewBinding();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //the UI does not need to be informed upon changes at this point
        releaseViewBinding();
    }

    private void makeViewBinding() {
        compositeDisposable.add(
                viewModel.getColor()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::changeBackgroundColor));
    }

    private void releaseViewBinding() {
        compositeDisposable.clear();
    }

    private void changeBackgroundColor(Integer integer) {
        findViewById(R.id.layout).setBackgroundColor(integer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
    }
}
