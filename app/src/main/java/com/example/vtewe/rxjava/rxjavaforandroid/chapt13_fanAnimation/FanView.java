package com.example.vtewe.rxjava.rxjavaforandroid.chapt13_fanAnimation;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class FanView extends FrameLayout {
    private static final String TAG = FanView.class.getSimpleName();
    private float openRatio = 1;

    public FanView(Context context) {
        this(context, null, 0);
    }

    public FanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setStaticTransformationsEnabled(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout(" + changed + ", " + left + ", "  + top + ", " + right + ", " + bottom + ")");
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "End onLayout");
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        Log.d(TAG, "getChildStaticTransformation-old(" + child + ", " + t + ")");

        final float childIndex = getChildCount() - indexOfChild(child) - 1;

//        moveItems(child, t, childIndex);
        rotateItems(child,t,childIndex);
//        changeSizeOfItems(t);
        return true;
    }

    private void changeSizeOfItems(Transformation t) {
        Matrix matrix = t.getMatrix();
        matrix.postScale(0.5f,0.5f);
    }

    private void moveItems(View child, Transformation t, float childIndex) {
        final float childHeight = child.getHeight();
        final float newXPosition = childIndex * 50;
        final float newYPosition = childHeight * childIndex * 1.1f;

        Matrix matrix = t.getMatrix();
        matrix.setTranslate(newXPosition,newYPosition);
        Log.d(TAG, "getChildStaticTransformation-new(" + child + ", " + t + ")");
    }

    private void rotateItems(View child, Transformation t, float childIndex) {
        final float childHeight = child.getHeight();

        final float rotation = childIndex * 20;
        final float rotationCenter = childHeight / 2;
        final float adjustedRotation = rotation * openRatio;

        Matrix matrix = t.getMatrix();
        matrix.setRotate(adjustedRotation, rotationCenter, rotationCenter);
//        matrix.postRotate(rotation, rotationCenter, rotationCenter);
        Log.d(TAG, "getChildStaticTransformation-new(" + child + ", " + t + ")");
    }

    public void setOpenRatio(float r){
        this.openRatio = r;

        //invalidate all children
        final int childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            View view = getChildAt(i);
            view.invalidate();
        }
        invalidate();
    }
}
