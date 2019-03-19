package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameGrid;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

public class GameUtils {

    public static GameSymbol calculateWinnerForGrid(GameGrid gameGrid) {
        final int WIDTH = gameGrid.getWidth();
        final int HEIGHT = gameGrid.getHeight();
        for (int r = 0; r < WIDTH; r++) {
            for (int c = 0; c < HEIGHT; c++) {
                GameSymbol player = gameGrid.getSymbolAt(r, c);
                if (player == GameSymbol.EMPTY)
                    continue;
                if (c + 3 < WIDTH &&
                        player == gameGrid.getSymbolAt(r, c+1) &&
                        player == gameGrid.getSymbolAt(r, c+2) &&
                        player == gameGrid.getSymbolAt(r, c+3))
                    return player;
                if (r + 3 < HEIGHT) {
                    if (player == gameGrid.getSymbolAt(r+1, c) &&
                            player == gameGrid.getSymbolAt(r+2, c) &&
                            player == gameGrid.getSymbolAt(r+3, c))
                        return player;
                    if (c + 3 < WIDTH &&
                            player == gameGrid.getSymbolAt(r+1, c+1) &&
                            player == gameGrid.getSymbolAt(r+2, c+2) &&
                            player == gameGrid.getSymbolAt(r+3, c+3))
                        return player;
                    if (c - 3 >= 0 &&
                            player == gameGrid.getSymbolAt(r+1, c-1) &&
                            player == gameGrid.getSymbolAt(r+2, c-2) &&
                            player == gameGrid.getSymbolAt(r+3, c-3))
                        return player;
                }
            }
        }
        return null;
    }

    public static GameStatus calculateGameStatus(GameGrid gameGrid) {
        final int WIDTH = gameGrid.getWidth();
        final int HEIGHT = gameGrid.getHeight();
        for (int r = 0; r < WIDTH; r++) {
            for (int c = 0; c < HEIGHT; c++) {
                GameSymbol player = gameGrid.getSymbolAt(r, c);
                if (player == GameSymbol.EMPTY)
                    continue;

                if (c + 3 < WIDTH &&
                        player == gameGrid.getSymbolAt(r, c+1) &&
                        player == gameGrid.getSymbolAt(r, c+2) &&
                        player == gameGrid.getSymbolAt(r, c+3))
                    return GameStatus.ended(player,
                            new GridPosition(r, c),
                            new GridPosition(r, c+3));
                if (r + 3 < HEIGHT) {
                    if (player == gameGrid.getSymbolAt(r+1, c) &&
                            player == gameGrid.getSymbolAt(r+2, c) &&
                            player == gameGrid.getSymbolAt(r+3, c))
                        return GameStatus.ended(player,
                                new GridPosition(r, c),
                                new GridPosition(r+3, c));
                    if (c + 3 < WIDTH &&
                            player == gameGrid.getSymbolAt(r+1, c+1) &&
                            player == gameGrid.getSymbolAt(r+2, c+2) &&
                            player == gameGrid.getSymbolAt(r+3, c+3))
                        return GameStatus.ended(player,
                                new GridPosition(r, c),
                                new GridPosition(r+3, c+3));
                    if (c - 3 >= 0 &&
                            player == gameGrid.getSymbolAt(r+1, c-1) &&
                            player == gameGrid.getSymbolAt(r+2, c-2) &&
                            player == gameGrid.getSymbolAt(r+3, c-3))
                        return GameStatus.ended(player,
                                new GridPosition(r, c),
                                new GridPosition(r+3, c-3));
                }
            }
        }
        return GameStatus.ongoing();
    }

    public static GridPosition calculateNewDropPosition(GridPosition gridPosition, GameGrid gameGrid){
        int i = gameGrid.getHeight() - 1;
        for (; i >= -1; i--) {
            if (i == -1) {
                // Let -1 fall through
                break;
            }
            GameSymbol symbol =
                    gameGrid.getSymbolAt(
                            gridPosition.getX(), i);
            if (symbol == GameSymbol.EMPTY) {
                break;
            }
        }
        return new GridPosition(
                gridPosition.getX(), i);
    }
}
