package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.SavedGame;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PersistedGameStore {
    private final static String TAG = PersistedGameStore.class.getSimpleName();
    private static final String SAVED_GAMES_KEY = "saved_games";
    private final SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    //The list of saved games that is kept in memory. It is loaded on startup from disk
    private List<SavedGame> savedGames;
    private final PublishSubject<List<SavedGame>> savedGamesSubject = PublishSubject.create();

    PersistedGameStore(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
        loadGames();
    }

    public Observable<List<SavedGame>> getSavedGameStream(){
        return savedGamesSubject.hide()
            .startWith(savedGames);
    }

    public Observable<Void> put(GameState gameState) {
        final long timestamp = new Date().getTime();
        final SavedGame savedGame = new SavedGame(gameState, timestamp);
        savedGames.add(savedGame);
        persistGames();
        savedGamesSubject.onNext(savedGames);
        return Observable.empty();
    }

    private void persistGames() {
        String jsonString = gson.toJson(savedGames);
        sharedPreferences.edit()
                .putString(SAVED_GAMES_KEY, jsonString)
                .commit();
        Log.d(TAG, "Games saved");
    }

    private void loadGames(){
        String gamesJson = sharedPreferences.getString(SAVED_GAMES_KEY, "[]");
        try {
            savedGames = gson.fromJson(gamesJson, new TypeToken<List<SavedGame>>(){}.getType());
            Log.d(TAG, "Loaded " + savedGames.size() + " games");
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "Failed to load games");
        }
    }
}
