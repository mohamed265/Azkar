package com.mohamed265.azkar;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;

import com.mohamed265.azkar.dataStructure.Zekr;
import com.mohamed265.azkar.model.DataBase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreviewZekr extends Activity {

	RelativeLayout rl;
	DataBase db;
	TextView zekrDescreption;
	Button descreption, addPalm;
	boolean flagView = false;
	ArrayList<ImageView> images;
	Context con = this;
	Random rand;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify_message);
		setTitle("ÐßÑ");
		db = new DataBase(this);
		rl = (RelativeLayout) findViewById(R.id.notify_message_layout);

		rand = new Random();

		Display display = getWindowManager().getDefaultDisplay();
		final int width = display.getWidth();
		final int height = display.getHeight();

		Zekr zekr = null;
		try {
			zekr = Zekr.fromJson((String) getIntent().getExtras().getString(
					"des"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String tempSpace = "";
		for (int i = 0; i < width; i++)
			tempSpace += " ";
		zekr.zekrContent += tempSpace;
		zekr.zekrDescription += tempSpace;

		TextView zekrContent = (TextView) findViewById(R.id.tv);
		zekrContent.setText(zekr.zekrContent);
		zekrContent.setTextSize(40);
		zekrContent.setMaxHeight(height / 4);
		zekrContent.setMovementMethod(new ScrollingMovementMethod());
		// zekrContent.setTextDirection(2);
		// zekrContent.setTextDirection(zekrContent.TEXT_DIRECTION_LTR);
		// zekrContent.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);

		descreption = (Button) findViewById(R.id.plusbutton);
		descreption.setBackgroundResource(R.drawable.plus);
		descreption.setY(height / 4);
		int descreptionWidth = width / 12;
		descreption.setLayoutParams(new RelativeLayout.LayoutParams(
				descreptionWidth, descreptionWidth));

		zekrDescreption = (TextView) findViewById(R.id.tv1);
		zekrDescreption.setText(zekr.zekrDescription);
		zekrDescreption.setY(height / 4 + width / 12 + width / 8);
		zekrDescreption.setTextSize(30);
		zekrDescreption.setVisibility(4);
		zekrDescreption.setMaxHeight(height / 4);
		zekrDescreption.setMovementMethod(new ScrollingMovementMethod());

		final int hwigh4 = height / 4;

		final int low = height / 4 + width / 12 + width / 8;

		descreption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (flagView) {
					zekrDescreption.setVisibility(4);
					descreption.setBackgroundResource(R.drawable.plus);
					for (int i = 0; i < images.size(); i++) {
						if (images.get(i).getY() - hwigh4 > low)
							images.get(i).setY(images.get(i).getY() - hwigh4);
					}
				} else {
					zekrDescreption.setVisibility(0);
					descreption.setBackgroundResource(R.drawable.minus);
					for (int i = 0; i < images.size(); i++) {
						images.get(i).setY(images.get(i).getY() + hwigh4);
					}
				}

				flagView = !flagView;
			}
		});

		addPalm = (Button) findViewById(R.id.addpalm);
		addPalm.setBackgroundResource(R.drawable.up);
		descreptionWidth = width / 4;
		addPalm.setX(width / 2 - descreptionWidth / 2);
		addPalm.setY(height / 4);
		addPalm.setLayoutParams(new RelativeLayout.LayoutParams(
				descreptionWidth, descreptionWidth));

		images = new ArrayList<ImageView>();

		addPalm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView im = new ImageView(con);
				im.setBackgroundResource(R.drawable.palm);
				im.setX(rand.nextInt(width - 1));
				if (flagView)
					im.setY((float) (rand.nextInt(low / 2) + low * 1.5));
				else
					im.setY(rand.nextInt(low) + low);
				rl.addView(im, new RelativeLayout.LayoutParams(100, 100));
				images.add(im);
				db.addCount(1);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notify_message, menu);
		return true;
	}

}
