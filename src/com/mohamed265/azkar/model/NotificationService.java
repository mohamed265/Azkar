package com.mohamed265.azkar.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.mohamed265.azkar.PreviewZekr;
import com.mohamed265.azkar.R;
import com.mohamed265.azkar.dataStructure.NotificationObject;
import com.mohamed265.azkar.dataStructure.ZekrConfigProgram;

public class NotificationService extends Service {

	DataBase db;

	@Override
	public void onCreate() {
		// Log.d("tag", "on Create");
		super.onCreate();
		db = new DataBase(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		// Log.d("tag", "on start command ");

		new Thread(new Runnable() {
			@Override
			public void run() {
				long now, wait;
				NotificationObject NO;
				do {
					now = calcTimeNow();
					NO = db.getNextNotificationObject(now);
					wait = calcWaitTime(NO, now);
					Log.d("Wait", wait + " ");
					try {
						Thread.sleep(wait * 1000);
					} catch (Exception e) {
					}
					sendNotification(NO);
					db.addCount(NO.zekr.toViewRepet());
				} while (true);
			}
		}).start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Log.d("tag", "destroy");
		super.onDestroy();
	}

	private long calcTimeNow() {
		long millis, tnow;
		millis = System.currentTimeMillis();
		tnow = (millis / 1000) % 60;
		tnow += (((millis / (1000 * 60)) % 60)) * 60;
		tnow += (((millis / (1000 * 60 * 60)) % 24) + 2) * 3600;
		return tnow;
	}

	private long calcWaitTime(NotificationObject NO, long now) {
		if (NO.time >= now)
			return NO.time - now;
		else
			return (ZekrConfigProgram.day - now) + NO.time;
	}

	private void sendNotification(NotificationObject NO) {

		final NotificationManager mgr = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification note = new Notification(R.drawable.notificationicon,
				"ระ฿วั", System.currentTimeMillis());

		Intent intt = new Intent(this, PreviewZekr.class);

		intt.putExtra("des", NO.zekr.toJson());

		PendingIntent intent = PendingIntent.getActivity(this, 0, intt,
				268435456);

		note.setLatestEventInfo(this, NO.progName,
				NO.zekr.toViewNotification(), intent);
		note.flags |= Notification.FLAG_AUTO_CANCEL;

		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mgr.notify(NO.ID, note);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
// Timer timing = new Timer();
// timing.schedule(new TimerTask() {
// @Override
// public void run() {
//
// }
// }, 10000);