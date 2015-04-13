package com.mohamed265.azkar.NOTUSED;
//package xxxxxx;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//
//import org.json.JSONException;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.example.azkar.R;
//
//public class azkarDaily extends Service {
//
//	static int os = 0;
//	boolean flagRunning = true;
//	ArrayList<Thread> threadList;
//	ArrayList<ZekrConfigProgrsm> ZekrConfigProgrsmList;
//	Context con = this;
//	DataBaseHandler db;
//	Random rand;
//
//	@Override
//	public void onDestroy() {
//		Log.d("tag", "destroy");
//		flagRunning = false;
//		super.onDestroy();
//	}
//
//	@Override
//	public void onCreate() {
//		Log.d("tag", "on Create");
//		super.onCreate();
//		db = new DataBaseHandler(this);
//		threadList = new ArrayList<Thread>();
//		ZekrConfigProgrsmList = new ArrayList<ZekrConfigProgrsm>();
//		rand = new Random();
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		super.onStartCommand(intent, flags, startId);
//
//		for (int i = 0; i < threadList.size(); i++) {
//			threadList.get(i).interrupt();
//		}
//
//		threadList = new ArrayList<Thread>();
//		// ********************
//		Log.d("tag", "on start command ");
//
//		List<ZekrConfigProgrsm> list = db.getAllPrograms();
//		for (int i = 0; i < list.size(); i++) {
//			Inti(list.get(i));
//		}
//
//		return START_STICKY;
//	}
//
//	void Inti(final ZekrConfigProgrsm zcp1) {
//
//		Thread test = new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				ZekrConfigProgrsm zcp = zcp1;
//
//				devideZekrs(zcp);
//
//				zcp.calculateWaitTime();
//
//				HashSet<Integer> set = new HashSet<Integer>();
//
//				long sleep;
//				int pro, choies;
//
//				do {
//
//					try {
//						sleep = calcWait(zcp);
//						if (sleep != zcp.waitTimeBetween)
//							set.clear();
//						Thread.sleep(sleep);
//					} catch (Exception e) {
//					}
//
//					pro = 0;
//
//					do {
//						pro++;
//						choies = rand.nextInt(zcp.azkar.size());
//						if (pro > zcp.azkar.size())
//							break;
//					} while (set.contains(choies));
//					set.add(choies);
//
//					db.addCount(zcp.azkar.get(choies).repetedTimes);
//					sendNotification(zcp.azkar.get(choies), zcp.programName);
//
//				} while (flagRunning);
//			}
//		});
//
//		test.start();
//		threadList.add(test);
//
//	}
//
//	void devideZekrs(ZekrConfigProgrsm zcp) {
//		for (int i = 0; i < zcp.azkar.size(); i++) {
//			if (zcp.azkar.get(i).repetedTimes > 5) {
//				int times = (zcp.azkar.get(i).repetedTimes) / 5;
//
//				if (zcp.azkar.get(i).repetedTimes % 5 != 0) {
//
//					zcp.azkar.get(i).repetedTimes = zcp.azkar.get(i).repetedTimes % 5;
//
//					for (int j = 0; j < times; j++) {
//						zcp.azkar.add(new Zekr(zcp.azkar.get(i).zekrContent,
//								zcp.azkar.get(i).zekrDescription, 5));
//						// , zcp.azkar.get(i).ID)
//
//					}
//				} else {
//					zcp.azkar.get(i).repetedTimes = 5;
//					for (int j = 0; j < times - 1; j++)
//						zcp.azkar.add(new Zekr(zcp.azkar.get(i).zekrContent,
//								zcp.azkar.get(i).zekrDescription, 5// ,
//																	// zcp.azkar.get(i).ID)
//								));
//				}
//			}
//		}
//	}
//
//	private long calcTimeNow() {
//		long millis, tnow;
//		millis = System.currentTimeMillis();
//		tnow = (millis / 1000) % 60;
//		tnow += (((millis / (1000 * 60)) % 60)) * 60;
//		tnow += (((millis / (1000 * 60 * 60)) % 24) + 2) * 3600;
//		return tnow;
//	}
//
//	private long calcWait(ZekrConfigProgrsm zcp) {
//		long tnow = calcTimeNow();
//
//		if (tnow >= zcp.endProgram)
//			return (ZekrConfigProgrsm.day - tnow + zcp.startProgram) * 1000;
//		else if (tnow < zcp.startProgram)
//			return (zcp.startProgram - tnow) * 1000;
//		else
//			return zcp.waitTimeBetween * 1000;
//
//	}
//
//	@SuppressWarnings("deprecation")
//	private void sendNotification(Zekr zker, String zkerMessage) {
//
//		final NotificationManager mgr = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		@SuppressWarnings("deprecation")
//		Notification note = new Notification(R.drawable.notificationicon,
//				"ระ฿วั", System.currentTimeMillis());
//
//		Intent intt1 = new Intent(this, PreviewZekr.class);
//
//		intt1.putExtra("des", zker.toJson());
//
//		PendingIntent i = PendingIntent.getActivity(this, 0, intt1, 268435456);
//
//		note.setLatestEventInfo(this, zkerMessage, zker.toView(), i);
//		note.flags |= Notification.FLAG_AUTO_CANCEL;
//
//		try {
//			Uri notification = RingtoneManager
//					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
//					notification);
//			r.play();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		os++;
//		mgr.notify(os, note);
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//}
