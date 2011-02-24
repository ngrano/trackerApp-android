package com.trackerapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	private static final String TAG = UpdaterService.class.getSimpleName();
	private Updater updater;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		updater = new Updater();
		
		Log.d(TAG, "onCreate'd");
	}
	
	@Override
	public synchronized void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (!updater.isRunning()) {
		  updater.start();
		}
		
		Log.d(TAG, "onStart'd");
	}

	@Override
	public synchronized void onDestroy() {
		super.onDestroy();
		
		if(updater.isRunning()) {
		  updater.interrupt();
		}
		
		updater = null;
		
		Log.d(TAG, "onDestroy'd");
	}
	
	class Updater extends Thread {
		private boolean isRunning = false;
		// Get this from saved preferences
		static final long DELAY = 2000;
		
		public Updater() {
			// Name for the thread
			super("Updater");
		}
		
		@Override
		public void run() {
			isRunning = true;
			
			while (isRunning) {
				Log.d(TAG, "Updater running");
				try {
					
					// Get data here
					
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					isRunning = false;
				}
			}
		}
		
		public boolean isRunning() {
		return this.isRunning;
		
		}

	}

}
