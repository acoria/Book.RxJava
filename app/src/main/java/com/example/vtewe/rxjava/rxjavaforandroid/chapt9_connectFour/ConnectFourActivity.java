package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.vtewe.rxjava.R;

import com.example.vtewe.rxjava.rxjavaforandroid.GameApplication;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data.GameModel;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GridPosition;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.view.InteractiveGameGridView;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.view.PlayerView;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.view.WinnerTextView;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ConnectFourActivity extends AppCompatActivity {

    public final static String TAG = com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.ConnectFourActivity.class.getSimpleName();
    CompositeDisposable viewSubscriptions = new CompositeDisposable();
    InteractiveGameGridView gameGridView;
    GameViewModel viewModel;
    private PlayerView playerInTurnView;
    private WinnerTextView winnerTextView;
    private FrameLayout winnerView;
    private Button newGameButton;
    private Button saveGameButton;
    private Button loadGameButton;
    private GameModel gameModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_four);

        resolveViews();

        // Get the shared GameModel
        gameModel = ((GameApplication) getApplication()).getGameModel();

        createViewModel();
        makeViewBinding();
    }

    private void resolveViews() {
        gameGridView = findViewById(R.id.grid_view);
        winnerTextView = findViewById(R.id.winner_text_view);
        winnerView = findViewById(R.id.winner_view);
        playerInTurnView = findViewById(R.id.player_in_turn_image_view);
        newGameButton = findViewById(R.id.new_game_button);
        saveGameButton = findViewById(R.id.save_game_button);
        loadGameButton = findViewById(R.id.load_game_button);
    }

    private void createViewModel() {
        Observable<GridPosition> gridPositionEventObservable = gameGridView.getTouchesOnGrid();

        viewModel = new GameViewModel(
                gameModel.getActiveGameState(),
                gameModel::putActiveGameState,
                gridPositionEventObservable);
        viewModel.subscribe();
    }

    private void makeViewBinding(){

        viewSubscriptions.add(
                RxView.clicks(newGameButton)
                    .subscribe(ignore -> gameModel.newGame()));

        viewSubscriptions.add(
                RxView.clicks(saveGameButton)
                        .subscribe(ignore -> gameModel.saveActiveGame()));

        viewSubscriptions.add(
                RxView.clicks(loadGameButton)
                    .subscribe(ignore -> openLoadGameActivity()));

        viewSubscriptions.add(viewModel.getFullGameState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gameGridView::setData));

        viewSubscriptions.add(viewModel.getPlayerInTurn()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playerInTurnView::setData));

        //could also be moved to the WinnerView
        viewSubscriptions.add(viewModel.getGameStatus()
                .map(GameStatus::isEnded)
                .map(isEnded -> {
                    if(isEnded){Log.d(TAG, "ended");}else{
                        Log.d(TAG, "not yet");
                    }
                    return isEnded;
                })
                .map(isEnded -> isEnded ? View.VISIBLE : View.GONE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(winnerView::setVisibility));

        viewSubscriptions.add(viewModel.getGameStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(winnerTextView::setData));
    }

    private void openLoadGameActivity() {
        Intent intent = new Intent(this, LoadGameActivity.class);
        startActivity(intent);
    }

    private void releaseViewBinding() {
        viewSubscriptions.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseViewBinding();
        viewModel.unsubscribe();
    }
}
