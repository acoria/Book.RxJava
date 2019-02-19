package com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt6_fileBrowser.FileListAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

public class FileBrowserActivityWithStore extends AppCompatActivity {

    final static String TAG = FileBrowserActivityWithStore.class.getSimpleName();
    ListView listView;
    FileBrowserViewModelWithStore viewModel;
    FileBrowserModel fileBrowserModel;
    File rootFile;
    Observable<File> listItemClickObservable;
    Observable<Object> backButtonObservable;
    Observable<Object> rootButtonObservable;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        listView = findViewById(R.id.list_view);
        rootFile = new File(Environment.getRootDirectory().getPath());

        setTitle("File Browser");

        setupInputObservables();
        setupModel();
        setupViewModel();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            initWithPermissions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //as soon as the view model connects back to the UI, the latest value is send immediately
        makeViewBinding();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //the UI does not need to be informed upon changes at this point
        releaseViewBinding();
    }

    private void makeViewBinding() {
        compositeDisposable.add(
                viewModel.getFileListObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateList
                                ,e -> Log.e(TAG, "Error reading files", e))
        );
    }

    private void releaseViewBinding() {
        compositeDisposable.clear();
    }

    private void initWithPermissions() {
        //potential API call results should still be received (but not be displayed yet in the UI)
        viewModel.subscribe();
    }

    private void setupInputObservables() {
        listItemClickObservable =
                Observable.create(emitter ->
                        listView.setOnItemClickListener(
                                ((parent, view, position, id) -> {
                                    final File file = (File) view.getTag();
                                    Log.d(TAG, "Selected: " + file);
                                    if(file.isDirectory()){
                                        emitter.onNext(file);
                                    }
                                })
                        )
                );
        backButtonObservable = RxView.clicks(findViewById(R.id.btn_previous));
        rootButtonObservable = RxView.clicks(findViewById(R.id.btn_root));
    }

    private void setupModel() {
        fileBrowserModel = new FileBrowserModel(rootFile, this::createFilesObservable);
    }

    private void setupViewModel() {
        viewModel = new FileBrowserViewModelWithStore(
                fileBrowserModel,
                listItemClickObservable,
                backButtonObservable,
                rootButtonObservable,
                rootFile
        );
    }


    List<File> getFiles(File dirOrFile) {
        List<File> fileList = new ArrayList<>();
        File[] files = dirOrFile.listFiles();
        if(files == null) return null;
        for (File file : files) {
            if (!file.isHidden() && file.canRead()) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    Observable<List<File>> createFilesObservable(File file) {
        // Create a new observable based on the file we received in the arguments
        return Observable.create(emitter -> {
            try {
                final List<File> fileList = getFiles(file);
                emitter.onNext(fileList);
                emitter.onComplete();
            } catch (Exception e) {
                // An exception was thrown, let the subscriber handle it as it sees fit
                emitter.onError(e);
            }
        });
    }
    private void updateList(List<File> files) {
        FileListAdapter fileListAdapter = new FileListAdapter(this, R.layout.array_adapter_textview, files);
        listView.setAdapter(fileListAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initWithPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unsubscribe();
    }
}
