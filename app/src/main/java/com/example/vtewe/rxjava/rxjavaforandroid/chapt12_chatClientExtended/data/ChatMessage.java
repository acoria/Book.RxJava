package com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.data;

import java.util.Date;
import java.util.UUID;

public class ChatMessage {
    private boolean isPending;
    String id;
    long timestamp; //without time zone
    String message;

    public ChatMessage(String message){
        this.id = UUID.randomUUID().toString();
        this.timestamp = new Date().getTime();
        this.message = message;
        this.isPending = true;
    }

    private ChatMessage(ChatMessage oldChatMessage){
        this.id = oldChatMessage.getId();
        this.timestamp = oldChatMessage.getTimestamp();
        this.message = oldChatMessage.getMessage();
        this.isPending = oldChatMessage.isPending();
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isPending() {
        return isPending;
    }

    public ChatMessage setPending(boolean isPending) {
        ChatMessage newChatMessage = new ChatMessage(this);
        newChatMessage.isPending = isPending;
        return newChatMessage;
    }

    @Override
    public String toString() {
        return message;
    }
}
