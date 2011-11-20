package com.novoda.aqvsira;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
		bombTimer = new BombTimer(timerView, this, getIntent().getStringExtra("id"));
	}

	public void starCountdown(View v) {
		bombTimer.start();
	}

	private static class BombTimer extends CountDownTimer {
		private static final long TOTAL_TIME = 10000;
		private static final long INTERVAL = 950;
		private final TextView timerView;
		private Context context;
		private String id;

		public BombTimer(TextView timerView, Context context, String id) {
			super(TOTAL_TIME, INTERVAL);
			this.timerView = timerView;
			this.context = context;
			this.id = id;
		}

		@Override
		public void onFinish() {
			timerView.setText("Boom!");
			Intent in = new Intent(context, RIP.class);
			in.putExtra("id", id);
			context.startActivity(in);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long secsLeft = (millisUntilFinished + 100) / 1000;
			String string = "00:";
			if (secsLeft >= 10) {
				string += secsLeft;
			} else {
				string += "0" + secsLeft;
			}
			timerView.setText(string);
		}
	}
}
