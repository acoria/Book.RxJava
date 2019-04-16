package com.example.vtewe.rxjava.rxjavaforandroid.Paint;

public class DeltaTouchMotion {

    public double lastX;
    public double lastY;
    public double x;
    public double y;
    public int motionEvent;

    public DeltaTouchMotion(double lastX, double lastY, double x, double y, int motionEvent) {
        this.lastX = lastX;
        this.lastY = lastY;
        this.x = x;
        this.y = y;
        this.motionEvent = motionEvent;
    }
}
