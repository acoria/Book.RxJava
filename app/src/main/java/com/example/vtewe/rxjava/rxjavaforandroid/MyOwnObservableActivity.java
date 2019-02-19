package com.example.vtewe.rxjava.rxjavaforandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.RxButton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MyOwnObservableActivity extends AppCompatActivity {

    private final String TAG = MyOwnObservableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_observable);


        Button button = findViewById(R.id.button);

        //use Rx Lib
        Observable<String> buttonObservable = RxButton.fromButton(button);
        buttonObservable.subscribe(
                consumer -> Log.e(TAG,consumer),
                error -> Log.e(TAG,"an error"),
                () -> Log.e(TAG,"on completed")
        );

        //without Rx Lib
        Observable<View> buttonClickObservable = Observable.create(new ObservableOnSubscribe<View>() {
               @Override
               public void subscribe(ObservableEmitter<View> e) throws Exception {
                   button.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if(!e.isDisposed()) {
                               e.onError(new Exception());
                           }
                       }
                   });
               }
           }
        );

        buttonClickObservable.subscribe(
                view -> Log.e(TAG,((TextView) view).getText().toString()),
                error -> Log.e(TAG,"an error"),
                () -> Log.e(TAG,"on completed")
        );

    }
}
