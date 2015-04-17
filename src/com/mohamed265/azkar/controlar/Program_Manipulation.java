package com.mohamed265.azkar.controlar;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.mohamed265.azkar.MainActivity;
import com.mohamed265.azkar.R;
import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.dataStructure.ZekrConfigProgram;
import com.mohamed265.azkar.model.DataBase;
import com.mohamed265.azkar.model.InteractiveArrayAdapter;
import com.mohamed265.azkar.model.NotificationService;
import com.mohamed265.azkar.model.SimpleGestureFilter;
import com.mohamed265.azkar.model.SimpleGestureFilter.SimpleGestureListener;
import com.mohamed265.azkar.model.onDataChangeListener;

public class Program_Manipulation extends Activity implements
		SimpleGestureListener {

	private final int INVISIBLE = View.INVISIBLE;
	private final int VISIBLE = View.VISIBLE;

	private SimpleGestureFilter detector;
	ImageView first, second, hiden;
	boolean page, edit = false;
	TextView programName, startTime, endTime;
	EditText proNameEdit;
	TimePicker tpStart, tpEnd;
	ZekrConfigProgram program;
	ListView selected;
	MenuItem ok, addZekr;
	DataBase db;
	Context con = this;
	ArrayAdapter<Zekr> adapter;
	List<Zekr> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_program);
		setTitle("≈⁄œ«œ »—‰«„Ã");
		db = new DataBase(this);

		Display display = getWindowManager().getDefaultDisplay();
		final int width = display.getWidth();
		final int height = display.getHeight();
		// enhance Display

		detector = new SimpleGestureFilter(this, this);
		first = (ImageView) findViewById(R.id.first_imageview);
		second = (ImageView) findViewById(R.id.second_imageview);
		hiden = (ImageView) findViewById(R.id.hiden);
		hiden.setVisibility(INVISIBLE);
		page = true;

		programName = (TextView) findViewById(R.id.program_name);
		startTime = (TextView) findViewById(R.id.program_start);
		endTime = (TextView) findViewById(R.id.end_program);

		proNameEdit = (EditText) findViewById(R.id.program_name_edit);

		tpStart = (TimePicker) findViewById(R.id.start_time_picker);
		tpEnd = (TimePicker) findViewById(R.id.end_time_picker);

		selected = (ListView) findViewById(R.id.selected_list);
		selected.setVisibility(INVISIBLE);

		try {
			program = ZekrConfigProgram.fromJson(getIntent().getExtras()
					.getString("prog"));
		} catch (Exception e) {
		}

		if (program != null) {
			tpStart.setCurrentMinute((int) (program.startProgram % 3600) / 60);
			tpStart.setCurrentHour((int) (program.startProgram / 3600));
			tpEnd.setCurrentMinute((int) (program.endProgram % 3600) / 60);
			tpEnd.setCurrentHour((int) (program.endProgram / 3600));
			proNameEdit.setText(program.programName);
			edit = true;
		} else {
			program = new ZekrConfigProgram();
			program.startProgram = program.endProgram = calcTimeNow();
			program.programName = "TEST";
			edit = false;
		}

		list = db.getAllZekrs();
		adapter = new InteractiveArrayAdapter(this, list, program.azkar);
		selected.setAdapter(adapter);

		new onDataChangeListener() {
			@Override
			public void onChange() {
				list = db.getAllZekrs();
				adapter.clear();
				adapter.addAll(list);
			}
		};

		tpStart.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				program.startProgram = hourOfDay * 3600 + minute * 60;
			}
		});

		tpEnd.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				program.endProgram = hourOfDay * 3600 + minute * 60;
			}
		});

		proNameEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				program.programName = proNameEdit.getText().toString();
			}
		});
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	void viewFirstPage() {
		first.setBackgroundResource(R.drawable.iconswap_full);
		second.setBackgroundResource(R.drawable.iconswap_empt);

		programName.setVisibility(VISIBLE);
		startTime.setVisibility(VISIBLE);
		endTime.setVisibility(VISIBLE);
		proNameEdit.setVisibility(VISIBLE);
		tpStart.setVisibility(VISIBLE);
		tpEnd.setVisibility(VISIBLE);

		selected.setVisibility(INVISIBLE);
		addZekr.setVisible(false);
		ok.setVisible(false);
	}

	void viewSecondPage() {
		first.setBackgroundResource(R.drawable.iconswap_empt);
		second.setBackgroundResource(R.drawable.iconswap_full);

		programName.setVisibility(INVISIBLE);
		startTime.setVisibility(INVISIBLE);
		endTime.setVisibility(INVISIBLE);
		proNameEdit.setVisibility(INVISIBLE);
		tpStart.setVisibility(INVISIBLE);
		tpEnd.setVisibility(INVISIBLE);

		selected.setVisibility(VISIBLE);
		addZekr.setVisible(true);
		ok.setVisible(true);

		hideKeyboard();
	}

	private void hideKeyboard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_program, menu);
		ok = menu.findItem(R.id.ok_done);
		addZekr = menu.findItem(R.id.addZekr_addprog);
		addZekr.setVisible(false);
		ok.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.addZekr_addprog: {
			FragmentManager fm = getFragmentManager();
			Zekr_Manipulation object = new Zekr_Manipulation();
			object.show(fm, " ");
		}
			return true;
		case R.id.ok_done: {
			if (program.azkar.size() != 0) {
				program.calculateWaitTime();
				ms("«Œ— ŒÿÊ…", " «·Êﬁ  »Ì  «·–ﬂ— "
						+ (program.waitTimeBetween / 3600) + " ”«⁄… "
						+ ((program.waitTimeBetween % 3600) / 60) + " œﬁÌﬁ… "
						+ "\nÂ·  —Ìœ «·⁄Êœ… ø", "«⁄Êœ", "Õ›Ÿ");
			} else {
				Toast.makeText(con, "ÌÃ» ≈ŒÌ«— –ﬂ— Ê«Õœ ⁄·Ï «·«ﬁ·",
						Toast.LENGTH_LONG * 1000).show();
			}
		}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	void ms(String title, String mess, String fanswer, String sanswer) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title)
				.setMessage(mess)
				.setNegativeButton(fanswer,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						})
				.setPositiveButton(sanswer,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								if (edit)
									db.updateProgram(program);
								else
									db.addProgram(program);

								Toast.makeText(
										MainActivity.con,
										(edit ? "·ﬁœ  „  ⁄œÌ· «·»—‰«„Ã »‰Ã«Õ"
												: "·ﬁœ  „ ≈‰‘«¡ «·»—‰«„Ã »‰Ã«Õ"),
										Toast.LENGTH_LONG * 1000).show();

								Intent i = new Intent(con,
										NotificationService.class);
								stopService(i);
								startService(i);
								finish();
							}
						});
		builder.show();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		// String str = "";

		switch (direction) {

		case SimpleGestureFilter.SWIPE_RIGHT:
			page = !page;
			if (page) {
				viewFirstPage();
			} else {
				viewSecondPage();
			}
			// str = "Swipe Right";
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			page = !page;
			if (page) {
				viewFirstPage();
			} else {
				viewSecondPage();

			}
			// str = "Swipe Left";
			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			// str = "Swipe Down";
			break;
		case SimpleGestureFilter.SWIPE_UP:
			// str = "Swipe Up";
			break;

		}
		// Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoubleTap() {
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}

	private long calcTimeNow() {
		long millis, tnow;
		millis = System.currentTimeMillis();
		tnow = (millis / 1000) % 60;
		tnow += (((millis / (1000 * 60)) % 60)) * 60;
		tnow += (((millis / (1000 * 60 * 60)) % 24) + 2) * 3600;
		return tnow;
	}

}
