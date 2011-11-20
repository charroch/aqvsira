package com.novoda.aqvsira.application;

import java.io.IOException;

import android.app.Application;
import android.util.Log;

import com.novoda.aqvsira.server.AbstractServerListener;
import com.novoda.aqvsira.server.Client;
import com.novoda.aqvsira.server.Server;

public class Aqvsira extends Application {

	private static Server server;

	@Override
	public void onCreate() {
		super.onCreate();
		if (server == null) {
			setup();
		}
	}

	private static void setup() {
		server = null;
		try {
			server = new Server(4567);
			server.start();
		} catch (IOException e) {
			Log.v("XXX", "IOException" + e.toString());
			Log.e("XXX", "IOException", e);
		}
		server.addListener(new AbstractServerListener() {
			@Override
			public void onReceive(Client client, byte[] data) {
				Log.v("XXX", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				Log.v("XXX", "onReceive");
				Log.v("XXX", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

				
			};
		});
	}

}
