package com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores.VideoModel_CoffeeBreak;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class VideoModel implements VideoModelInterface {

    private BehaviorSubject<List<Video>> videoList = BehaviorSubject.createDefault(new ArrayList<>());

    @Override
    public void put(Video video) {
        List<Video> updatedList = new ArrayList(videoList.getValue());
        updatedList.add(video);
        videoList.onNext(updatedList);
    }

    @Override
    public Observable<List<Video>> getVideoListStream() {
        return videoList.hide();
    }


}
