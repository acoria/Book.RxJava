package com.example.vtewe.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Example1_ObservableObserver extends AppCompatActivity {

    String TAG = "Log output:";
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.rx_button);
        disposable.add(RxButton.fromButton(button)
                .flatMap(buttonText -> {
                    return Observable.just(buttonText + "->flatMap");
                })
                .flatMap(buttonText ->
                        StackedRxCalls.getInstance().stackedCall(buttonText))
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String value) {
                        Log.e(TAG, value + "->on next");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "on error");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "on complete");
                    }
                }));

//        button = findViewById(R.id.new_event_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                triggerNewEvent();
//            }
//        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // don't send events once the activity is destroyed
        disposable.dispose();
    }
}
