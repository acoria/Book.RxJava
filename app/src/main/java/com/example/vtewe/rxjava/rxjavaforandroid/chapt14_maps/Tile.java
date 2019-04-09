package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

public class Tile {

    private int screenPosX;
    private int screenPosY;
    private int width;
    private int height;
    private int indexX;
    private int indexY;

    public Tile(int screenPosX, int screenPosY, int width, int height, int indexX, int indexY) {
        this.screenPosX = screenPosX;
        this.screenPosY = screenPosY;
        this.width = width;
        this.height = height;
        this.indexX = indexX;
        this.indexY = indexY;
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

    public int getIndexX() {
        return indexX;
    }

    public int getIndexY() {
        return indexY;
    }
}
