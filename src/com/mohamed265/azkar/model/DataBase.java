package com.mohamed265.azkar.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mohamed265.azkar.dataStructure.NotificationObject;
import com.mohamed265.azkar.dataStructure.Statistics;
import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.dataStructure.ZekrConfigProgram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "AZKAR";

	private static final String TABLE_AZKAR = "azkar";
	private static final String TABLE_STATISTICS = "statistics";
	private static final String TABLE_ZEKRCONFIG = "ZekrConfig";
	private static final String TABLE_NOTIFICATION = "notification";

	public static final String KEY_ID = "ID";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_DESCRIPTION = "descreption";
	public static final String KEY_REPETEDTIMES = "repetedTime";

	public static final String KEY_DATE = "date";
	public static final String KEY_COUNT = "count";

	public static final String KEY_PROGNAME = "progName";
	public static final String KEY_PROGRAMS = "programes";
	public static final String KEY_START_TIME = "startTime";
	public static final String KEY_END_TIME = "endTime";

	public static final String KEY_AZKAR_ID = "ZekrID";
	public static final String KEY_CONFIG_ID = "configID";
	public static final String KEY_TIME = "time";

	public static final boolean WITH_OUT_TODAY = false;
	public static final boolean WITH_TODAY = true;

	public static ArrayList<onDataChangeListener> list;

	static {
		list = new ArrayList<onDataChangeListener>();
	}

	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_STATISTICS_TABLE = "CREATE TABLE " + TABLE_STATISTICS
				+ "( " + KEY_DATE + " TEXT PRIMARY KEY , " + KEY_COUNT
				+ " INTEGER ) ";
		db.execSQL(CREATE_STATISTICS_TABLE);

		String CREATE_AZKAR_TABLE = "CREATE TABLE " + TABLE_AZKAR + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
				+ KEY_CONTENT + " TEXT ," + KEY_DESCRIPTION + " TEXT ,"
				+ KEY_REPETEDTIMES + " INTEGER )";
		db.execSQL(CREATE_AZKAR_TABLE);

		String CREATE_ZEKRPROG_TABLE = "CREATE TABLE " + TABLE_ZEKRCONFIG
				+ "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
				+ KEY_START_TIME + " INTEGER, " + KEY_END_TIME + " INTEGER, "
				+ KEY_PROGNAME + " TEXT )";
		db.execSQL(CREATE_ZEKRPROG_TABLE);

		String CREATE_ZEKRPROG_NOTIFICATION = "CREATE TABLE "
				+ TABLE_NOTIFICATION + "( " + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_TIME
				+ " INTEGER , " + KEY_CONFIG_ID + " INTEGER , " + KEY_AZKAR_ID
				+ " INTEGER" + " , " + " FOREIGN KEY(" + KEY_AZKAR_ID
				+ ") REFERENCES " + TABLE_AZKAR + "(" + KEY_ID
				+ ") ON DELETE CASCADE ," + " FOREIGN KEY(" + KEY_CONFIG_ID
				+ ") REFERENCES " + TABLE_ZEKRCONFIG + "(" + KEY_ID
				+ ") ON DELETE CASCADE) ";
		db.execSQL(CREATE_ZEKRPROG_NOTIFICATION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private void notifyDataChange() {

		do {

		} while (list.remove(null));

		for (int i = 0; i < list.size(); i++)
			list.get(i).onChange();
	}

	// Azkar Table
	public long addZekr(Zekr zker) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT, zker.zekrContent);
		values.put(KEY_REPETEDTIMES, String.valueOf(zker.repetedTimes));
		values.put(KEY_DESCRIPTION, zker.zekrDescription);

		long rs = db.insert(TABLE_AZKAR, null, values);
		db.close();
		notifyDataChange();
		return rs;
	}

	private Zekr getZekr(int id, SQLiteDatabase db) {

		if (!db.isOpen())
			db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_AZKAR, new String[] { KEY_CONTENT,
				KEY_DESCRIPTION, KEY_REPETEDTIMES }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		String content = cursor.getString(0);
		String des = cursor.getString(1);
		int rep = Integer.parseInt(cursor.getString(2));

		cursor.close();

		return new Zekr(content, des, rep, id);
	}

	public void updateZekr(Zekr zekr) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CONTENT, zekr.zekrContent + "");
		values.put(KEY_DESCRIPTION, zekr.zekrDescription + "");
		values.put(KEY_REPETEDTIMES, String.valueOf(zekr.repetedTimes));

		db.update(TABLE_AZKAR, values, KEY_ID + " =  " + zekr.ID, null);

		notifyDataChange();
	}

	public void deleteZekr(Zekr zekr) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_AZKAR, KEY_ID + " = ?",
				new String[] { String.valueOf(zekr.ID) });
		deleteNotificatinByZekrID(zekr.ID);
		db.close();
		notifyDataChange();
	}

	public List<Zekr> getAllZekrs() {
		List<Zekr> akzarList = new ArrayList<Zekr>();
		String selectQuery = "SELECT " + KEY_CONTENT + " , " + KEY_DESCRIPTION
				+ " , " + KEY_REPETEDTIMES + " , " + KEY_ID + " FROM "
				+ TABLE_AZKAR;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Zekr zekr = new Zekr(cursor.getString(0), cursor.getString(1),
						cursor.getInt(2), cursor.getInt(3));
				akzarList.add(zekr);
			} while (cursor.moveToNext());
		}
		db.close();
		cursor.close();

		return akzarList;
	}

	// Program Table
	public void addProgram(ZekrConfigProgram zcp) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues progValues = new ContentValues();
		progValues.put(KEY_PROGNAME, zcp.programName);
		progValues.put(KEY_START_TIME, String.valueOf(zcp.startProgram));
		progValues.put(KEY_END_TIME, String.valueOf(zcp.endProgram));
		zcp.ID = (int) db.insert(TABLE_ZEKRCONFIG, null, progValues);

		addToNotificationTable(zcp, db);

		db.close();
		notifyDataChange();
	}

	public ZekrConfigProgram getProgram(int id) {
		// Log.d("tagGETProg", id + " ");
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursorConfig = db.query(TABLE_ZEKRCONFIG, new String[] {
				KEY_PROGNAME, KEY_START_TIME, KEY_END_TIME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursorConfig != null)
			cursorConfig.moveToFirst();

		ZekrConfigProgram zcp = new ZekrConfigProgram();

		zcp.programName = cursorConfig.getString(0);
		zcp.startProgram = cursorConfig.getLong(1);
		zcp.endProgram = cursorConfig.getLong(2);
		zcp.azkar = getAzkarFromNotificationTable(id, db);

		db.close();
		cursorConfig.close();

		return zcp;
	}

	public void updateProgram(ZekrConfigProgram zcp) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROGNAME, zcp.programName);
		values.put(KEY_START_TIME, String.valueOf(zcp.startProgram));
		values.put(KEY_END_TIME, String.valueOf(zcp.endProgram));
		db.update(TABLE_ZEKRCONFIG, values, KEY_ID + " = " + zcp.ID, null);

		db.delete(TABLE_NOTIFICATION, KEY_CONFIG_ID + " = " + zcp.ID, null);
		addToNotificationTable(zcp, db);
		db.close();
		notifyDataChange();
	}

	public void deleteProgram(ZekrConfigProgram zcp) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ZEKRCONFIG, KEY_ID + " = ?",
				new String[] { String.valueOf(zcp.ID) });
		deleteNotificatinByConfigID(zcp.ID);
		db.close();
		notifyDataChange();
	}

	public List<ZekrConfigProgram> getAllPrograms() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<ZekrConfigProgram> list = new ArrayList<ZekrConfigProgram>();

		String selectQuery = "SELECT " + KEY_START_TIME + " , " + KEY_END_TIME
				+ " , " + KEY_PROGNAME + " , " + KEY_ID + " FROM "
				+ TABLE_ZEKRCONFIG;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				ZekrConfigProgram zcp = new ZekrConfigProgram();
				zcp.startProgram = cursor.getInt(0);
				zcp.endProgram = cursor.getInt(1);
				zcp.programName = cursor.getString(2);
				zcp.ID = cursor.getInt(3);
				zcp.azkar = getAzkarFromNotificationTable(zcp.ID, db);
				list.add(zcp);
			} while (cursor.moveToNext());
		}

		db.close();
		return list;
	}

	// Notification Table

	public NotificationObject getNextNotificationObject(long now) {
		SQLiteDatabase db = this.getReadableDatabase();
		NotificationObject NO = new NotificationObject(), firstNO = new NotificationObject();
		int IDZekr = 0, fIDZekr = 0, IDConfig = 0, fIDConfig = 0, ID = 0, fID = 0;
		String selectQuery = "SELECT " + KEY_AZKAR_ID + " , " + KEY_CONFIG_ID
				+ " , " + KEY_TIME + " , " + KEY_ID + " FROM "
				+ TABLE_NOTIFICATION + " ORDER BY " + KEY_TIME;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {

			fIDZekr = cursor.getInt(0);
			fIDConfig = cursor.getInt(1);
			firstNO.time = cursor.getInt(2);
			fID = cursor.getInt(3);

			do {

				IDZekr = cursor.getInt(0);
				IDConfig = cursor.getInt(1);
				NO.time = cursor.getInt(2);
				ID = cursor.getInt(3);

				if (now < NO.time)
					break;

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		if (now <= NO.time) {
			NO.zekr = getZekr(IDZekr, db);
			NO.progName = getProgram(IDConfig).programName;
			NO.ID = ID;
			// db.close();
			return NO;
		} else {
			firstNO.zekr = getZekr(fIDZekr, db);
			firstNO.progName = getProgram(fIDConfig).programName;
			firstNO.ID = fID;
			// db.close();
			return firstNO;
		}
	}

	public List<NotificationObject> getAllNotificationObject() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT " + KEY_AZKAR_ID + " , " + KEY_CONFIG_ID
				+ " , " + KEY_TIME + " , " + KEY_ID + " FROM "
				+ TABLE_NOTIFICATION + " ORDER BY " + KEY_TIME;

		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<NotificationObject> listNO = new ArrayList<NotificationObject>();

		if (cursor.moveToFirst()) {
			do {

				NotificationObject NO = new NotificationObject();
				NO.zekr = getZekr(cursor.getInt(0), db);
				NO.time = cursor.getInt(2);
				NO.ID = cursor.getInt(3);
				listNO.add(NO);
				Log.d("dataBase", "zekr ID " + cursor.getInt(0) + " config ID "
						+ cursor.getInt(1) + " time " + cursor.getInt(2)
						+ " ID " + cursor.getInt(3));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return listNO;
	}

	private ArrayList<Zekr> getAzkarFromNotificationTable(int id,
			SQLiteDatabase db) {
		ArrayList<Zekr> list = new ArrayList<Zekr>();
		String selectQuery = "SELECT " + KEY_AZKAR_ID + " FROM "
				+ TABLE_NOTIFICATION + " WHERE " + KEY_CONFIG_ID + " = " + id;

		int ID_DataBase;
		boolean dontRepet = true;
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {

				dontRepet = true;
				ID_DataBase = cursor.getInt(0);

				for (int i = 0; i < list.size() && dontRepet; i++)
					if (list.get(i).ID == ID_DataBase)
						dontRepet = false;

				if (dontRepet)
					list.add(getZekr(ID_DataBase, db));

			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}

	private void addToNotificationTable(ZekrConfigProgram zcp, SQLiteDatabase db) {
		long waitTime = zcp.calculateWaitTime(), time = zcp.startProgram, temp;
		ContentValues values = new ContentValues();

		for (int i = 0; i < zcp.azkar.size(); i++) {

			values.put(KEY_AZKAR_ID, String.valueOf(zcp.azkar.get(i).ID));
			values.put(KEY_CONFIG_ID, String.valueOf(zcp.ID));
			values.put(KEY_TIME, String.valueOf(time));

			temp = (zcp.azkar.get(i).repetedTimes + 4) / 5;

			for (int j = 0; j < temp; j++) {

				while (isInDataBase(time, db))
					time++;

				if (values.containsKey(KEY_TIME))
					values.remove(KEY_TIME);

				values.put(KEY_TIME, String.valueOf(time));

				db.insert(TABLE_NOTIFICATION, null, values);

				time += waitTime;
			}
			values.clear();
		}
	}

	private boolean isInDataBase(long time, SQLiteDatabase db) {

		Cursor cursor = db.query(TABLE_NOTIFICATION,
				new String[] { KEY_AZKAR_ID }, KEY_TIME + " =?",
				new String[] { String.valueOf(time) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		boolean flag = cursor.getCount() == 0 ? false : true;

		cursor.close();

		return flag;
	}

	private void deleteNotificatinByZekrID(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATION, KEY_AZKAR_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
		notifyDataChange();
	}

	private void deleteNotificatinByConfigID(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTIFICATION, KEY_CONFIG_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
		notifyDataChange();
	}

	// Statistics Table
	private String getDate() {
		Calendar c = Calendar.getInstance();
		return (c.get(Calendar.DAY_OF_MONTH)) + "/"
				+ (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
	}

	public void addCount(int num) {
		String Date = getDate();
		addToDataBaseStatistics(Date, num);

	}

	public void addToDataBaseStatistics(String Date, int num) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.query(TABLE_STATISTICS, new String[] { KEY_COUNT },
				KEY_DATE + "=?", new String[] { Date }, null, null, null, null);
		ContentValues values = new ContentValues();

		if (cursor != null)
			cursor.moveToFirst();

		if (cursor.getCount() != 0) {
			num += cursor.getInt(0);
			values.put(KEY_COUNT, num);
			db.update(TABLE_STATISTICS, values, KEY_DATE + " = ?",
					new String[] { Date });
		} else {
			values.put(KEY_DATE, Date);
			values.put(KEY_COUNT, num);
			db.insert(TABLE_STATISTICS, null, values);
		}

		db.close();
		notifyDataChange();
	}

	public Statistics getTodayStatistcs() {
		SQLiteDatabase db = getReadableDatabase();
		String Date = getDate();

		Cursor cursor = db.query(TABLE_STATISTICS, new String[] { KEY_COUNT },
				KEY_DATE + "=?", new String[] { Date }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();
		db.close();

		Statistics st = new Statistics();
		st.date = Date;
		st.numberOFZekr = (cursor.getCount() == 0 ? 0 : cursor.getInt(0));

		return st;
	}

	public List<Statistics> getAllStatistcs(boolean todayFlag) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Statistics> list = new ArrayList<Statistics>();

		String selectQuery = "SELECT " + KEY_DATE + " , " + KEY_COUNT
				+ " FROM " + TABLE_STATISTICS;
		String Date = getDate();

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Statistics st = new Statistics();
				st.date = cursor.getString(0);
				st.numberOFZekr = cursor.getInt(1);
				if (!st.date.equals(Date) || todayFlag)
					list.add(st);
			} while (cursor.moveToNext());
		}

		db.close();
		return list;
	}

	public int getAverageStatistics() {
		ArrayList<Statistics> list = (ArrayList<Statistics>) getAllStatistcs(WITH_TODAY);
		long average = 0;
		for (int i = 0; i < list.size(); i++)
			average += list.get(i).numberOFZekr;
		return (int) (average / (list.size() == 0 ? 1 : list.size()));

	}
}
