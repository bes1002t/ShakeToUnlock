package de.imwatchforum.shaketounlock;

import it.imwatch.SensorService;
import it.imwatch.SensorServiceInstance;
import it.imwatch.SimpleShakeDetector;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class ShakeToUnlockService extends Service {

	private PowerManager pm;
	private PowerManager.WakeLock wl;

	private SimpleShakeDetector mShakeDetector;
	private SensorService mSensors;
	private String TAG = "Shake";
	private int time;


	@Override
	public IBinder onBind(Intent intent) {
		time = intent.getExtras().getInt("time");
		return null;
	}


	@Override
	public void onCreate() {
		mShakeDetector = new SimpleShakeDetector(this, new SimpleShakeDetector.OnShakeListener() {

			@Override
			public void onShakeDetected(int steps) {
				pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

				wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);

				wl.acquire();

				TimerTask task = new TimerTask() {
					public void run() {
						wl.release();
					}
				};

				Timer timer = new Timer();
				timer.schedule(task, time);
			}

		}, SimpleShakeDetector.DEFAULT_UPDATE_INTERVAL);
		
		mSensors = new SensorServiceInstance(100);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();

		mShakeDetector.onPause();
		mSensors.onPause();
	}


	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		mShakeDetector.onResume();
		mSensors.onResume();
	}
}