package com.example.vtewe.rxjava;

import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.GameUtils;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameGrid;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class GameUtilsUnitTest {
    private static final GameGrid EMPTY_GRID = new GameGrid(3, 3);
    public static final GameState EMPTY_GAME = new GameState(EMPTY_GRID, GameSymbol.EMPTY);
    PublishSubject<GridPosition> touchEventMock;
    BehaviorSubject<GameStatus> gameStatusMock;
    BehaviorSubject<GameState> activeGameStateMock;
    BehaviorSubject<GameSymbol> playerInTurnMock;
    TestObserver<GameState> testObserver;
    Observable<GameState> gameStateObservable;

    GameSymbol nextSymbol = GameSymbol.RED;

    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);
        GameState gameState = new GameState(EMPTY_GRID,GameSymbol.EMPTY);

        GameStatus gameStatus = GameUtils.calculateGameStatus(EMPTY_GRID);
        gameStatusMock = BehaviorSubject.createDefault(gameStatus);

        activeGameStateMock = BehaviorSubject.createDefault(gameState);

        playerInTurnMock = BehaviorSubject.createDefault(nextSymbol);
        touchEventMock = PublishSubject.create();

        testObserver = new TestObserver<>();
        gameStateObservable = GameUtils.processGameMoves(touchEventMock, gameStatusMock, activeGameStateMock, playerInTurnMock);
    }

    @Test
    public void testDropOn00(){
        activeGameStateMock.onNext(EMPTY_GAME);
        gameStateObservable.subscribe(testObserver);

        testObserver.assertValueCount(0);

        //pretend to click on 0/0-Grid
        touchEventMock.onNext(new GridPosition(0,0));
        testObserver.assertValueCount(1);

        //check if it got dropped to the bottom
        GameState actualGameState = testObserver.values().get(0);
        GameState expectedGameState = EMPTY_GAME.setSymbolAt(new GridPosition(0,2), nextSymbol);
        assertTrue(actualGameState.equals(expectedGameState));

    }
}
