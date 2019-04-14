package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.utils.ViewUtils;

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

        ViewUtils.TouchDelta touchDelta = new ViewUtils.TouchDelta();
        TileView tileView = findViewById(R.id.tile_view);
        tileView.setOnTouchListener(touchDelta);

        tileViewModel = new TileViewModel(touchDelta.getObservable());
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
