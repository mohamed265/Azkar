package com.mohamed265.azkar.dataStructure;

public class NotificationObject {
	public Zekr zekr;
	public int time, ID;
	public String progName;

	public NotificationObject() {

	}

	public NotificationObject(Zekr zekr_, String progName_, int time_, int ID_) {
		zekr = zekr_;
		progName = progName_;
		time = time_;
		ID = ID_;
	}

}
