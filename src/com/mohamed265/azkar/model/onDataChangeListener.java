package com.mohamed265.azkar.model;

import java.util.ArrayList;


public abstract class onDataChangeListener {

	public onDataChangeListener() {
		if (DataBase.list == null)
			DataBase.list = new ArrayList<onDataChangeListener>();
		DataBase.list.add(this);
	}

	public abstract void onChange();
}
