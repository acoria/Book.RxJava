package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import android.support.v4.util.Pair;
import android.util.Log;


import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data.GameModel;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.FullGameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;


public class GameViewModel {
    public final String TAG = GameViewModel.class.getSimpleName();

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final Observable<GameSymbol> playerInTurnObservable;
    private final Observable<GridPosition> touchEventObservable;
    private Observable<GameStatus> gameStatusObservable;
    private GameModel gameModel;
    private Observable<GameState> gameStateObservable;


    public GameViewModel(
            GameModel gameModel,
            Observable<GridPosition> touchEventObservable){
        this.touchEventObservable = touchEventObservable;
        this.gameModel = gameModel;
        this.gameStateObservable = gameModel.getActiveGameState();
        playerInTurnObservable =
                gameStateObservable
                        .map(GameState::getLastPlayedSymbol)
                        .map(symbol -> {
                            Log.d(TAG,"playerInTurnObservable - symbol");
                            if(symbol == GameSymbol.RED){
                                return GameSymbol.BLACK;
//                            }else if(symbol == GameSymbol.CIRCLE){
//                                return GameSymbol.TRIANGLE;
                            }else{
                                return GameSymbol.RED;
                            }
                        });

        gameStatusObservable =
                gameStateObservable
                    .map(GameState::getGameGrid)
                    .map(GameUtils::calculateGameStatus);
    }

    public Observable<FullGameState> getFullGameState(){
        return Observable.combineLatest(gameStateObservable, gameStatusObservable, FullGameState::new).hide();
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public Observable<GameStatus> getGameStatus(){ return gameStatusObservable;}


    public void subscribe(){
        Observable<GridPosition> filteredTouchesEventObservable =
                touchEventObservable
                //filter when game has ended
                .withLatestFrom(gameStatusObservable, Pair::new)
                .filter(pair -> !pair.second.isEnded())
                .map(pair -> pair.first)
                //filter when an occupied grid is touched
                .withLatestFrom(gameStateObservable, Pair::new)
                .map(pair -> GameUtils.calculateNewDropPosition(pair.first,pair.second.getGameGrid()))
                //filter negative Y (when calculateNewDrop returned -1 for Y)
                .filter(gridPosition -> gridPosition.getY() >= 0);


        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateObservable, playerInTurnObservable, Pair::new);

        subscriptions.add(filteredTouchesEventObservable
                //gets the latest value from another observable, without triggering the chain for new value coming from it
                .withLatestFrom(
                        gameInfoObservable,
                        (gridPosition, gameInfo) -> {
                            Log.d(TAG, gridPosition.toString());
                            return gameInfo.first.setSymbolAt(gridPosition, gameInfo.second);}
                )
                .subscribe(gameState -> gameModel.putActiveGameState(gameState)));
    }

    public void unsubscribe(){
        subscriptions.clear();
    }
}
