package com.example.vtewe.rxjava.rxjavaforandroid.Paint;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;
import android.view.MotionEvent;

import com.example.vtewe.rxjava.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class PaintViewModel {
    BehaviorSubject<List<Pair<Path, Paint>>> paths = BehaviorSubject.createDefault(new ArrayList());
    BehaviorSubject<Paint> paintBehaviorSubject;

    public PaintViewModel(
            Observable<DeltaTouchMotion> deltaTouchMotion,
            Observable<Integer> colorPick,
            Observable<Float> thicknessPick){

        paintBehaviorSubject= BehaviorSubject.createDefault(getFreshPaint());
        colorPick
                .subscribe(color -> {
                    Paint freshPaint = getFreshPaint();
                    freshPaint.setColor(color);
                    paintBehaviorSubject.onNext(freshPaint);
                });
        thicknessPick
                .subscribe(thickness -> {
                    Paint freshPaint = getFreshPaint();
                    freshPaint.setStrokeWidth(thickness);
                    paintBehaviorSubject.onNext(freshPaint);
                });
        deltaTouchMotion
                .withLatestFrom(paths, Pair::new)
                .withLatestFrom(paintBehaviorSubject, Pair::new)
                .map(triple -> {
                    switch (triple.first.first.motionEvent){
                        case MotionEvent.ACTION_DOWN :
                            Path path = new Path();
                            path.moveTo((float)triple.first.first.lastX,(float)triple.first.first.lastY);
                            paths.getValue().add(new Pair(path, triple.second));
                            break;
                        case MotionEvent.ACTION_MOVE :
                            List<Pair<Path, Paint>> pathList = paths.getValue();
                            Pair<Path, Paint> pair = pathList.get(pathList.size()-1);
                            Path lastPath = pair.first;
                            lastPath.quadTo((float) triple.first.first.lastX, (float) triple.first.first.lastY,(float) triple.first.first.x, (float) triple.first.first.y);
                            break;
                    }
                    return paths.getValue();
                })
                .subscribe(paths::onNext);

    }

    public Observable<List<Pair<Path, Paint>>> getPaths(){
        return paths;
    }

    private Paint getFreshPaint() {
        Paint newPaint = new Paint();
        newPaint.setStyle(Paint.Style.STROKE);

        if(paintBehaviorSubject != null){
            Paint oldPaint = paintBehaviorSubject.getValue();
            newPaint.setColor(oldPaint.getColor());
            newPaint.setStrokeWidth(oldPaint.getStrokeWidth());
        }
        return newPaint;
//        paint.setAntiAlias(true);
//        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void resetPaths(){
        paths.onNext(new ArrayList());
    }

}
