package com.example.vtewe.rxjava.rxjavaforandroid.chapt4_disposables;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.SerialDisposable;


public class DisposablesActivity extends AppCompatActivity {

    Boolean subTypeComposite = false;

    Observable<Long> intervalObservable;
    SerialDisposable serialDisposable;
    CompositeDisposable compositeDisposable;

    Button button_1;
    Button button_2;
    Button button_3;
    Button button_cancel_sub;
    Button button_switch_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposables);

        setTitle("Disposables");

        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);
        button_cancel_sub = findViewById(R.id.cancel_subs);
        button_switch_sub = findViewById(R.id.switch_subs_type);

        intervalObservable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());

        RxView.clicks(button_cancel_sub)
                .subscribe(this::cancelSubscription);

        RxView.clicks(button_switch_sub)
                .subscribe(this::switchSubscriptionType);

        switchSubscriptionType(null);


    }

    private Disposable subscribeButton1(){
        return intervalObservable.subscribe(
                intervalNo -> button_1.setText(intervalNo.toString())
        );
    }

    private Disposable subscribeButton2(){
        return intervalObservable.subscribe(
                intervalNo -> button_2.setText(intervalNo.toString())
        );
    }
    private Disposable subscribeButton3(){
        return intervalObservable.subscribe(
                intervalNo -> button_3.setText(intervalNo.toString())
        );
    }


    private void switchSubscriptionType(Object o) {
        cancelSubscription(null);
        if(subTypeComposite) {
            button_switch_sub.setText("Switch to Composite Disposable");
            observeWithSerial();
        }else{
            button_switch_sub.setText("Switch to Serial Disposable");
            observeWithComposite();
        }
        subTypeComposite = !subTypeComposite;
    }

    private void observeWithComposite() {
        //composite adds a new subscription
        compositeDisposable = new CompositeDisposable();
        RxView.clicks(button_1)
                .subscribe( event -> compositeDisposable.add(subscribeButton1()));
        RxView.clicks(button_2)
                .subscribe( event -> compositeDisposable.add(subscribeButton2()));
        RxView.clicks(button_3)
                .subscribe( event -> compositeDisposable.add(subscribeButton3()));
    }

    private void observeWithSerial() {
        //serial replaces the new with the old subscription
        serialDisposable = new SerialDisposable();
        RxView.clicks(button_1)
                .subscribe( event -> serialDisposable.set(subscribeButton1()));
        RxView.clicks(button_2)
                .subscribe( event -> serialDisposable.set(subscribeButton2()));
        RxView.clicks(button_3)
                .subscribe( event -> serialDisposable.set(subscribeButton3()));
    }

    private void cancelSubscription(Object o) {
        if(serialDisposable != null && !serialDisposable.isDisposed()){
            serialDisposable.dispose();
        }
        if(compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }

        button_1.setText("Button 1");
        button_2.setText("Button 2");
        button_3.setText("Button 3");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(serialDisposable != null && !serialDisposable.isDisposed()) {
            serialDisposable.dispose();
        }
        if(compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}