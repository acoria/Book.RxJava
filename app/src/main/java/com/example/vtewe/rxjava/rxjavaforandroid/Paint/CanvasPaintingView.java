package com.example.vtewe.rxjava.rxjavaforandroid.Paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.example.vtewe.rxjava.R;

import java.util.ArrayList;
import java.util.List;

public class CanvasPaintingView extends View {


    List<Pair<Path,Paint>> paths = new ArrayList();

    public CanvasPaintingView(Context context) {
        super(context);
    }

    public CanvasPaintingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasPaintingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Pair<Path,Paint> pair: paths){
            canvas.drawPath(pair.first, pair.second);
        }
    }

    public void setPaths(List<Pair<Path,Paint>> paths){
        this.paths = paths;
        invalidate();
    }
}
