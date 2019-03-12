package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GridPosition;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.view.GameGridView;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;

public class InteractiveGameGridView extends GameGridView {


    public InteractiveGameGridView(Context context) {
        super(context, null);
    }

    public InteractiveGameGridView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public InteractiveGameGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Observable<GridPosition> getTouchesOnGrid() {

        //        Observable<MotionEvent> userTouchObservable = RxView.touches(gameGridView, motionEvent -> true);
        Observable<GridPosition> gridPositionEventObservable = RxView.touches(this, motionEvent -> true)
                .filter(event -> event.getAction() == MotionEvent.ACTION_UP)
                .map(event -> convertPixelsToGridPosition(
                        event.getX(),event.getY(),
                        getWidth(), getHeight(),
                        getGridWidth(), getGridHeight()));

        return gridPositionEventObservable;
    }

    private GridPosition convertPixelsToGridPosition(
            float touchX, float touchY,
            int viewWidthPixels, int viewHeightPixels,
            int gridWidth, int gridHeight){

        // Horizontal GridPosition coordinate as i
        float rx = touchX /
                (float)(viewWidthPixels+1);
        int i = (int)(rx * gridWidth);
        // Vertical GridPosition coordinate as n
        float ry = touchY /
                (float)(viewHeightPixels+1);
        int n = (int)(ry * gridHeight);
        return new GridPosition(i, n);
    }
}
