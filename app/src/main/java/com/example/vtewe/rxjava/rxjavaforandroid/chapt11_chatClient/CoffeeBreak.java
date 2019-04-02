package com.example.vtewe.rxjava.rxjavaforandroid.chapt11_chatClient;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;

public class CoffeeBreak {

    public static void main(String[] args) {
        CoffeeBreak coffeeBreak = new CoffeeBreak();
        Observable<Integer> observable = coffeeBreak.createCountingObservable();
        observable.subscribe(integer -> System.out.println("Number of subscribers: " + integer.toString()));
        observable.subscribe(integer -> System.out.println("Number of subscribers: " + integer.toString()));
        observable
                .subscribe(integer -> System.out.println("Number of subscribers: " + integer.toString()))
                .dispose();
        observable.subscribe(integer -> System.out.println("Number of subscribers: " + integer.toString()));
    }

    Observable<View> itemClicks(ListView listView) {
        return Observable.create(emitter -> {
            AdapterView.OnItemClickListener listener = (parent, clickedView, pos, id) -> emitter.onNext(clickedView);
            listView.setOnItemClickListener(listener);
            emitter.setDisposable(Disposables.fromAction(
                    () -> {
                        if (listView.getOnItemClickListener() == listener) {
                            listView.setOnItemClickListener(null);
                        }
                    })
            );
        });
    }

    Observable<Integer> createCountingObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            int numberOfSubscribers = 0;

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(++numberOfSubscribers);
                emitter.setDisposable(Disposables.fromAction(() -> numberOfSubscribers--));
            }
        });
    }
}

