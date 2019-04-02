package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data;


import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ChatStore {

    private List<ChatMessage> cache = new ArrayList<>();
    private PublishSubject<List<ChatMessage>> subject = PublishSubject.create();

    public void put(ChatMessage chatMessage){
        cache.add(chatMessage);
        subject.onNext(cache);
    }

    public Observable<List<ChatMessage>> getAll(){
        return subject.hide();
    }
}
