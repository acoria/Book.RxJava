package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import android.support.v4.util.Pair;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class ChatViewModel {

    private static final String TAG = ChatViewModel.class.getSimpleName();
    private BehaviorSubject<List<String>> messageListSubject = BehaviorSubject.create();
    private final Observable<List<ChatMessage>> chatMessageObservable;
    private Observable<String> searchFieldObservable;
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public ChatViewModel(
            Observable<List<ChatMessage>> chatMessageObservable,
            Observable<String> searchFieldObservable) {
        this.chatMessageObservable = chatMessageObservable;
        this.searchFieldObservable = searchFieldObservable;
    }

    public Observable<List<String>> getMessageList(){
        return messageListSubject.hide();
   }

   public void subscribe(){
       subscriptions.add(Observable.combineLatest(chatMessageObservable,searchFieldObservable,Pair::new)
//               (chatMessages, searchText) -> new Pair(chatMessages,searchText))
               .flatMap(pair -> Observable.fromIterable(pair.first)
                       .filter(chatMessage -> {
                           if (pair.second.isEmpty()) {
                               return true;
                           }
                           return chatMessage.toString().contains(pair.second);
                       })
                       .map(chatMessage -> chatMessage.toString())
                       .toList()
                       .toObservable())
               .subscribe(messageListSubject::onNext));
   }
    public void unsubscribe(){
        subscriptions.clear();
    }
}
