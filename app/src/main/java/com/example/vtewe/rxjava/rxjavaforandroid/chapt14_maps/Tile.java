package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

public class Tile {

    private final int zoom;
    private int screenPosX;
    private int screenPosY;
    private int width;
    private int height;
    private int indexX;
    private int indexY;

    public Tile(int screenPosX, int screenPosY, int width, int height, int indexX, int indexY, int zoom) {
        this.screenPosX = screenPosX;
        this.screenPosY = screenPosY;
        this.width = width;
        this.height = height;
        this.indexX = indexX;
        this.indexY = indexY;
        this.zoom = zoom;
    }

    public int getScreenPosX() {
        return screenPosX;
    }

    public int getScreenPosY() {
        return screenPosY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int getIndexX() {
        return indexX;
    }

    private int getIndexY() {
        return indexY;
    }

    public int tileHashCode() {
        int hash = zoom;
        hash = 31 * hash + screenPosX;
        hash = 31 * hash + screenPosY;
        return hash;
    }

    static class Builder{
        private final Tile tile;
        private int posY;
        private int posX;

        public Builder(Tile tile){
            this.tile = tile;
        }

        public void screenPosX(int posX){
            this.posX = posX;
        }
        public void screenPosY(int posY){
            this.posY = posY;
        }
        public Tile build(){
            return new Tile(posX,posY,tile.getWidth(),tile.getHeight(),tile.getIndexX(),tile.getIndexY(), tile.zoom);
        }

    }

}

