package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.utils.TileBitmapLoader;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.utils.ViewUtils;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class MapActivity extends AppCompatActivity {
    private static final int DEFAULT_ZOOM_LEVEL = 2;
    private static final String TAG = MapActivity.class.getSimpleName();
    private TileViewModel tileViewModel;
    private CompositeDisposable subscription = new CompositeDisposable();
    private BehaviorSubject<Integer> zoomLevel = BehaviorSubject.createDefault(DEFAULT_ZOOM_LEVEL);
    private Observable<PointD> tileViewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ViewUtils.TouchDelta touchDelta = new ViewUtils.TouchDelta();
        TileView tileView = findViewById(R.id.tile_view);
        tileView.setOnTouchListener(touchDelta);
        tileViewSize = tileView.getViewSize();

        TileBitmapLoader tileBitmapLoader = new TileBitmapLoader(this);
        tileView.setTileBitmapLoader(tileBitmapLoader);

        Observable<Integer> increment = RxView.clicks(findViewById(R.id.button_zoom_in))
                .withLatestFrom(zoomLevel, (click, lastZoomLevel) -> lastZoomLevel + 1);
        Observable<Integer> decrement = RxView.clicks(findViewById(R.id.button_zoom_out))
                .withLatestFrom(zoomLevel, (click, lastZoomLevel) -> lastZoomLevel - 1);
        subscription.add(Observable.merge(increment,decrement)
                .subscribe(zoomLevel::onNext));
//        RxView.clicks(findViewById(R.id.button_zoom_in))
//                .subscribe(click -> zoomLevel.onNext(zoomLevel.getValue() + 1));

        tileViewModel = new TileViewModel(touchDelta.getObservable());
        subscription.add(tileViewModel.getTiles(zoomLevel, tileViewSize)
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
