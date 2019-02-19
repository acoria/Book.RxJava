package com.example.vtewe.rxjava;

import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxButton {

    public static Observable<String> fromButton(Button button){

        final PublishSubject<String> subject = PublishSubject.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext("click");
            }
        });
        return subject;
    }
}
