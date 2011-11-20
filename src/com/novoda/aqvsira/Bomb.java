package com.novoda.aqvsira;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Bomb extends Activity {

    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bomb);
//        tv = new TextView(this);
//        tv.setTextSize(128.0f);
//        this.setContentView(tv);

//        CD counter = new CD(10000, 1000);
//        counter.start();
    }

    
    
    public class CD extends CountDownTimer {
        public CD(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv.setText("Boom!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv.setText("" + millisUntilFinished / 1000);
        }
    }
}
