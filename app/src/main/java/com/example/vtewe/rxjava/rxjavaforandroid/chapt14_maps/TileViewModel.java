package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TileViewModel {

    private static final String TAG = TileViewModel.class.getSimpleName();
    private int tileSize = 256;
    //dragging subject
    private BehaviorSubject<PointD> mapOffset = BehaviorSubject.createDefault(new PointD(0f,0f));


    public TileViewModel(Observable<PointD> xyMovementEvents) {
        calculateUpdates(mapOffset, xyMovementEvents)
                .subscribe(mapOffset::onNext);
    }

    public Observable<Collection<Tile>> getTiles(Observable<Integer> zoomLevel, Observable<PointD> tilesViewSize) {
        return Observable.combineLatest(
                    zoomLevel
                        .withLatestFrom(tilesViewSize, (zoom, viewSize) -> calculateTiles(zoom, viewSize)),
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

    private Collection<Tile> calculateTiles(int zoomLevel, PointD viewSize){
        PointD offset = mapOffset.getValue();

        final int firstTileX = (int) Math.floor(-offset.x / tileSize);
        final int firstTileY = (int) Math.floor(-offset.y / tileSize);
        final int lastTileX = (int) Math.ceil((-offset.x + viewSize.x) / tileSize);
        final int lastTileY = (int) Math.ceil((-offset.y + viewSize.y) / tileSize);

        final int left = Math.max(0, firstTileX);
        final int right = Math.min(1 << zoomLevel, lastTileX);
        final int top = Math.max(0, firstTileY);
        final int bottom = Math.min(1 << zoomLevel, lastTileY);


        Collection<Tile> tiles = new ArrayList<>();
        int xPos = 0, yPos;
//        int gridSize = (int) Math.pow(2,zoomLevel);

        for(int i = left; i < right; i++){
            tiles.add(new Tile(xPos,0,tileSize,tileSize,i,0, zoomLevel));
            yPos = tileSize;
            for(int j = top+1; j < bottom; j++){
                tiles.add(new Tile(xPos,yPos,tileSize,tileSize,i,j, zoomLevel));
                yPos+=tileSize;
            }
            xPos+=tileSize;
        }
//        for(int i = 0; i < gridSize; i++){
//            tiles.add(new Tile(xPos,0,tileSize,tileSize,i,0, zoomLevel));
//            yPos = tileSize;
//            for(int j = 1; j < gridSize; j++){
//                tiles.add(new Tile(xPos,yPos,tileSize,tileSize,i,j, zoomLevel));
//                yPos+=tileSize;
//            }
//            xPos+=tileSize;
//        }
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
