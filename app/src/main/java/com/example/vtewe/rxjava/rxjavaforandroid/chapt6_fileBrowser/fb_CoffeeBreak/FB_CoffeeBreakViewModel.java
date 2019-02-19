package com.example.vtewe.rxjava.rxjavaforandroid.chapt6_fileBrowser.fb_CoffeeBreak;

import android.graphics.Color;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class FB_CoffeeBreakViewModel {

    final static String TAG = FB_CoffeeBreakViewModel.class.getSimpleName();
    private Observable<Object> changeColorButtonObservable;
    private BehaviorSubject<Integer> colorOutput = BehaviorSubject.createDefault(getNewColor());
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FB_CoffeeBreakViewModel(Observable<Object> changeColorButtonObservable) {
        this.changeColorButtonObservable = changeColorButtonObservable;
    }

    public Observable<Integer> getColor(){
        return colorOutput.hide();
    }

    public void subscribe(){
        compositeDisposable.add(
                changeColorButtonObservable
                        .map(event -> getNewColor())
                        .subscribe(colorOutput::onNext));
    }

    private int getNewColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void unsubscribe(){
        compositeDisposable.clear();
    }
}
