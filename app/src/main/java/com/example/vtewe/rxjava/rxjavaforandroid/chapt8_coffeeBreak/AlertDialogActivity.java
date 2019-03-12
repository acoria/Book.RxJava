package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_coffeeBreak;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class AlertDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        Observable<Object> buttonObservable = RxView.clicks(findViewById(R.id.btn_show_alert));
        Observable<String> titleObservable =
                RxTextView.textChanges(findViewById(R.id.title))
                .map(Object::toString);
        Observable<String> messageObservable = RxTextView.textChanges(findViewById(R.id.message))
                .map(Objects::toString);

        Observable<Pair<String,String>> dialogContentsObservable = Observable.combineLatest(
                titleObservable,
                messageObservable,
                Pair::new);

//        Observable<DialogContents> dialogContentsObservable = Observable.combineLatest(
//                titleObservable,
//                messageObservable,
//                DialogContents::new
//        );

        buttonObservable
                .withLatestFrom(
                        dialogContentsObservable,
                        (buttonClick, dialogContents) -> dialogContents)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showDialogWithPair);
//                .subscribe(this::showDialog);
    }

    private void showDialogWithPair(Pair<String,String> dialogContents){
        new AlertDialog.Builder(this)
                .setTitle(dialogContents.first)
                .setMessage(dialogContents.second)
                .show();
    }
    private void showDialog(DialogContents dialogContents){
        new AlertDialog.Builder(this)
                .setTitle(dialogContents.title)
                .setMessage(dialogContents.message)
                .show();
    }

}

class DialogContents{

    String title;
    String message;

    DialogContents(String title, String message){
        this.title = title;
        this.message = message;
    }

}
