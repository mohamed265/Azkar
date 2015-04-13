package com.mohamed265.azkar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamed265.azkar.controlar.Data_Manipulation;
import com.mohamed265.azkar.controlar.Program_Manipulation;
import com.mohamed265.azkar.controlar.Zekr_Manipulation;
import com.mohamed265.azkar.dataStructure.NotificationObject;
import com.mohamed265.azkar.dataStructure.Statistics;
import com.mohamed265.azkar.model.DataBase;
import com.mohamed265.azkar.model.FirstTimeInit;
import com.mohamed265.azkar.model.onDataChangeListener;

public class MainActivity extends Activity {

	public static final int VERSION = 1;

	public static Context con;
	Activity ac = this;
	final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences settings;
	ArrayList<Statistics> stList;
	ArrayAdapter<Statistics> adapter;
	TextView countZekr, times, zekrtoday;
	ProgressBar pb;
	DataBase db;
	int num;
	ListView lv;
	Menu me;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pb = (ProgressBar) findViewById(R.id.pBarAverage);
		countZekr = (TextView) findViewById(R.id.previewCount_Main);
		times = (TextView) findViewById(R.id.times);
		zekrtoday = (TextView) findViewById(R.id.zekrtoday);
		con = this;
		new FirstTimeInit(this);
		db = new DataBase(this);
		settings = con.getSharedPreferences(PREFS_NAME, 0);
		//db.addToDataBaseStatistics("11/4/2015", 21);
		//db.addToDataBaseStatistics("12/4/2015", 489);

		Display dis = getWindowManager().getDefaultDisplay();
		int width = dis.getWidth();
		int height = dis.getHeight();

		pb.setMax(settings.getInt("MaxAverage", 1000));
		pb.setProgress(db.getAverageStatistics());
		pb.setLayoutParams(new RelativeLayout.LayoutParams(width / 2 + width
				/ 8, height / 2));
		pb.setY(-height / 22);
		pb.setX(-width / 7);
		pb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = "„ Ê”ÿ –ﬂ—ﬂ " + pb.getProgress()
						+ "  ŒÿÏ «·Âœ› «·Õ«·Ï " + pb.getMax();
				Toast.makeText(con, text, Toast.LENGTH_LONG).show();
			}
		});

		countZekr.setText(db.getTodayStatistcs().numberOFZekr + "");
		num = Integer.valueOf((String) countZekr.getText());
		times.setText((num < 11 && num != 0 ? "„—« " : "„—…"));

		new onDataChangeListener() {
			@Override
			public void onChange() {
				countZekr.setText(db.getTodayStatistcs().numberOFZekr + "");
				num = Integer.valueOf((String) countZekr.getText());
				times.setText((num < 11 && num != 0 ? "„—« " : "„—…"));
				pb.setProgress(db.getAverageStatistics());

				if (pb.getProgress() >= pb.getMax()) {
					settings.edit()
							.putInt("MaxAverage", (int) (pb.getMax() * 1.2))
							.commit();
					sendNotification((int) (pb.getMax() * 1.2),
							" ·œÌﬂ Âœ› ÃœÌœ œ⁄ „ Ê”ÿ –ﬂ—ﬂ » ŒÿÏ ",
							"·ﬁœ Õÿ„  «·„ Ê”ÿ");
					pb.setMax((int) (pb.getMax() * 1.2));
				}
			}
		};

		zekrtoday.setLayoutParams(new RelativeLayout.LayoutParams(
				(width / 3) * 2, (height) / 6));
		zekrtoday.setX(width / 6 + width / 15);
		zekrtoday.setY(0);

		countZekr.setLayoutParams(new RelativeLayout.LayoutParams(width / 3,
				width / 8));
		countZekr.setX(width - width / 3);
		countZekr.setY(height / 6);

		times.setLayoutParams(new RelativeLayout.LayoutParams((width / 3),
				(height) / 12));
		times.setX(width / 5);
		times.setY(height / 6);

		lv = (ListView) findViewById(R.id.dailyCount);
		lv.setY((float) (height / 4.7));

		stList = (ArrayList<Statistics>) db
				.getAllStatistcs(DataBase.WITH_OUT_TODAY);
		Collections.reverse(stList);
		if (stList.size() > 2) {
			stList.add(stList.get(1));
			stList.add(stList.get(0));
		}
		adapter = new ArrayAdapter<Statistics>(this,
				android.R.layout.simple_list_item_1, stList);
		lv.setAdapter(adapter);
		
		List<NotificationObject> arr = db.getAllNotificationObject();
		
		for (int i = 0; i < arr.size(); i++) {
			Log.d("noti", arr.get(i).progName + " " + arr.get(i).zekr.ID);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem mi = menu.add("Œ—ÊÃ");

		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				finish();
				return true;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addZekr: {
			FragmentManager fm = getFragmentManager();
			Zekr_Manipulation object = new Zekr_Manipulation();
			object.show(fm, " ");
		}
			return true;
		case R.id.previewZekr: {
			Intent in = new Intent(this, Data_Manipulation.class);
			in.putExtra("type", false);
			startActivity(in);
		}
			return true;
		case R.id.previewprogram: {
			Intent in = new Intent(this, Data_Manipulation.class);
			in.putExtra("type", true);
			startActivity(in);
		}
			return true;
		case R.id.addprogram: {
			Intent intent = new Intent(this, Program_Manipulation.class);
			startActivity(intent);
		}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static void sendNotification(int NO, String mes, String title) {

		final NotificationManager mgr = (NotificationManager) con
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification note = new Notification(R.drawable.notificationicon,
				"√–ﬂ«—", System.currentTimeMillis());

		note.setLatestEventInfo(con, title, mes + NO, null);
		note.flags |= Notification.FLAG_AUTO_CANCEL;

		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(
					con.getApplicationContext(), notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}

		mgr.notify(1000000, note);
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_MENU:
			// Toast.makeText(con, "msh 3arf", Toast.LENGTH_LONG).show();
			openOptionsMenu();
			return true;
		}

		return super.onKeyDown(keycode, e);
	}
}