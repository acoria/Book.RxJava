package com.example.vtewe.rxjava.rxjavaforandroid.Paint;

import android.view.MotionEvent;
import android.view.View;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.PointD;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ViewUtils {
    public static class TouchDelta implements View.OnTouchListener {
        private Subject<DeltaTouchMotion> deltaStream = PublishSubject.create();
        private PointD lastTouch = null;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastTouch = new PointD(event.getX(), event.getY());
                    DeltaTouchMotion delta = new DeltaTouchMotion(
                            lastTouch.x,
                            lastTouch.y,
                            lastTouch.x,
                            lastTouch.y,
                            MotionEvent.ACTION_DOWN);
                    deltaStream.onNext(delta);
                    break;
                case MotionEvent.ACTION_MOVE:
                            delta = new DeltaTouchMotion(
                                lastTouch.x,
                                lastTouch.y,
                                event.getX(),
                                event.getY(),
                                MotionEvent.ACTION_MOVE);
                        deltaStream.onNext(delta);
                    lastTouch = new PointD(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:
//                    lastTouch = null;
//                    final DeltaTouchMotion delta = new DeltaTouchMotion(
//                            lastTouch.x,
//                            lastTouch.y,
//                            MotionEvent.ACTION_UP);
//                    deltaStream.onNext(delta);
//                    break;
            }
            return true;
        }

        public Observable<DeltaTouchMotion> getObservable() {
            return deltaStream;
        }
    }
}
