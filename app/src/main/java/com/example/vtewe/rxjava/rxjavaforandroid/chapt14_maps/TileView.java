package com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt14_maps.utils.TileBitmapLoader;

import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

public class TileView extends View {

    private Collection<Tile> tiles;
    private Paint tilePaint;
    private BehaviorSubject<PointD> tileViewSize = BehaviorSubject.create();
    private TileBitmapLoader tileBitmapLoader;
    private Paint bitmapPaint;

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
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setTiles(Collection<Tile> tiles){
        this.tiles = tiles;
        if (tileBitmapLoader != null) {
            tileBitmapLoader.load(tiles);
        }
        invalidate();
    }

    public Observable<PointD> getViewSize(){
        return tileViewSize.hide();
    }

    public void setTileBitmapLoader(TileBitmapLoader tileBitmapLoader) {
        this.tileBitmapLoader = tileBitmapLoader;
        this.tileBitmapLoader.bitmapsLoadedEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> invalidate());
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        draw rectangles
//        canvas.drawColor(Color.GRAY);
//        if (tiles != null) {
//            for (Tile tile : tiles) {
//                RectF dst = new RectF(
//                        (float) tile.getScreenPosX(),
//                        (float) tile.getScreenPosY(),
//                        (float) (tile.getScreenPosX() + tile.getWidth()),
//                        (float) (tile.getScreenPosY() + tile.getHeight()));
//                canvas.drawRect(dst, tilePaint);
//                canvas.drawText(tile.getIndexX() + ", " + tile.getIndexY(), tile.getScreenPosX() + tile.getWidth()/2, tile.getScreenPosY() + tile.getHeight()/2,tilePaint);
//            }
//        }

        //add bitmaps
        if (tiles != null) {
            for (Tile tile : tiles) {
                Bitmap bitmap = null;
                if (tileBitmapLoader != null) {
                    bitmap = tileBitmapLoader.getBitmap(tile);
                }
                RectF dst = new RectF(
                        (float) tile.getScreenPosX(), (float) tile.getScreenPosY(),
                        (float) (tile.getScreenPosX() + tile.getWidth()),
                        (float) (tile.getScreenPosY() + tile.getHeight()));
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, null, dst, bitmapPaint);
                }
                canvas.drawRect(dst, tilePaint);
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        tileViewSize.onNext(new PointD(width,height));
    }
}
