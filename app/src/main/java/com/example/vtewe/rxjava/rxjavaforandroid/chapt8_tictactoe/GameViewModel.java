package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe;

import android.support.v4.util.Pair;
import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameGrid;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GridPosition;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class GameViewModel {
    public final String TAG = GameViewModel.class.getSimpleName();

    private static final int GRID_WIDTH = 3;
    private static final int GRID_HEIGHT = 3;
    private static final GameGrid EMPTY_GRID = new GameGrid(GRID_WIDTH, GRID_HEIGHT);
    private static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);

    private final CompositeDisposable subscriptions = new CompositeDisposable();
    private final BehaviorSubject<GameState> gameStateSubject = BehaviorSubject.createDefault(EMPTY_GAME);

    private final Observable<GameSymbol> playerInTurnObservable;
    private final Observable<GridPosition> touchEventObservable;


    public GameViewModel(Observable<GridPosition> touchEventObservable){
        this.touchEventObservable = touchEventObservable;

        playerInTurnObservable =
                gameStateSubject
                        .map(GameState::getLastPlayedSymbol)
                        .map(symbol -> {
                            Log.d(TAG,"playerInTurnObservable - symbol");
                            if(symbol == GameSymbol.RED){
                                return GameSymbol.BLACK;
                            }else{
                                return GameSymbol.RED;
                            }
                        });
    }

    public Observable<GameState> getGameState(){
        return gameStateSubject.hide();
//                .map(GameState::getGameGrid);
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public void subscribe(){

        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateSubject, playerInTurnObservable, Pair::new);

        subscriptions.add(touchEventObservable
                //gets the latest value from another observable, without triggering the chain for new value coming from it
                .withLatestFrom(
                        gameInfoObservable,
                        (gridPosition, gameInfo) -> {
                            Log.d(TAG, gridPosition.toString());
                            return gameInfo.first.setSymbolAt(gridPosition, gameInfo.second);}
                )
                .subscribe(gameStateSubject::onNext));
    }

    public void unsubscribe(){
        subscriptions.clear();
    }
}
