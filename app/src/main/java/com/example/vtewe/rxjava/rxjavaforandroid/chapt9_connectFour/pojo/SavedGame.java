package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo;

public class SavedGame {
    GameState gameState;
    long timestamp;

    public SavedGame(GameState gameState, long timestamp) {
        this.gameState = gameState;
        this.timestamp = timestamp;
    }

    public long getTimestamp(){
        return timestamp;
    }
    public GameState getGameState(){
        return gameState;
    }
}
