package com.example.vtewe.rxjava.rxjavaforandroid.chapt2_newsarticle;

import android.util.Log;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt2_newsarticle.network.FeedParser;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt2_newsarticle.network.RawNetworkObservable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class FeedObservable {
    private static final String TAG = FeedObservable.class.getSimpleName();

    private FeedObservable() {

    }

    public static Observable<List<Entry>> getFeed(final String url) {
        return RawNetworkObservable.create(url)
                .map(response -> {
                    FeedParser parser = new FeedParser();
                    try {
                        List<Entry> entries = parser.parse(response.body().byteStream());
                        Log.v(TAG, "Number of entries from url " + url + ": " + entries.size());
                        return entries;
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing feed", e);
                    }
                    return new ArrayList<>();
                });
    }
}
