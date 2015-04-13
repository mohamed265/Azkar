package com.mohamed265.azkar.dataStructure;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class ZekrConfigProgram {

	public ArrayList<Zekr> azkar;
	public long startProgram, endProgram, waitTimeBetween;
	public String programName;
	public int ID;

	public static final int day = 86400;

	public ZekrConfigProgram() {
		azkar = new ArrayList<Zekr>();
	}

	public long calculateWaitTime() {
		int times = 0;
		for (int i = 0; i < azkar.size(); i++)
			times += ((azkar.get(i).repetedTimes + 4) / 5);
		// Log.d("test", startProgram + " " + endProgram + " " + times);
		waitTimeBetween = (endProgram - startProgram) / times;
		return waitTimeBetween;
	}

	public String toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("azkarSize", azkar.size());
			for (int i = 0; i < azkar.size(); i++) {
				obj.put("zekr" + i, azkar.get(i).toJson());
			}
			obj.put("startProgram", startProgram);
			obj.put("endProgram", endProgram);
			obj.put("programName", programName);
			obj.put("type", ID);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj.toString();
	}

	public static ZekrConfigProgram fromJson(String js) throws JSONException {
		if (js == null)
			return null;
		JSONObject obj = new JSONObject(js);
		ZekrConfigProgram zcp = new ZekrConfigProgram();

		zcp.startProgram = obj.getLong("startProgram");
		zcp.endProgram = obj.getLong("endProgram");
		zcp.programName = obj.getString("programName");
		zcp.ID = obj.getInt("type");

		ArrayList<Zekr> azkar = new ArrayList<Zekr>();
		int programLenght = obj.getInt("azkarSize");
		for (int i = 0; i < programLenght; i++) {
			azkar.add(Zekr.fromJson(obj.getString("zekr" + i)));
		}
		zcp.azkar = azkar;

		return zcp;
	}
}
