package com.example.vtewe.rxjava.rxjavaforandroid.chapt10_unittests;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class Blackbox {

    public Observable<List<Integer>> sortList(List<Integer> list){
//        this.list = list;
        return Observable.create(emitter -> {
            // Sort the list and return a new instance
            emitter.onNext(getSortedList(list));
            emitter.onComplete();
        });
    }

    private List<Integer> getSortedList(List<Integer> listToSort) {
        Collections.sort(listToSort);
        return listToSort;
    }

    public Observable<String> splitWords(String toBeSplit){
        if(toBeSplit.isEmpty()) return Observable.create(emitter -> emitter.onError(new NullPointerException()));
        String[] split = getSplitString(toBeSplit);
        return Observable.create(emitter -> {
            for(int i=0;i<split.length;i++){
                emitter.onNext(split[i]);
            }
            emitter.onComplete();
        });
    }

    private String[] getSplitString(String toBeSplit) {
        return toBeSplit.split(" ");
    }

    public Observable<String> startUncompletable(String someString){
        return Observable.create(emitter -> emitter.onNext(someString));
    }

}

