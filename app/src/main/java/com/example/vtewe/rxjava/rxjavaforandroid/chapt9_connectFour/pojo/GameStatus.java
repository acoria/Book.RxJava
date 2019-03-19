package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo;

public class GameStatus {
    private boolean isEnded;
    private GameSymbol winner;
    GridPosition winningPositionStart;
    GridPosition winningPositionEnd;


    public GameStatus(GameSymbol winner,
                      GridPosition winningPositionStart,
                      GridPosition winningPositionEnd){
        this.winner = winner;
        this.winningPositionStart = winningPositionStart;
        this.winningPositionEnd = winningPositionEnd;
    }

    public boolean isEnded() {
        return winner != null;
    }

    public GameSymbol getWinner() {
        return winner;
    }

    public static GameStatus ended(GameSymbol winner,
                                   GridPosition winningPositionStart,
                                   GridPosition winningPositionEnd) {
        return new GameStatus(winner, winningPositionStart, winningPositionEnd);
    }

    public GridPosition getWinningPositionStart() {
        return winningPositionStart;
    }

    public GridPosition getWinningPositionEnd() {
        return winningPositionEnd;
    }

    public static GameStatus ongoing() {
        return new GameStatus(null,null,null);
    }
}
