package com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores;

import android.content.SharedPreferences;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class FileBrowserModel {

    private static final String SELECTED_FOLDER_KEY = "selected_folder";
    private Observable<List<File>> filesListObservable = null;
    private SharedPreferencesStore<File> selectedFolderStore;

    public FileBrowserModel(
            Function<File, Observable<List<File>>> getFiles,
            String defaultPath,
            SharedPreferences sharedPreferences) {

        //Load previously persisted value or use the default
//        String persistedSelectedFolderPath = sharedPreferences.getString(SELECTED_FOLDER_KEY, defaultPath);
//        File initialSelectedFolder = new File(persistedSelectedFolderPath);

        selectedFolderStore = new SharedPreferencesStore<>(
                SELECTED_FOLDER_KEY,
                defaultPath,
                sharedPreferences,
                File::getAbsolutePath,
                File::new
        );

        filesListObservable = selectedFolderStore
                .getStream()
                .switchMap(file -> getFiles.apply(file))
                .subscribeOn(Schedulers.io());
    }

    public Observable<File> getSelectedFile(){
        return selectedFolderStore.getStream();
    }

    public void putSelectedFile(File file){
        selectedFolderStore.put(file);
    }

    public Observable<List<File>> getFilesList(){
        return filesListObservable;
    }
}
