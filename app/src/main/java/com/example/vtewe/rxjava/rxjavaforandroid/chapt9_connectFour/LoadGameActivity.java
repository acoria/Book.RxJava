package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.CustomApplication;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data.GameModel;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.SavedGame;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LoadGameActivity extends AppCompatActivity {

    private GameModel gameModel;
    private CompositeDisposable subscriptions = new CompositeDisposable();
    private Observable<List<SavedGame>> savedGamesObservable;
    private ListView listView;
    private SavedGamesListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        setTitle("Load Previous Game");

        // Get the shared GameModel
        gameModel = ((CustomApplication) getApplication()).getGameModel();

        listView = findViewById(R.id.saved_games_list);
        listAdapter = new SavedGamesListAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
                SavedGame savedGame = (SavedGame) view.getTag();
                gameModel.putActiveGameState(savedGame.getGameState());
                finish();
        });

        savedGamesObservable = gameModel.getSavedGamesStream();
        makeViewBindings();

    }

    private void makeViewBindings() {
        subscriptions.add(
                savedGamesObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(savedGames -> updateListView(savedGames)));
    }

    private void updateListView(List<SavedGame> savedGames) {
        listAdapter.clear();
        listAdapter.addAll(savedGames);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }
}
