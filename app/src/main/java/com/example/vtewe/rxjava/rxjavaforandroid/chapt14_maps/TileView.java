package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;

public class TileView extends View {

    private Collection<Tile> tiles;
    private Paint tilePaint;

    public TileView(Context context) {
        super(context);
        init();
    }

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        tilePaint = new Paint();
        tilePaint.setColor(Color.BLACK);
        tilePaint.setStyle(Paint.Style.STROKE);
        tilePaint.setStrokeWidth(2);
        tilePaint.setTextSize(70);
    }

    public void setTiles(Collection<Tile> tiles){
        this.tiles = tiles;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        if (tiles != null) {
            for (Tile tile : tiles) {
//                Bitmap bitmap = null;
//                if (tileBitmapLoader != null) {
//                    bitmap = tileBitmapLoader.getBitmap(tile);
//                }
                RectF dst = new RectF(
                        (float) tile.getScreenPosX(),
                        (float) tile.getScreenPosY(),
                        (float) (tile.getScreenPosX() + tile.getWidth()),
                        (float) (tile.getScreenPosY() + tile.getHeight()));
                canvas.drawRect(dst, tilePaint);
                canvas.drawText(tile.getIndexX() + ", " + tile.getIndexY(), tile.getScreenPosX() + tile.getWidth()/2, tile.getScreenPosY() + tile.getHeight()/2,tilePaint);
//                if (bitmap != null) {
//                    canvas.drawBitmap(bitmap, null, dst, bitmapPaint);
//                }
//                canvas.drawRect(dst, tilePaint);
            }
        }
    }
}
