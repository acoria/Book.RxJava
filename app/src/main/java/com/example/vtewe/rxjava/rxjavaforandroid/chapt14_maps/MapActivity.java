package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vtewe.rxjava.R;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = MapActivity.class.getSimpleName();
    private TileViewModel tileViewModel;
    private CompositeDisposable subscription = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        TileView tileView = findViewById(R.id.tile_view);

        tileViewModel = new TileViewModel();
        subscription.add(tileViewModel.getTiles()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        tileView::setTiles,
                        error -> Log.d(TAG,error.getMessage())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }
}
