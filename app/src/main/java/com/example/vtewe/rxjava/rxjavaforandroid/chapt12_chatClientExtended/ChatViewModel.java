package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended;

import android.support.v4.util.Pair;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data.ChatMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class ChatViewModel {

    private static final String TAG = ChatViewModel.class.getSimpleName();
    private BehaviorSubject<List<String>> messageListSubject = BehaviorSubject.create();
    private final Observable<Collection<ChatMessage>> chatMessageCollectionObservable;
    private Observable<String> searchFieldObservable;
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public ChatViewModel(
            Observable<Collection<ChatMessage>> chatMessageObservable,
            Observable<String> searchFieldObservable) {
        this.chatMessageCollectionObservable = chatMessageObservable;
        this.searchFieldObservable = searchFieldObservable;
    }

    public Observable<List<String>> getMessageList(){
        return messageListSubject.hide();
   }

   public void subscribe(){

        //sort the collection by timestamp
        Observable<List<ChatMessage>> chatMessageListObservable = chatMessageCollectionObservable
                .map(collectionList -> {
                    List<ChatMessage> chatMessages = new ArrayList<>(collectionList);
                    Collections.sort(chatMessages, this::chatMessageComparator);
                    return chatMessages;
                });

       subscriptions.add(Observable.combineLatest(chatMessageListObservable,searchFieldObservable,Pair::new)
//               (chatMessages, searchText) -> new Pair(chatMessages,searchText))
               .flatMap(pair -> Observable.fromIterable(pair.first)
                       .filter(chatMessage -> {
                           if (pair.second.isEmpty()) {
                               return true;
                           }
                           return chatMessage.toString().contains(pair.second);
                       })
                       .map(ChatViewModel::formatMessage)
                       .toList()
                       .toObservable())
               .subscribe(messageListSubject::onNext));
   }
    public void unsubscribe(){
        subscriptions.clear();
    }

    private static String formatMessage(ChatMessage message){
        StringBuilder builder = new StringBuilder();
        builder.append(message.getMessage());
        if(message.isPending()){
            builder.append(" (pending)");
        }
        return builder.toString();
    }

    private int chatMessageComparator(ChatMessage a, ChatMessage b) {
        if (a.getTimestamp() > b.getTimestamp()) {
            return 1;
        } else if (a.getTimestamp() < b.getTimestamp()) {
            return -1;
        }
        return 0;
    }

}
