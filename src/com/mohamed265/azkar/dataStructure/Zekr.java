package com.mohamed265.azkar.dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

public class Zekr {
	public String zekrContent, zekrDescription;
	public int repetedTimes, ID;

	public Zekr(String c, String d, int r) {
		zekrContent = c;
		zekrDescription = d;
		repetedTimes = r;
	}

	public Zekr(String c, String d, int r, int id) {
		ID = id;
		zekrContent = c;
		zekrDescription = d;
		repetedTimes = r;
	}

	public int toViewRepet() {
		return (repetedTimes > 5 ? 5 : repetedTimes);
	}

	public String toViewNotification() {
		return zekrContent + " " + toViewRepet() + " „—« ";
	}

	public String toView() {
		return zekrContent + " " + repetedTimes + " „—« ";
	}

	public String toJson() {
		JSONObject obj = new JSONObject();

		try {
			obj.put("zekrContent", zekrContent);
			obj.put("zekrDescription", zekrDescription);
			obj.put("repetedTimes", repetedTimes);
			obj.put("ID", ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return obj.toString();
	}

	public static Zekr fromJson(String js) throws JSONException {
		JSONObject obj = new JSONObject(js);
		String zekrContent = null, zekrDescription = null;
		int repetedTimes = 0, id = 0;
		try {
			zekrContent = obj.getString("zekrContent");
			zekrDescription = obj.getString("zekrDescription");
			repetedTimes = obj.getInt("repetedTimes");
			id = obj.getInt("ID");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new Zekr(zekrContent, zekrDescription, repetedTimes, id);
	}

}
