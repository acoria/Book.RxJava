package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class ChatStore {

    private Map<String,ChatMessage> cache = new HashMap<>();
    private BehaviorSubject<Collection<ChatMessage>> subject = BehaviorSubject.create();

    public void put(ChatMessage chatMessage){
        cache.put(chatMessage.getId(),chatMessage);
        subject.onNext(cache.values());
    }

    public Observable<Collection<ChatMessage>> getStream(){
        return subject.hide();
    }
}
