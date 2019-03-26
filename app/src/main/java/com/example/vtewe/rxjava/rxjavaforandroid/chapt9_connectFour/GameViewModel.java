package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.FullGameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class GameViewModel {
    public final String TAG = GameViewModel.class.getSimpleName();

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final Observable<GameSymbol> playerInTurnObservable;
    private final Observable<GridPosition> touchEventObservable;
    private Observable<GameStatus> gameStatusObservable;
    private Consumer<GameState> putActiveGameState;
    private Observable<GameState> activeGameStateObservable;


    public GameViewModel(
            Observable<GameState> activeGameStateObservable,
            Consumer<GameState> putActiveGameState,
            Observable<GridPosition> touchEventObservable){
        this.touchEventObservable = touchEventObservable;
        this.putActiveGameState = putActiveGameState;
        this.activeGameStateObservable = activeGameStateObservable;
        playerInTurnObservable =
                this.activeGameStateObservable
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
                this.activeGameStateObservable
                    .map(GameState::getGameGrid)
                    .map(GameUtils::calculateGameStatus);
    }

    public Observable<FullGameState> getFullGameState(){
        return Observable.combineLatest(activeGameStateObservable, gameStatusObservable, FullGameState::new).hide();
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public Observable<GameStatus> getGameStatus(){ return gameStatusObservable;}


    public void subscribe(){
        subscriptions.add(GameUtils.processGameMoves(touchEventObservable, gameStatusObservable,activeGameStateObservable, playerInTurnObservable)
                .subscribe(putActiveGameState));
    }

    public void unsubscribe(){
        subscriptions.clear();
    }
}
