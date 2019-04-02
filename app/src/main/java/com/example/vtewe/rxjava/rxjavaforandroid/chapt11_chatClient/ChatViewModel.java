package com.example.vtewe.rxjava.rxjavaforandroid.chapt11_chatClient;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class ChatViewModel {

    private static final String TAG = ChatViewModel.class.getSimpleName();
    private BehaviorSubject<List<String>> messageListSubject = BehaviorSubject.create();
    private final Observable<String> chatMessageObservable;
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public ChatViewModel(Observable<String> chatMessageObservable) {
        this.chatMessageObservable = chatMessageObservable;
    }

    public Observable<List<String>> getMessageList(){
        return messageListSubject.hide();
   }

   public void subscribe(){
       Gson gson = new Gson();
       subscriptions.add(chatMessageObservable
               .map(json ->
                   gson.fromJson(json, ChatMessage.class))
               .scan(
                       new ArrayList<>(), ChatViewModel::arrayAccumulatorFunction)
               .flatMap(list ->
                       Observable.fromIterable(list)
                               .map(ChatMessage::toString)
                               .toList()
                               .toObservable())
               .subscribe(messageListSubject::onNext));
   }
    public void unsubscribe(){
        subscriptions.clear();
    }

    static List<ChatMessage> arrayAccumulatorFunction( List<ChatMessage> previousMessagesList, ChatMessage newMessage) {
        List<ChatMessage> newMessagesList = new ArrayList<>(previousMessagesList);
        newMessagesList.add(newMessage);
        return newMessagesList;
    }
}
