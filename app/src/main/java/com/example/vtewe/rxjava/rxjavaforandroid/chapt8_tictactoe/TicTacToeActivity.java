package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GridPosition;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.view.InteractiveGameGridView;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.view.PlayerView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TicTacToeActivity extends AppCompatActivity {

    public final static String TAG = TicTacToeActivity.class.getSimpleName();
    GameViewModel viewModel;
    private PlayerView playerInTurnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        InteractiveGameGridView gameGridView = findViewById(R.id.grid_view);
        playerInTurnView = findViewById(R.id.player_in_turn_image_view);

        Observable<GridPosition> gridPositionEventObservable = gameGridView.getTouchesOnGrid();

        viewModel = new GameViewModel(gridPositionEventObservable);
        viewModel.getGameState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gameGridView::setData);

        viewModel.getPlayerInTurn()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playerInTurnView::setData);

        viewModel.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
    }

    private void testfillGrid(){
        //        GameSymbol[][] gameSymbol =
//                new GameSymbol[][]{
//                        new GameSymbol[]{
//                                GameSymbol.EMPTY, GameSymbol.EMPTY, GameSymbol.EMPTY
//                        },
//                        new GameSymbol[]{
//                                GameSymbol.EMPTY, GameSymbol.EMPTY, GameSymbol.EMPTY
//                        },
//                        new GameSymbol[]{
//                                GameSymbol.EMPTY, GameSymbol.EMPTY, GameSymbol.EMPTY
//                        }
//
//                };

    }
}
