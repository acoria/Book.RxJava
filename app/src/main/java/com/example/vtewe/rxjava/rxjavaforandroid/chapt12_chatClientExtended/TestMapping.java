package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class TestMapping {

    public static void main(String[] args) {

        TestMapping testMapping = new TestMapping();
        testMapping.test();
            }

    private void test(){
        List<String> list = new ArrayList<>();
        list.add("blue");
        list.add("green");

//        Observable<List<String>> listObservable = Observable.create(emitter -> {
        Observable<String> listObservable = Observable.create(emitter -> {
            for(String item : list){
                emitter.onNext(item);
            }
                emitter.onComplete();
        });
//        listObservable
//                .map(item -> item + "-mapped")
//                .subscribe(this::print);

//        listObservable
//                .switchMap(item -> {
//                    Observable<String> newObservable = Observable.create(emitter -> emitter.onNext("yellow"));
//                    return newObservable;
//                })
//                .subscribe(this::print);

        listObservable
                .flatMap(listItem -> {
                    Observable<String> newObservable = Observable.create(emitter -> emitter.onNext("yellow"));
                    return newObservable;
                })
                .toList()
//                .toObservable()
                .subscribe(this::printList);

    }

    private void print(String text){
        System.out.println(text);
    }

    private void printList(List<String> list){
        System.out.println("list");
        for(String listItem : list) {
            System.out.println(listItem);
        }
    }
}
