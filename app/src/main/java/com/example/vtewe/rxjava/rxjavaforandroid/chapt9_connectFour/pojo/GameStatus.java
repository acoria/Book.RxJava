package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo;

public class GameStatus {
    private boolean isEnded;
    private GameSymbol winner;


    public GameStatus(GameSymbol winner){
        this.winner = winner;
    }

    public boolean isEnded() {
        return winner != null;
    }

    public GameSymbol getWinner() {
        return winner;
    }

    public static GameStatus ended(GameSymbol winner){
        return new GameStatus(winner);
    }

    public static GameStatus ongoing() {
        return new GameStatus(null);
    }
}
