package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import android.support.v4.util.Pair;
import android.util.Log;


import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.FullGameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameGrid;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class GameViewModel {
    public final String TAG = GameViewModel.class.getSimpleName();

    private static final int GRID_WIDTH = 7;
    private static final int GRID_HEIGHT = 7;
    private static final GameGrid EMPTY_GRID = new GameGrid(GRID_WIDTH, GRID_HEIGHT);
    private static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);

    private final CompositeDisposable subscriptions = new CompositeDisposable();
    private final BehaviorSubject<GameState> gameStateSubject = BehaviorSubject.createDefault(EMPTY_GAME);

    private final Observable<GameSymbol> playerInTurnObservable;
    private final Observable<GridPosition> touchEventObservable;
    private final Observable<Object> newGameEventObservable;
    private Observable<GameStatus> gameStatusObservable;


    public GameViewModel(Observable<GridPosition> touchEventObservable, Observable<Object> newGameEventObservable){
        this.touchEventObservable = touchEventObservable;
        this.newGameEventObservable = newGameEventObservable;

        playerInTurnObservable =
                gameStateSubject
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
                gameStateSubject
                    .map(gameState -> retrieveNewGameStatus(gameState.getGameGrid()));
    }

    public Observable<FullGameState> getFullGameState(){
        return Observable.combineLatest(gameStateSubject,gameStatusObservable, FullGameState::new).hide();
    }

    public Observable<GameSymbol> getPlayerInTurn() {
        return playerInTurnObservable;
    }

    public Observable<GameStatus> getGameStatus(){ return gameStatusObservable;}

    private static GameStatus retrieveNewGameStatus(GameGrid gameGrid){
        return GameUtils.calculateGameStatus(gameGrid);
    }

    public void subscribe(){

        subscriptions.add(newGameEventObservable
                .map(ignore -> EMPTY_GAME)
                .subscribe(gameStateSubject::onNext));

        Observable<GridPosition> filteredTouchesEventObservable =
                touchEventObservable
                //filter when game has ended
                .withLatestFrom(gameStatusObservable, Pair::new)
                .filter(pair -> !pair.second.isEnded())
                .map(pair -> pair.first)
                //filter when an occupied grid is touched
                .withLatestFrom(gameStateSubject, Pair::new)
                .map(pair -> GameUtils.calculateNewDropPosition(pair.first,pair.second.getGameGrid()))
                //filter negative Y (when calculateNewDrop returned -1 for Y)
                .filter(gridPosition -> gridPosition.getY() >= 0);


        Observable<Pair<GameState, GameSymbol>> gameInfoObservable =
                Observable.combineLatest(gameStateSubject, playerInTurnObservable, Pair::new);

        subscriptions.add(filteredTouchesEventObservable
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
