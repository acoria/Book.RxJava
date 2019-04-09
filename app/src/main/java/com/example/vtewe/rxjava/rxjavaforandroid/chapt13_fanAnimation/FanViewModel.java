package com.example.vtewe.rxjava.rxjavaforandroid.chapt13_fanAnimation;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class FanViewModel {

    BehaviorSubject<Boolean> isOpenSubject = BehaviorSubject.createDefault(true);
    BehaviorSubject<Float> openRatioSubject = BehaviorSubject.createDefault(0f);
    Observable<Object> clickObservable;

    public FanViewModel(Observable<Object> clickObservable){
        this.clickObservable = clickObservable;

        //reverse isOpen
        this.clickObservable
                .subscribe(click -> isOpenSubject.onNext(!isOpenSubject.getValue()));

        Observable<Float> targetRatioObservable = isOpenSubject
                .map(isOpen -> isOpen ? 0f : 1f);

        AnimateToOperator.animate(targetRatioObservable,200)
                .subscribe(openRatioSubject::onNext);
    }

    Observable<Float> getOpenRatio(){
        return openRatioSubject.hide();
    }
}
