package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameState;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameSymbol;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GridPosition;

public class GameGridView extends View {
    private static final String TAG = GameGridView.class.getSimpleName();
    private GameState gameState;
//    private FullGameState gameState;
    private int width;
    private int height;
    private final Paint linePaint;
    private final Paint winnerLinePaint;
    private final Paint bitmapPaint;
    private final Bitmap circlePlayerBitmap;
    private final Bitmap crossPlayerBitmap;
    private final Bitmap trianglePlayerBitmap;
    private final Rect bitmapSrcRect;

    public GameGridView(Context context) {
        this(context, null);
    }

    public GameGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(8f);

        winnerLinePaint = new Paint();
        winnerLinePaint.setColor(Color.BLACK);
        winnerLinePaint.setStrokeWidth(30f);

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePlayerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_circle);
        crossPlayerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_cross);
        trianglePlayerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_triangle);
        bitmapSrcRect = new Rect(0, 0, circlePlayerBitmap.getWidth(), circlePlayerBitmap.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = right - left;
        height = bottom - top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        Log.d(TAG, "width: " + width + ", height: " + height);

        final float gridWidth = getGridWidth();
        final float gridHeight = getGridHeight();

        final float tileWidth = width / gridWidth;
        final float tileHeight = height / gridHeight;

        drawSymbols(canvas, gridWidth, gridHeight, tileWidth, tileHeight);
        drawGridLines(canvas, gridWidth, gridHeight, tileWidth, tileHeight);
//        if (gameState.getGameStatus().isEnded()) {
//            drawWinner(canvas, tileWidth, tileHeight,
//                    gameState.getGameStatus().getWinningPositionStart(),
//                    gameState.getGameStatus().getWinningPositionEnd());
//        }
    }

    private void drawSymbols(Canvas canvas,
                             float gridWidth, float gridHeight,
                             float tileWidth, float tileHeight) {
        if (gameState == null) {
            return;
        }


        for (int i = 0; i < gridWidth; i++) {
            for (int n = 0; n < gridHeight; n++) {
                GameSymbol symbol = gameState.getGameGrid().getSymbolAt(i, n);
                RectF dst = new RectF(i * tileWidth, n * tileHeight,
                        (i + 1) * tileWidth, (n + 1) * tileHeight);
                if (symbol == GameSymbol.CIRCLE) {
                    canvas.drawBitmap(
                            circlePlayerBitmap,
                            bitmapSrcRect, dst,
                            bitmapPaint);
                } else if (symbol == GameSymbol.CROSS) {
                    canvas.drawBitmap(
                            crossPlayerBitmap,
                            bitmapSrcRect, dst,
                            bitmapPaint);
                } else if (symbol == GameSymbol.TRIANGLE) {
                canvas.drawBitmap(
                        trianglePlayerBitmap,
                        bitmapSrcRect, dst,
                        bitmapPaint);
            }
            }
        }
    }

    private void drawGridLines(Canvas canvas,
                               float gridWidth, float gridHeight,
                               float tileWidth, float tileHeight) {
        for (int i = 0; i <= gridWidth; i++) {
            Log.d(TAG, "line " + i);
            canvas.drawLine(i * tileWidth, 0, i * tileWidth, height, linePaint);
        }

        for (int n = 0; n <= gridHeight; n++) {
            canvas.drawLine(0, n * tileHeight, width, n * tileHeight, linePaint);
        }
    }

    private void drawWinner(Canvas canvas,
                            float tileWidth, float tileHeight,
                            GridPosition start, GridPosition end) {
        canvas.drawLine(
                start.getX() * tileWidth + tileWidth / 2,
                start.getY() * tileHeight + tileHeight / 2,
                end.getX() * tileWidth + tileWidth / 2,
                end.getY() * tileHeight + tileHeight / 2,
                winnerLinePaint);
    }

//    public void setData(FullGameState gameState) {
    public void setData(GameState gameState) {
        Log.d(TAG, "newData");
        this.gameState = gameState;
        invalidate();
    }

    public int getGridWidth() {
        if(gameState == null)return 3;
        return gameState.getGameGrid().getWidth();
    }

    public int getGridHeight() {
        if(gameState == null)return 3;
        return gameState.getGameGrid().getHeight();
    }
}
