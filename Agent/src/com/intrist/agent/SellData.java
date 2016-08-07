package com.intrist.agent;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SellData extends Activity {

	private static final int OBJECT = 0, LASTMF = 1, LASTMP = 2, DIFFERENT = 3, TODAY = 4, COUNT = 5;
	private LayoutInflater ltInflater;
	private static final String TAG = "myLogs";
	
	private DataBase sqdb;
	
	GridView gvMain;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.sell_data);
		
		sqdb = new DataBase(this);
		sqdb.OpenDB();
		
		ltInflater = getLayoutInflater();
		
		Intent intent = getIntent();
		
		String name = intent.getStringExtra("name");
		
        createTabelsMenu(name);
        //createTable();
        
	}
	
	private void createTable() {
		
		ArrayList<String[]> listData = getSellData();
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items);
		items.removeAllViews();
		
		for (String[] data : listData) {
			
			View item = ltInflater.inflate(R.layout.sell_item, items, false);
			((TextView) item.findViewById(R.id.sell_object)).setText(data[OBJECT]);
			((TextView) item.findViewById(R.id.sell_lastMF)).setText(data[LASTMF]);
			((TextView) item.findViewById(R.id.sell_lastMP)).setText(data[LASTMP]);
			((TextView) item.findViewById(R.id.sell_different)).setText(data[DIFFERENT]);
			((TextView) item.findViewById(R.id.sell_today)).setText(data[TODAY]);
			
			((LinearLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(touch);			
			
			items.addView(item);
		}
		
	}
	
	OnTouchListener touch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private ArrayList<String[]> getSellData() {
		
		ArrayList<String[]> listData = new ArrayList<String[]>();
		
		String query = "select object, lastMF, lastMP, diferent, today "
				+ "from "+"salesData"+" as dataTable";
		
		Cursor c = sqdb.getSQLData(query, null);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {

					String[] data = new String[COUNT];
					
					data[OBJECT]	= c.getString(0);
					data[LASTMF]	= c.getString(1);
					data[LASTMP]	= c.getString(2);
					data[DIFFERENT]	= c.getString(3);
					data[TODAY]		= c.getString(4);
					
					listData.add(data);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
	}
	
	public void createTabelsMenu(String name) {
		
		String query = "select column "
				+ "from reports "
				+ "where report = ? LIMIT 15";
				//+ "group by column";
		
		Cursor c = sqdb.getSQLData(query, new String[] { name });
		
		ArrayList<String> listData = new ArrayList<String>();
		
		int count = 0;
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
						
					if (listData.contains(c.getString(0))) {
						break;
					}
					
					listData.add(c.getString(0));
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		String[] tabelsData = new String[listData.size()];
		
		for (int i = 0; i < listData.size(); i++) {
			
			tabelsData[i] = listData.get(i);
		}
		
		query = "select value "
			+ "from reports "
			+ "where report = ?";
		
		c = sqdb.getSQLData(query, new String[] { name });
		
		String[] tabelsData1 = new String[c.getCount()+tabelsData.length];
		
		for (int i = 0; i < tabelsData.length; i++) {
			
			tabelsData1[i] = tabelsData[i];
		}
		
		count = 0 + tabelsData.length;
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					tabelsData1[count++] = c.getString(0);
						
				} while (c.moveToNext());
			}
		}
		c.close();
		
		adjustGridView(tabelsData1, tabelsData.length);
	}
	
	private void adjustGridView(String[] tabelsData, int columnLength) {
		
		adapter = new ArrayAdapter<String>(this, R.layout.gride_item1, R.id.tvText, tabelsData);
		gvMain = (GridView) findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);
		
		//gvMain.setNumColumns(GridView.AUTO_FIT);
		//gvMain.setColumnWidth(80);
		
		gvMain.setNumColumns(columnLength);
		gvMain.setVerticalSpacing(1);
		gvMain.setHorizontalSpacing(1);
		
	}
	
	protected void onDestroy() {
		super.onDestroy();		
		//sqdb.closeDB();
	}
	
}
