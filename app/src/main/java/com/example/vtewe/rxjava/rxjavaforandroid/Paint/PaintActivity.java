package com.example.vtewe.rxjava.rxjavaforandroid.Paint;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class PaintActivity extends AppCompatActivity {

    CanvasPaintingView paintingView;
    BehaviorSubject<Integer> colorSubject;
    BehaviorSubject<Float> thicknessSubject = BehaviorSubject.createDefault(20f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("The Canvas is your Oyster");
        setContentView(R.layout.activity_paint);

        colorSubject = BehaviorSubject.createDefault(getResources().getColor(R.color.red));
        paintingView = findViewById(R.id.paint_view);

        ViewUtils.TouchDelta touchDelta = new ViewUtils.TouchDelta();
        paintingView.setOnTouchListener(touchDelta);
        PaintViewModel viewModel = new PaintViewModel(touchDelta.getObservable(), colorSubject, thicknessSubject);

        viewModel.getPaths()
                .subscribe(this::setPaths);

        RxView.clicks(findViewById(R.id.button_red))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.red)));
        RxView.clicks(findViewById(R.id.button_blue))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.blue)));
        RxView.clicks(findViewById(R.id.button_green))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.green)));
        RxView.clicks(findViewById(R.id.button_white))
                .subscribe(click -> colorSubject.onNext(getResources().getColor(R.color.white)));
        RxView.clicks(findViewById(R.id.button_thin))
                .subscribe(click -> thicknessSubject.onNext(20f));
        RxView.clicks(findViewById(R.id.button_fat))
                .subscribe(click -> thicknessSubject.onNext(50f));
        RxView.clicks(findViewById(R.id.button_empty_canvas))
                .subscribe(click -> viewModel.resetPaths());
    }

    private void setPaths(List<Pair<Path, Paint>> paths) {
        paintingView.setPaths(paths);
    }

}
