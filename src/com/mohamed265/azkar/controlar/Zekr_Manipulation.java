package com.mohamed265.azkar.controlar;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mohamed265.azkar.MainActivity;
import com.mohamed265.azkar.R;
import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.model.DataBase;

public class Zekr_Manipulation extends DialogFragment {

	Button bu;
	Zekr zekr_;
	EditText cont, des, re;
	boolean flagEdit;

	public Zekr_Manipulation() {
		flagEdit = false;
	}

	public Zekr_Manipulation(Zekr zr) {
		zekr_ = zr;
		flagEdit = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (flagEdit)
			getDialog().setTitle(" ⁄œÌ· –ﬂ—");
		else
			getDialog().setTitle("≈÷«›… –ﬂ—");

		View v = inflater.inflate(R.layout.addzekr_view, null);
		bu = (Button) v.findViewById(R.id.add_addzekr);
		cont = (EditText) v.findViewById(R.id.content_addzekr_edit);
		des = (EditText) v.findViewById(R.id.descrition_addzekr_edit);
		re = (EditText) v.findViewById(R.id.repettime_addzekr_edit);

		if (zekr_ != null) {
			cont.setText(zekr_.zekrContent);
			des.setText(zekr_.zekrDescription);
			re.setText(zekr_.repetedTimes + "");
		}

		bu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DataBase dp = new DataBase(MainActivity.con);
				Zekr zekr = new Zekr(cont.getText().toString(), des.getText()
						.toString(), Integer.valueOf(re.getText().toString()));

				if (flagEdit) {
					zekr.ID = zekr_.ID;
					dp.updateZekr(zekr);
					Toast.makeText(MainActivity.con, " „  ⁄œÌ· «·–ﬂ— »‰Ã«Õ",
							Toast.LENGTH_LONG * 1000).show();
				} else {
					dp.addZekr(zekr);
					Toast.makeText(MainActivity.con, " „ ≈÷«›… «·–ﬂ— »‰Ã«Õ",
							Toast.LENGTH_LONG * 1000).show();
				}

				dismiss();
			}
		});
		return v;
	}
}