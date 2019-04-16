package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.Tile;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.TileBitmap;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class TileBitmapLoader {
    private static final String TAG = TileBitmapLoader.class.getSimpleName();
    private final Map<Integer, Bitmap> loadedTileBitmaps = new ConcurrentHashMap<>();
    private final Subject<Object> tilesUpdateSubject = PublishSubject.create();
    private final Resources resources;
    private final List<Integer> resourceImages = Arrays.asList(R.drawable.ic_accessibility_black, R.drawable.ic_airplanemode_active_black, R.drawable.ic_brightness_3_black);

    public TileBitmapLoader(Context context) {
        resources = context.getResources();
    }

    public void load(Collection<Tile> mapTileDrawables) {
        Observable.fromIterable(mapTileDrawables)
                .flatMap(this::loadTileBitmap)
                .map(mapTileBitmap -> {
                    if (mapTileBitmap != null && mapTileBitmap.getBitmap() != null) {
                        loadedTileBitmaps.put(mapTileBitmap.getMapTile().tileHashCode(),
                                mapTileBitmap.getBitmap());
                    }
                    return loadedTileBitmaps;
                })
                .subscribe(tile -> tilesUpdateSubject.onNext(new Object()));
    }

    public Bitmap getBitmap(Tile tile) {
        final int hash = tile.tileHashCode();
        if (loadedTileBitmaps.containsKey(hash)) {
            return loadedTileBitmaps.get(hash);
        }
        return null;
    }

    public Observable<Object> bitmapsLoadedEvent() {
        return tilesUpdateSubject.hide();
    }

    private Observable<TileBitmap> loadTileBitmap(final Tile mapTile) {
        Log.d(TAG, "Loading bitmap for tile " + mapTile.toString());
        try {
            Random random = new Random();
            return Observable.just(new TileBitmap(mapTile,BitmapFactory.decodeResource(resources, resourceImages.get(random.nextInt(resourceImages.size())))));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
}
