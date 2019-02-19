package com.example.vtewe.rxjava.rxjavaforandroid.chapt5_subjects;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SubjectsActivity extends AppCompatActivity {

    static final String TAG = SubjectsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        setTitle("Subjects");

        BehaviorSubject<CharSequence> firstnameSubject = BehaviorSubject.createDefault("No");
        BehaviorSubject<CharSequence> lastnameSubject = BehaviorSubject.createDefault("Name");

        RxView.clicks(findViewById(R.id.b1l))
                .subscribe( event -> firstnameSubject.onNext(((Button)findViewById(R.id.b1l)).getText()));
        RxView.clicks(findViewById(R.id.b2l))
                .subscribe( event -> firstnameSubject.onNext(((Button)findViewById(R.id.b2l)).getText()));
        RxView.clicks(findViewById(R.id.b3l))
                .subscribe( event -> firstnameSubject.onNext(((Button)findViewById(R.id.b3l)).getText()));
        RxView.clicks(findViewById(R.id.b1r))
                .subscribe( event -> lastnameSubject.onNext(((Button)findViewById(R.id.b1r)).getText()));
        RxView.clicks(findViewById(R.id.b2r))
                .subscribe( event -> lastnameSubject.onNext(((Button)findViewById(R.id.b2r)).getText()));
        RxView.clicks(findViewById(R.id.b3r))
                .subscribe( event -> lastnameSubject.onNext(((Button)findViewById(R.id.b3r)).getText()));

        Observable.combineLatest(firstnameSubject, lastnameSubject, (first, last) -> {
            return first + " " + last;
        })
        .doOnNext(someString -> Log.i(TAG, Thread.currentThread().getName()))
        .subscribe(combinedName -> ((TextView)findViewById(R.id.output)).setText(combinedName));
    }
}
