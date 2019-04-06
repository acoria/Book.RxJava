package com.example.vtewe.rxjava.rxjavaforandroid;

import android.app.Application;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt12_chatClientExtended.ChatModel;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data.GameModel;

public class CustomApplication extends Application {

    private GameModel gameModel;
    ChatModel chatModel;

    @Override
    public void onCreate() {
        super.onCreate();
        gameModel = new GameModel(this);
        chatModel = new ChatModel();
        chatModel.onCreate();
    }

    public GameModel getGameModel() {
        return gameModel;
    }
    public ChatModel getChatModel(){
        return chatModel;
    }

}
