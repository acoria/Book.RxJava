package com.example.vtewe.rxjava.rxjavaforandroid.chapt13_fanAnimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FanAnimationActivity extends AppCompatActivity {

    private static final String TAG = FanAnimationActivity.class.getSimpleName();
    private final List<String> fanItemTitles = new ArrayList<>(Arrays.asList("Send E-Mail","SMS","Call","John Smith"));
    private FanView fanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_animation);

        fanView = findViewById(R.id.fan_view);
        setTitles();

        Observable<Object> clickObservable = RxView.clicks(fanView);
        FanViewModel fanViewModel = new FanViewModel(clickObservable);

        fanViewModel
                .getOpenRatio()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(fanView::setOpenRatio);

    }

    private void setTitles() {
        for(int i=0;i<fanView.getChildCount();i++){
            ((TextView) fanView.getChildAt(i).findViewById(R.id.fan_view_item_title)).setText(fanItemTitles.get(i));
        }
    }
}
