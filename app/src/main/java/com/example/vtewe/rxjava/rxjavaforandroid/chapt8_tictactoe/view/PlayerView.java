package com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

import com.example.vtewe.rxjava.R;
import com.example.vtewe.rxjava.rxjavaforandroid.chapt8_tictactoe.pojo.GameSymbol;

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
            case EMPTY:
                setImageResource(0);
        }
    }
}
