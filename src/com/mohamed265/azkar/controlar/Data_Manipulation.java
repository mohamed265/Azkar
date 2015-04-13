package com.mohamed265.azkar.controlar;

import java.util.List;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mohamed265.azkar.PreviewZekr;
import com.mohamed265.azkar.R;
import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.dataStructure.ZekrConfigProgram;
import com.mohamed265.azkar.model.DataBase;
import com.mohamed265.azkar.model.onDataChangeListener;

public class Data_Manipulation extends ListActivity {

	@Override
	protected void onRestart() {
		InitProgram();
		InitAzkar();
		super.onRestart();
	}

	boolean preview; // true program
						// false azkar
	DataBase db;
	List<Zekr> azkarList;
	List<ZekrConfigProgram> programList;
	MenuItem men;
	public static Context con;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		preview = getIntent().getBooleanExtra("type", false);
		db = new DataBase(this);
		ListView list = getListView();
		con = this;
		getListView().setBackgroundResource(R.drawable.background);

		InitProgram();
		InitAzkar();

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				FragmentManager fm = getFragmentManager();
				Data_Manipulation_Long_Click object = null;

				if (preview)
					object = new Data_Manipulation_Long_Click(preview, programList.get(position));
				else
					object = new Data_Manipulation_Long_Click(preview, azkarList.get(position));

				object.show(fm, " ");

				return true;
			}
		});

		new onDataChangeListener() {
			@Override
			public void onChange() {
				InitProgram();
				InitAzkar();
			}
		};
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (preview) {
			Intent i = new Intent(Data_Manipulation.this, Program_Manipulation.class);
			i.putExtra("prog", programList.get(position).toJson());
			startActivity(i);
		} else {
			Intent i = new Intent(Data_Manipulation.this, PreviewZekr.class);
			i.putExtra("des", azkarList.get(position).toJson());
			startActivity(i);
		}

	}

	void InitProgram() {
		if (preview) {
			setTitle("«·»—«„Ã");
			programList = db.getAllPrograms();
			String[] names = new String[programList.size()];
			for (int i = 0; i < names.length; i++)
				names[i] = programList.get(i).programName;

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, names);
			setListAdapter(adapter);
		}
	}

	void InitAzkar() {
		if (!preview) {
			setTitle("«·«–ﬂ«—");
			azkarList = db.getAllZekrs();
			String[] names = new String[azkarList.size()];
			for (int i = 0; i < names.length; i++)
				names[i] = azkarList.get(i).zekrContent;

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, names);
			setListAdapter(adapter);
		}
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		InitProgram();
		InitAzkar();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.preview_menu, menu);
		if (preview)
			men = menu.findItem(R.id.addZekr_pre);
		else
			men = menu.findItem(R.id.addprogram_pre);
		men.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addZekr_pre:
			FragmentManager fm = getFragmentManager();
			Zekr_Manipulation object = new Zekr_Manipulation();
			object.show(fm, " ");
			break;
		case R.id.addprogram_pre:
			Intent i = new Intent(con, Program_Manipulation.class);
			startActivity(i);
			break;
		}
		return true;
	}
}
