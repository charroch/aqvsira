package com.novoda.aqvsira;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class Bomb extends Activity {

    private BombTimer bombTimer;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bomb);
        TextView timerView = (TextView) findViewById(R.id.bomb_timer);
        bombTimer = new BombTimer(timerView);
    }
    
    public void starCountdown(View v){
    	bombTimer.start();
    }

    private static class BombTimer extends CountDownTimer {
        private static final long TOTAL_TIME = 10000;
		private static final long INTERVAL = 950;
		private final TextView timerView;

		public BombTimer(TextView timerView) {
            super(TOTAL_TIME, INTERVAL);
            this.timerView = timerView;
        }

        @Override
        public void onFinish() {
            timerView.setText("Boom!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long secsLeft = ( millisUntilFinished + 100 ) / 1000;
            String string = "00:";
            if(secsLeft >= 10){
            	string += secsLeft;
            }else{
            	string += "0" + secsLeft;
            }
			timerView.setText(string);
        }
    }
}
