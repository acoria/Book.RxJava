package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TileViewModel {

    private static final String TAG = TileViewModel.class.getSimpleName();
    //dragging subject
    private BehaviorSubject<PointD> mapOffset = BehaviorSubject.createDefault(new PointD(0f,0f));
    private BehaviorSubject<Integer> zoomLevel = BehaviorSubject.createDefault(2);


    public TileViewModel(Observable<PointD> xyMovementEvents) {
        calculateUpdates(mapOffset, xyMovementEvents)
                .subscribe(mapOffset::onNext);
    }

    public Observable<Collection<Tile>> getTiles() {
        return Observable.combineLatest(
                    zoomLevel.map(this::calculateTiles),
                    mapOffset,
                    Pair::new)
                .flatMap(pair ->
                        Observable.fromIterable(pair.first)
                            .map(tile -> this.offsetTile(tile, pair.second))
                            .toList()
                            .toObservable());
    }

    private Tile offsetTile(
            Tile tile, PointD offset) {
            Tile.Builder builder = new Tile.Builder(tile);
            builder.screenPosX((int)(tile.getScreenPosX() + offset.x));
            builder.screenPosY((int)(tile.getScreenPosY() + offset.y));
            return builder.build();
    }

    private Collection<Tile> calculateTiles(int zoomLevel){
                Collection<Tile> tiles = new ArrayList<>();
        int tileSize = 256;
        int xPos = 0, yPos;
        int gridSize = (int) Math.pow(2,zoomLevel);

        for(int i = 0; i < gridSize; i++){
            tiles.add(new Tile(xPos,0,tileSize,tileSize,i,0));
            yPos = tileSize;
            for(int j = 1; j < gridSize; j++){
                tiles.add(new Tile(xPos,yPos,tileSize,tileSize,i,j));
                yPos+=tileSize;
            }
            xPos+=tileSize;
        }

        return tiles;
    }

    private Observable<PointD> calculateUpdates(Observable<PointD> mapOffset, Observable<PointD> xyMovementEvents) {
        return xyMovementEvents
                .withLatestFrom(mapOffset, Pair::new)
                .map(pair ->
                    new PointD(
                        pair.first.x + pair.second.x,
                        pair.first.y + pair.second.y)
                );
    }

}
