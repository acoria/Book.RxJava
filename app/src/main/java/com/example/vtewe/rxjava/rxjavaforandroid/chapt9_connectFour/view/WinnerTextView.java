package com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.vtewe.rxjava.rxjavaforandroid.chapt9_connectFour.pojo.GameStatus;

public class WinnerTextView extends AppCompatTextView {

    public WinnerTextView(Context context) {
        super(context);
    }

    public WinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(GameStatus gameStatus){
        if(gameStatus.isEnded()){
            setText("Winner: " + gameStatus.getWinner());
        }else{
            setText("");
        }
    }
}
