package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameSymbol;

public class PlayerView extends AppCompatImageView {

    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(GameSymbol gameSymbol){
        switch(gameSymbol){
            case BLACK:
                setImageResource(R.drawable.symbol_black_circle);
                break;
            case RED:
                setImageResource(R.drawable.symbol_red_circle);
                break;
            case TRIANGLE:
                setImageResource(R.drawable.symbol_triangle);
                break;
            case EMPTY:
                setImageResource(0);
        }
    }
}
