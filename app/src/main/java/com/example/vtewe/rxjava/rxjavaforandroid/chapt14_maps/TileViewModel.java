package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TileViewModel {

    private static final String TAG = TileViewModel.class.getSimpleName();
//    int size = 300;
//    private Collection<Tile> TEST_TILES = Arrays.asList(
//            new Tile(0,0,size,size,0,0),
//            new Tile(size+1,0,size,size,1,0),
//            new Tile(0,size+1,size,size,0,1),
//            new Tile(size+1,size+1,size,size,1,1));

//    private BehaviorSubject<Collection<Tile>> tilesSubject = BehaviorSubject.createDefault(TEST_TILES);
    private BehaviorSubject<Collection<Tile>> tilesSubject = BehaviorSubject.createDefault(calculateTiles(1));

    public Observable<Collection<Tile>> getTiles(){
        return tilesSubject.hide();
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
                yPos+=gridSize;
            }
            xPos+=tileSize;
        }

        return tiles;
    }

}
