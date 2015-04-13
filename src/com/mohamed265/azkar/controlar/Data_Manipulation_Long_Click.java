package com.mohamed265.azkar.controlar;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mohamed265.azkar.MainActivity;
import com.mohamed265.azkar.PreviewZekr;
import com.mohamed265.azkar.R;
import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.dataStructure.ZekrConfigProgram;
import com.mohamed265.azkar.model.DataBase;

 

public class Data_Manipulation_Long_Click extends android.app.DialogFragment {
	boolean preview;
	Zekr zekr;
	ZekrConfigProgram zcp;

	public Data_Manipulation_Long_Click(boolean pv, ZekrConfigProgram zcp_) {
		preview = pv;
		zcp = zcp_;
	}

	public Data_Manipulation_Long_Click(boolean pv, Zekr zekr_) {
		preview = pv;
		zekr = zekr_;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("√⁄œ«œ« ");
		View v = inflater.inflate(R.layout.long_click_preview, null);
		ListView list = (ListView) v.findViewById(R.id.long_click_preview);
		String[] names = new String[] { "› Õ", " ⁄œÌ·", "„”Õ" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MainActivity.con, android.R.layout.simple_list_item_1, names);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (preview) {
					if (position == 0 || position == 1) {
						Intent i = new Intent(Data_Manipulation.con,
								Program_Manipulation.class);
						i.putExtra("prog", zcp.toJson());
						startActivity(i);
					} else if (position == 2) {
						new DataBase(Data_Manipulation.con).deleteProgram(zcp);
					}
				} else {
					if (position == 0) {
						Intent i = new Intent(Data_Manipulation.con, PreviewZekr.class);
						i.putExtra("des", zekr.toJson());
						startActivity(i);
					} else if (position == 1) {
						FragmentManager fm = getFragmentManager();
						Zekr_Manipulation ad = new Zekr_Manipulation(zekr);
						ad.show(fm, " ");
					} else if (position == 2) {
						new DataBase(Data_Manipulation.con).deleteZekr(zekr);
					}
				}
				dismiss();
			}
		});
		return v;
	}

}
