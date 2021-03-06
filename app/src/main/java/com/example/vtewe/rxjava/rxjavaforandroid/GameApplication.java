package com.example.vtewe.rxjava.rxjavaforandroid;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data.GameModel;

public class GameApplication extends CustomApplication {
    private GameModel gameModel;

    @Override
    public void onCreate() {
        super.onCreate();
        gameModel = new GameModel(this);
    }

    public GameModel getGameModel() {
        return gameModel;
    }
}
