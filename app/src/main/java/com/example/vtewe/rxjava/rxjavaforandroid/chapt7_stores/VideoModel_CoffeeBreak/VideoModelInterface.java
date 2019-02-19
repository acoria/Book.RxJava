package com.example.vtewe.rxjava.rxjavaforandroid.chapt7_stores.VideoModel_CoffeeBreak;

import java.util.List;

import io.reactivex.Observable;

public interface VideoModelInterface {
    void put(Video video);
    Observable<List<Video>> getVideoListStream();
}
