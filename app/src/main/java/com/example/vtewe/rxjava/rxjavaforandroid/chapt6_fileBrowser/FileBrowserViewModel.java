package com.example.vtewe.rxjava.rxjavaforandroid.chapt6_fileBrowser;

import android.util.Log;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class FileBrowserViewModel {

    final static String TAG = FileBrowserViewModel.class.getSimpleName();
    private Observable<File> listItemClickObservable;
    private Observable<Object> backButtonObservable;
    private Observable<Object> rootButtonObservable;
    private File fileSystemRoot;
    private Function<File, Observable<List<File>>> getFiles;
    private final BehaviorSubject<List<File>> filesOutput = BehaviorSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//    private Observable<String> fileTitleChangedObservable;

    public FileBrowserViewModel(
            Observable<File> listItemClickObservable,
            Observable<Object> backButtonObservable,
            Observable<Object> rootButtonObservable,
            File fileSystemRoot,
            Function<File, Observable<List<File>>> getFiles) {
        this.listItemClickObservable = listItemClickObservable;
        this.backButtonObservable = backButtonObservable;
        this.rootButtonObservable = rootButtonObservable;
        this.fileSystemRoot = fileSystemRoot;
        this.getFiles = getFiles;
    }

    public Observable<List<File>> getFileListObservable(){
        //Subject.hide makes sure the receiver is not able to push more events in the subject
        return filesOutput.hide();
    }

//    public Observable<String> getFileTitleChangeObservable(){
//        return fileTitleChangedObservable;
//    }

    public void subscribe(){
        final BehaviorSubject<File> selectedFile =
                BehaviorSubject.createDefault(fileSystemRoot);

        Observable<File> fileNavigateBackEventObservable =
                backButtonObservable
                        .map(event -> {
                            File currentDir = selectedFile.getValue();
                            File parentDir = currentDir.getParentFile();
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

//        fileTitleChangedObservable =
//                fileChangedObservable
//                    .map(file -> file.toString());

        compositeDisposable.add(
                fileChangedObservable
                .subscribe(selectedFile::onNext)
        );

        compositeDisposable.add(selectedFile
                .doOnNext(dir -> Log.d(TAG, "Selected dir " + dir))
                .switchMap(getFiles)
                .doOnNext(files -> Log.d(TAG, "Found " + files.size() + " files"))
                .subscribe(filesOutput::onNext)
        );

    }
    public void unsubscribe(){
        compositeDisposable.clear();
    }

}
