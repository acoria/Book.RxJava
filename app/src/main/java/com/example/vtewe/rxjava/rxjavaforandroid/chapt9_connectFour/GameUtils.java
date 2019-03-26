package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import android.support.v4.util.Pair;
import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameGrid;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

import io.reactivex.Observable;


public class GameUtils {
    private static final String TAG = GameUtils.class.getSimpleName();

    public static Observable<GameState> processGameMoves(
            Observable<GridPosition> touchEventObservable,
            Observable<GameStatus> gameStatusObservable,
            Observable<GameState> activeGameStateObservable,
            Observable<GameSymbol> playerInTurnObservable){

        Observable<GridPosition> filteredTouchesEventObservable =
                touchEventObservable
                        //filter when game has ended
                        .withLatestFrom(gameStatusObservable, Pair::new)
                        .filter(pair -> !pair.second.isEnded())
                        .map(pair -> pair.first)
                        .withLatestFrom(activeGameStateObservable, Pair::new)
                        .map(pair -> GameUtils.calculateNewDropPosition(pair.first,pair.second.getGameGrid()))
                        //filter negative Y (when calculateNewDrop returned -1 for Y)
                        .filter(gridPosition -> gridPosition.getY() >= 0);

        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(activeGameStateObservable, playerInTurnObservable, Pair::new);

        return filteredTouchesEventObservable
                //gets the latest value from another observable, without triggering the chain for new value coming from it
                .withLatestFrom(
                        gameInfoObservable,
                        (gridPosition, gameInfo) -> {
                            Log.d(TAG, gridPosition.toString());
                            return gameInfo.first.setSymbolAt(gridPosition, gameInfo.second);});
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
