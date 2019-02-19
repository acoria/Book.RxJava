package com.example.vtewe.rxjava;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.schedulers.Schedulers;

public class StackedRxCalls {

    public static StackedRxCalls getInstance() {
        return new StackedRxCalls();
    }

    //    public ObservableSource<String> stackedCall(String oldString){
    public Observable<String> stackedCall(String oldString) {
        return Observable.just(oldString + "->stackedCall");
    }

}
