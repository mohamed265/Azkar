package com.mohamed265.azkar.model;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mohamed265.azkar.R;
import com.mohamed265.azkar.dataStructure.Zekr;

public class InteractiveArrayAdapter extends ArrayAdapter<Zekr> {

	public List<Zekr> list, old;
	private final Activity context;

	public InteractiveArrayAdapter(Activity context, List<Zekr> list,
			List<Zekr> old) {
		super(context, R.layout.selected_item_row, list);
		this.context = context;
		this.list = list;
		this.old = old;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		final int pos = position;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.selected_item_row, null);
			TextView x = (TextView) view.findViewById(R.id.itemstring);
			x.setText(list.get(pos).toView());

			CheckBox checkbox = (CheckBox) view.findViewById(R.id.check);

			for (int i = 0; i < old.size(); i++) {
				if (old.get(i).ID == list.get(pos).ID) {
					checkbox.setChecked(true);
					break;
				}
			}

			checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked)
						old.add(list.get(pos));
					else {
						for (int i = 0; i < old.size(); i++) {
							if (list.get(pos).ID == old.get(i).ID) {
								old.remove(i);
								break;
							}
						}
					}
				}
			});

		} else
			view = convertView;

		return view;
	}

	public List<Zekr> getOld() {
		return old;
	}
}