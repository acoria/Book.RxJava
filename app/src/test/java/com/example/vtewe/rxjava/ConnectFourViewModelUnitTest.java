package com.example.vtewe.rxjava;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.GameViewModel;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.FullGameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;

import android.util.Log;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data.GameModel.EMPTY_GAME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class ConnectFourViewModelUnitTest {

    BehaviorSubject<GameState> activeGameStateMock;
    Consumer<GameState> putActiveGameStateMock;
    PublishSubject<GridPosition> touchEventMock;
    GameViewModel gameViewModel;


    @Before
    public void setup(){
        PowerMockito.mockStatic(Log.class);

        activeGameStateMock = BehaviorSubject.create();
        putActiveGameStateMock = mock(Consumer.class);
        touchEventMock = PublishSubject.create();
        gameViewModel = new GameViewModel(activeGameStateMock,putActiveGameStateMock,touchEventMock);
    }

    @Test
    public void testInitialState(){
        TestObserver<FullGameState> testObserver = new TestObserver<>();

        gameViewModel.getFullGameState()
            .subscribe(testObserver);

        gameViewModel.subscribe();
        activeGameStateMock.onNext(EMPTY_GAME);

        //putActiveGameState should only change on user input
        try {
            verify(putActiveGameStateMock, never()).accept(any());
        }catch(Exception e){
            fail();
        }

        testObserver.assertValueCount(1);
        FullGameState fullGameState = testObserver.values().get(0);
        //game should be calculated as ongoing
        assertFalse(fullGameState.getGameStatus().isEnded());
        assertEquals(EMPTY_GAME,fullGameState.getGameState());

    }

}
