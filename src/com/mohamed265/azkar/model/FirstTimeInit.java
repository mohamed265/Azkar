package com.mohamed265.azkar.model;

import com.mohamed265.azkar.MainActivity;
import com.mohamed265.azkar.R.string;
import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.dataStructure.ZekrConfigProgram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class FirstTimeInit {
	Context con;
	ZekrConfigProgram program;

	DataBase db;

	public FirstTimeInit(Context c) {
		con = c;
		CheckAndInitAppFirstTime();
	}

	void CheckAndInitAppFirstTime() {
		final String PREFS_NAME = "MyPrefsFile";

		SharedPreferences settings = con.getSharedPreferences(PREFS_NAME, 0);

		if (settings.getBoolean("my_first_time", true)) {
			program = new ZekrConfigProgram();
			db = new DataBase(con);
			program.startProgram = 18000;
			program.endProgram = 75600;
			program.programName = "����� ������ � ������";
			addDataToDataBase();

			settings.edit().putBoolean("my_first_time", false).commit();
			settings.edit().putInt("MaxAverage", 1000).commit();
			MainActivity.sendNotification(1000, " �� ����� ���� ����� ",
					" ���� ����� ");
			db.close();
		}
	}

	void addDataToDataBase() {
		Zekr x0 = new Zekr(
				"����� ��� ������",
				" ����� ������� ������� ��� ���� ���� ���� :����� ������ ����� ������������ ������ ����� ������ ����������� ���� ���������� ���� ������� ����������� ���� ����������",
				1);
		Zekr x1 = new Zekr("����� ��� ���� ��� ����� ���� ", "test5", 10);
		Zekr x2 = new Zekr(
				"����� ���� ������ ����� ���� ������",
				"���� ����� ��������� ������� ������������ ��� ������ ������� ������� ������� ���������� ������ ������� ������ ������ ���������) ���� �������",
				100);
		Zekr x3 = new Zekr("���� ������ ���� ������� �� �� �� ���", "test4", 3);
		Zekr x4 = new Zekr("����� ���� ������� ����������", "test3", 3);
		Zekr x5 = new Zekr(
				"�� ��� ��� ���� ���� �� ���� �� �� ����� ��� ����� ��� ��� �� ��� ����",
				"test2", 3);
		Zekr x6 = new Zekr(
				"��� ���� ���� �� ��� �� ���� ��� �� ����� ��� �� ������� ��� ������ ������",
				"test1", 3);

		Zekr x7 = new Zekr(
				" ������ ���� ������ ���� �� ��� ��� �� ���� ������ � ���� ����",
				"������ ���� ������", 100);

		db.addZekr(x0);
		db.addZekr(x1);
		db.addZekr(x2);
		db.addZekr(x3);
		db.addZekr(x4);
		db.addZekr(x5);
		db.addZekr(x6);
		db.addZekr(x7);

		program.azkar.add(x0);
		program.azkar.add(x1);
		program.azkar.add(x2);
		program.azkar.add(x3);
		program.azkar.add(x4);
		program.azkar.add(x5);
		program.azkar.add(x6);
		program.azkar.add(x7);

		db.addProgram(program);
		Intent i = new Intent(MainActivity.con, NotificationService.class);
		con.startService(i);

	}
}
