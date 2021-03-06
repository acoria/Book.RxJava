package com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores.first_mini_store;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class FileBrowserModel {

    private PublishSubject<File> selectedFolderSubject;
    //the selectedFolder acts a "mini-store"
    private File selectedFolder = null;
    private Observable<List<File>> filesListObservable = null;

    public FileBrowserModel(File sytemRootFile, Function<File, Observable<List<File>>> getFiles) {
        selectedFolderSubject = PublishSubject.create();

        filesListObservable = selectedFolderSubject
                .switchMap(file -> getFiles.apply(file))
                .subscribeOn(Schedulers.io());
    }

    public Observable<File> getSelectedFile(){
        if(selectedFolder == null){
            return selectedFolderSubject.hide();
        }
        return selectedFolderSubject.hide()
                .startWith(selectedFolder);
    }

    public void putSelectedFile(File file){
        selectedFolder = file;
        selectedFolderSubject.onNext(file);
    }

    public Observable<List<File>> getFilesList(){
        return filesListObservable;
    }
}
