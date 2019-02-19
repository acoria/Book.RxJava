package com.example.vtewe.rxjava.rxjavaforandroid.chapt2_newsarticle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.vtewe.rxjava.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RxJavaForAndroidNewsFeed extends AppCompatActivity {

    private final String TAG = RxJavaForAndroidNewsFeed.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_for_android_book);
        handleNewsFeed();
        handleTextFields();

    }

    private void handleTextFields() {
        Observable<CharSequence> firstTextObservable = RxTextView.textChanges(findViewById(R.id.firstTextView));
        Observable<CharSequence> secondTextObservable = RxTextView.textChanges(findViewById(R.id.secondTextView));

        Observable<CharSequence> combinedTextObservable = Observable.combineLatest(firstTextObservable, secondTextObservable,
                (firstText, secondText) -> {
                    CharSequence combinedText;
                    combinedText = firstText + "" + secondText;
                    return combinedText;
                });

        combinedTextObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateThirdTextField);

    }

    private void updateThirdTextField(CharSequence text){
        ((TextView) findViewById(R.id.thirdTextView)).setText(text);
    }

    private void handleNewsFeed() {
        Observable<List<Entry>> purpleFeedObservable =
                FeedObservable.getFeed("https://news.google.com/?output=atom");
//        Observable<List<Entry>> yellowFeedObservable =
//                FeedObservable.getFeed("http://www.theregister.co.uk/software/headlines.atom");

//        Observable<List<Entry>> combinedObservable = Observable.combineLatest(purpleFeedObservable, yellowFeedObservable,
//                (purpleList, yellowList) ->{
//                    final List<Entry> list = new ArrayList<>();
//                    list.addAll(purpleList);
//                    list.addAll(purpleList);
//                    return list;
//                });
//        combinedObservable
        purpleFeedObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::drawList);
    }

    private void drawList(List<Entry> entries){
        for(Entry entry : entries){
            Log.i(TAG, entry.toString());
        }
    }


}
