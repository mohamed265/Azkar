package com.mohamed265.azkar.NOTUSED;
//package xxxxxx;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//
//import org.json.JSONException;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DataBaseHandler extends SQLiteOpenHelper {
//	private static final int DATABASE_VERSION = 1;
//
//	// Database Name
//	private static final String DATABASE_NAME = "AZKAR";
//
//	private static final String TABLE_AZKAR = "azkar";
//	private static final String TABLE_STATISTICS = "statistics";
//	private static final String TABLE_ZEKRCONFIG = "ZekrConfig";
//	private static final String TABLE_ZEKRCONFIG_AZKAR = "ZekrConfigAzkar";
//	private static final String TABLE_NOTIFICATION = "notification";
//
//	public static final String KEY_ID = "ID";
//	public static final String KEY_CONTENT = "content";
//	public static final String KEY_DESCRIPTION = "descreption";
//	public static final String KEY_REPETEDTIMES = "repetedTime";
//
//	public static final String KEY_DATE = "date";
//	public static final String KEY_COUNT = "count";
//
//	public static final String KEY_PROGRAMS = "programes";
//	public static final String KEY_START_TIME = "startTime";
//	public static final String KEY_END_TIME = "endTime";
//
//	public static final String KEY_AZKAR_ID = "ZekrID";
//	public static final String KEY_CONFIG_ID = "configID";
//	public static final String KEY_TIME = "time";
//
//	public DataBaseHandler(Context context) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//
//		String CREATE_STATISTICS_TABLE = "CREATE TABLE " + TABLE_STATISTICS
//				+ "( " + KEY_DATE + " TEXT PRIMARY KEY , " + KEY_COUNT
//				+ " INTEGER  )";
//		db.execSQL(CREATE_STATISTICS_TABLE);
//
//		String CREATE_AZKAR_TABLE = "CREATE TABLE " + TABLE_AZKAR + "("
//				+ KEY_ID
//				+ " INTEGER PRIMARY KEY AUTOINCREMENT ON DELETE CASCADE, "
//				+ KEY_CONTENT + " TEXT ," + KEY_DESCRIPTION + " TEXT ,"
//				+ KEY_REPETEDTIMES + " INTEGER )";
//		db.execSQL(CREATE_AZKAR_TABLE);
//
//		String CREATE_ZEKRPROG_TABLE = "CREATE TABLE " + TABLE_ZEKRCONFIG
//				+ "( " + KEY_ID
//				+ " INTEGER PRIMARY KEY AUTOINCREMENT ON DELETE CASCADE, "
//				+ KEY_START_TIME + " INTEGER, " + KEY_END_TIME + " INTEGER)";
//		db.execSQL(CREATE_ZEKRPROG_TABLE);
//
//		String CREATE_ZEKRPROG_AZKAR_TABLE = "CREATE TABLE "
//				+ TABLE_ZEKRCONFIG_AZKAR + "( " + KEY_AZKAR_ID
//				+ " INTEGER, FOREIGN KEY(" + KEY_AZKAR_ID + ") REFERENCES "
//				+ TABLE_AZKAR + "(" + KEY_ID + ")" + KEY_CONFIG_ID
//				+ " INTEGER, FOREIGN KEY(" + KEY_CONFIG_ID + ") REFERENCES "
//				+ TABLE_ZEKRCONFIG + "(" + KEY_ID + ")";
//		db.execSQL(CREATE_ZEKRPROG_AZKAR_TABLE);
//
//		String CREATE_ZEKRPROG_NOTIFICATION = "CREATE TABLE "
//				+ TABLE_NOTIFICATION + "( " + KEY_CONFIG_ID
//				+ " INTEGER, FOREIGN KEY(" + KEY_CONFIG_ID + ") REFERENCES "
//				+ TABLE_ZEKRCONFIG + "(" + KEY_ID + ") , " + KEY_AZKAR_ID
//				+ " INTEGER, FOREIGN KEY(" + KEY_AZKAR_ID + ") REFERENCES "
//				+ TABLE_AZKAR + "(" + KEY_ID + ") , " + KEY_TIME + " INTEGER";
//		db.execSQL(CREATE_ZEKRPROG_NOTIFICATION);
//	}
//
//	public void addProgram(String js) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(KEY_PROGRAMS, js);
//		db.insert(TABLE_ZEKRCONFIG, null, values);
//		db.close();
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		// TODO Auto-generated method stub
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AZKAR);
//
//	}
//
//	// public void dropData() {
//	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_AZKAR);
//	// }
//
//	public void addCount(int num) {
//		Calendar c = Calendar.getInstance();
//		String Date = (c.get(Calendar.DAY_OF_MONTH) + 1) + "/"
//				+ (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR);
//
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		long res = 0;
//
//		try {
//			ContentValues values = new ContentValues();
//			values.put(KEY_DATE, Date);
//			values.put(KEY_COUNT, num);
//			res = db.insert(TABLE_STATISTICS, null, values);
//		} catch (Exception e) {
//			res = -1;
//			db.close();
//		}
//		SQLiteDatabase db1 = this.getWritableDatabase();
//		if (res == -1) {
//			Cursor cursor = db.query(TABLE_STATISTICS,
//					new String[] { KEY_COUNT }, KEY_DATE + "=?",
//					new String[] { Date }, null, null, null, null);
//
//			if (cursor != null)
//				cursor.moveToFirst();
//
//			num += Integer.parseInt(cursor.getString(0));
//
//			ContentValues values = new ContentValues();
//			values.put(KEY_COUNT, num);
//
//			db1.update(TABLE_STATISTICS, values, KEY_DATE + " = ?",
//					new String[] { Date });
//		}
//
//		db1.close();
//	}
//
//	public int getCOUNT() {
//		Calendar c = Calendar.getInstance();
//		String Date = (c.get(Calendar.DAY_OF_MONTH) + 1) + "/"
//				+ (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR);
//
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		Cursor cursor = db.query(TABLE_STATISTICS, new String[] { KEY_COUNT },
//				KEY_DATE + "=?", new String[] { Date }, null, null, null, null);
//		int num = -1;
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		try {
//			num = Integer.parseInt(cursor.getString(0));
//		} catch (Exception e) {
//
//		}
//		if (num == -1)
//			num = 0;
//
//		db.close();
//
//		return num;
//	}
//
//	/**
//	 * All CRUD(Create, Read, Update, Delete) Operations
//	 */
//
//	public long addZekr(Zekr zker) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		// values.put(KEY_ID, String.valueOf(zker.ID));
//		values.put(KEY_CONTENT, zker.zekrContent);
//		values.put(KEY_REPETEDTIMES, String.valueOf(zker.repetedTimes));
//		values.put(KEY_DESCRIPTION, zker.zekrDescription);
//
//		// Inserting Row
//		long rs = db.insert(TABLE_AZKAR, null, values);
//		db.close(); // Closing database connection
//		return rs;
//	}
//
//	public Zekr getZekr(int id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_AZKAR, new String[] { KEY_CONTENT,
//				KEY_DESCRIPTION, KEY_REPETEDTIMES }, KEY_ID + "=?",
//				new String[] { String.valueOf(id) }, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		String content = cursor.getString(0);
//		String des = cursor.getString(1);
//		int rep = Integer.parseInt(cursor.getString(2));
//
//		return new Zekr(content, des, rep, id);
//	}
//
//	public List<Zekr> getAllZekrs() {
//		List<Zekr> contactList = new ArrayList<Zekr>();
//		// Select All Query
//		String selectQuery = "SELECT " + KEY_CONTENT + " , " + KEY_DESCRIPTION
//				+ " , " + KEY_REPETEDTIMES + " , " + KEY_ID + " FROM "
//				+ TABLE_AZKAR;
//
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				Zekr zekr = new Zekr(cursor.getString(0), cursor.getString(1),
//						cursor.getInt(2), cursor.getInt(3));
//				contactList.add(zekr);
//			} while (cursor.moveToNext());
//		}
//
//		// return contact list
//		return contactList;
//	}
//
//	public List<ZekrConfigProgrsm> getAllPrograms() {
//
//		List<ZekrConfigProgrsm> contactList = new ArrayList<ZekrConfigProgrsm>();
//		String selectQuery = "SELECT " + KEY_PROGRAMS + " FROM "
//				+ TABLE_ZEKRCONFIG;
//		SQLiteDatabase db = this.getWritableDatabase();
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		if (cursor.moveToFirst()) {
//			do {
//				ZekrConfigProgrsm zekr = null;
//				try {
//					zekr = ZekrConfigProgrsm.fromJson(cursor.getString(0));
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				contactList.add(zekr);
//			} while (cursor.moveToNext());
//		}
//		return contactList;
//	}
//
//	// public int nextZekrID() {
//	//
//	// ArrayList<Integer> set = new ArrayList<Integer>();
//	// List<Zekr> list = getAllZekrs();
//	// for (int i = 0; i < list.size(); i++)
//	// set.add(list.get(i).ID);
//	// Collections.sort(set);
//	// for (int i = 0; i < set.size(); i++) {
//	// if ((i + 1) != set.get(i))
//	// return i + 1;
//	// }
//	// return set.size() + 1;
//	// }
//
//	// // Updating single contact
//	// public int updateContact(Zekr zekr, String attr, String newValue) {
//	// SQLiteDatabase db = this.getWritableDatabase();
//	//
//	// ContentValues values = new ContentValues();
//	// values.put(attr, newValue);
//	//
//	// // updating row
//	// return db.update(TABLE_AZKAR, values, KEY_NAME + " = ?",
//	// new String[] { pro.name });
//	// }
//
//	public void deleteZekr(Zekr zekr) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_AZKAR, KEY_ID + " = ?",
//				new String[] { String.valueOf(zekr.ID) });
//		db.close();
//	}
//
//	public void deleteProgram(String js) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_ZEKRCONFIG, KEY_PROGRAMS + " = ?", new String[] { js });
//		db.close();
//	}
//
//	public int getAzkarCount() {
//		String countQuery = "SELECT  * FROM " + TABLE_AZKAR;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		if (cursor == null)
//			return 0;
//		int s = cursor.getCount();
//		cursor.close();
//		return s;
//	}
//
//}
