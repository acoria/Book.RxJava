package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.FullGameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameGrid;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GridPosition;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;

public class TicTacToeActivity extends AppCompatActivity {

    public final static String TAG = TicTacToeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        GameGridView gameGridView = findViewById(R.id.grid_view);

        Observable<MotionEvent> userTouchObservable = RxView.touches(gameGridView);
        userTouchObservable
                .filter(event -> event.getAction() == MotionEvent.ACTION_UP)
                .map(event -> convertPixelsToGridPosition(
                        event.getX(),event.getY(),
                        gameGridView.getWidth(), gameGridView.getHeight(),
                        gameGridView.getGridWidth(), gameGridView.getGridHeight()))
                .subscribe(
                    (gridPosition) -> Log.d(TAG, "gridPosX: " + gridPosition.getX() + " gridPosY: " + gridPosition.getY())
//                (event) -> Toast.makeText(this, "Touched me!", Toast.LENGTH_LONG).show()
        );

        testfillGridView();

    }

    private GridPosition convertPixelsToGridPosition(
            float touchX, float touchY,
            int viewWidthPixels, int viewHeightPixels,
            int gridWidth, int gridHeight){

        // Horizontal GridPosition coordinate as i
        float rx = touchX /
                (float)(viewWidthPixels+1);
        int i = (int)(rx * gridWidth);
        // Vertical GridPosition coordinate as n
        float ry = touchY /
                (float)(viewHeightPixels+1);
        int n = (int)(ry * gridHeight);
        return new GridPosition(i, n);
    }

    private void testfillGridView() {
        GameGridView gameGridView = findViewById(R.id.grid_view);

        GameSymbol[][] gameSymbol =
                new GameSymbol[][] {
                        new GameSymbol[]{
                                GameSymbol.BLACK, GameSymbol.EMPTY, GameSymbol.EMPTY
                        },
                        new GameSymbol[]{
                                GameSymbol.BLACK, GameSymbol.RED, GameSymbol.EMPTY
                        },
                        new GameSymbol[]{
                                GameSymbol.RED, GameSymbol.EMPTY, GameSymbol.EMPTY
                        }

                };
        GameGrid gameGrid = new GameGrid(3, 3, gameSymbol);
        GameState gameState = new GameState(gameGrid, GameSymbol.BLACK);
        GameStatus gameStatus = GameStatus.ongoing();

        gameGridView.setData(new FullGameState(gameState, gameStatus));
    }
}
