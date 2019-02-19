package com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores;

import android.util.Log;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

public class FileBrowserViewModelWithStore {

    final static String TAG = FileBrowserViewModelWithStore.class.getSimpleName();
    private Observable<File> listItemClickObservable;
    private Observable<Object> backButtonObservable;
    private Observable<Object> rootButtonObservable;
    private File fileSystemRoot;
    FileBrowserModel fileBrowserModel;
    private final BehaviorSubject<List<File>> filesOutput = BehaviorSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private File currentlySelectedFile;

    public FileBrowserViewModelWithStore(
            FileBrowserModel fileBrowserModel,
            Observable<File> listItemClickObservable,
            Observable<Object> backButtonObservable,
            Observable<Object> rootButtonObservable,
            File fileSystemRoot) {
        this.fileBrowserModel = fileBrowserModel;
        this.listItemClickObservable = listItemClickObservable;
        this.backButtonObservable = backButtonObservable;
        this.rootButtonObservable = rootButtonObservable;
        this.fileSystemRoot = fileSystemRoot;
    }

    public Observable<List<File>> getFileListObservable(){
        //Subject.hide makes sure the receiver is not able to push more events in the subject
        return filesOutput.hide();
    }

    public void subscribe(){
        Observable<File> fileNavigateBackEventObservable =
                backButtonObservable
                        .map(event -> {
                            File parentDir = currentlySelectedFile.getParentFile();
                            if(parentDir.getParent() != null) {
                                //not the root element
                                return parentDir;
                            }
                            return null; //from root element one cannot navigate up
                        });

        Observable<File> navigateToRootObservable =
                rootButtonObservable.map(event -> fileSystemRoot);

        Observable<File> fileChangedObservable =
                Observable.merge(
                        listItemClickObservable,
                        fileNavigateBackEventObservable,
                        navigateToRootObservable);

        compositeDisposable.add(
                fileChangedObservable
                .subscribe(file -> {
                        fileBrowserModel.putSelectedFile(file);
                        currentlySelectedFile = file;
                })
        );
        compositeDisposable.add(fileBrowserModel.getFilesList()
                .doOnNext(dir -> Log.d(TAG, "Selected dir " + dir))
                .doOnNext(files -> Log.d(TAG, "Found " + files.size() + " files"))
                .subscribe(filesOutput::onNext)
        );

    }
    public void unsubscribe(){
        compositeDisposable.clear();
    }

}
