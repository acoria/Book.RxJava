package com.example.vtewe.rxjava.rxjavaforandroid.chapt11_chatClient;

import java.util.Date;
import java.util.UUID;

public class ChatMessage {
    String id;
    long timestamp; //without time zone
    String message;

    public ChatMessage(String message){
        this.id = UUID.randomUUID().toString();
        this.timestamp = new Date().getTime();
        this.message = message;
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

    @Override
    public String toString() {
        return message;
    }
}
