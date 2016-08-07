package com.intrist.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class ActivityTech extends Activity {

	private static final String TAG = "myLogs";
	
	private DataBase sqdb;
	
	private Spinner spinnerTabels;
	
	GridView gvMain;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tech_tabs);
		
		sqdb = new DataBase(this);
		sqdb.OpenDB();
		
        createTabelsMenu();
        
	}
		
	private void createTabelsMenu() {
		
		List<Map<String, String>> aData = new ArrayList<Map<String, String>>();
		
		Map<String, String> m;
		
		m = new HashMap<String, String>();
		m.put("kod",	"1");
		m.put("name",	"TRT");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"2");
		m.put("name",	"agents");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"3");
		m.put("name",	"AgentTRT");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"4");
		m.put("name",	"items");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"5");
		m.put("name",	"priceType");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"6");
		m.put("name",	"priceTypeTRT");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"7");
		m.put("name",	"itemsPrice");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"8");
		m.put("name",	"route");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"9");
		m.put("name",	"kontragent");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"10");
		m.put("name",	"kontragentTRT");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"11");
		m.put("name",	"stock");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"12");
		m.put("name",	"stockAgent");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"13");
		m.put("name",	"balance");
		aData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod",	"14");
		m.put("name",	"reports");
		aData.add(m);
		
		String[] from = { "name" };
		int[] to = { R.id.spinner_text };
		
		SimpleAdapter agAdapter = new SimpleAdapter(this, aData, R.layout.spinner_item, from, to);
		agAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
		
		spinnerTabels = (Spinner) findViewById(R.id.spinner1);
		spinnerTabels.setAdapter(agAdapter);
		//spinnerTabels.setPrompt("¬˚·ÂËÚÂ Ú‡·ÎËˆÛ");
		spinnerTabels.setOnItemSelectedListener(spinnerClick);
		//spinnerAgent.
		//spinnerTabels.setOnItemClickListener(spinnerClick1);
		//if (findFlag) spinnerAgent.setSelection(aData.indexOf(agent));
		
		//findFlag = !findFlag;
		
		//spinnerTabels.setSelection(aData.indexOf(agent));
		
	}
	
	OnItemSelectedListener spinnerClick = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			
			Log.d(TAG, "≈—“‹ Õ¿∆¿“»≈");
			
			/*changeAgent(aData.get(arg2));
			
			if (!findFlag)
				changeAgent(aData.get(arg2));
			else
				findFlag = !findFlag;*/
			}
		
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
	
	public void click_show(View v) {
		
		String thisTable = ((HashMap)spinnerTabels.getSelectedItem()).values().toArray()[0].toString();
		
		String query = "select * "
				+ "from "+thisTable+" as dataTable";
		
		//spinnerTabels.getSelectedItem().get("name");
		//spinnerTabels.getSelectedItem()[0];
		
		Cursor c = sqdb.getSQLData(query, null);
		
		String[] tabelsData = c.getColumnNames();
		String[] tabelsData1 = new String[c.getCount()*tabelsData.length+tabelsData.length];
		
		for (int i = 0; i < tabelsData.length; i++) {
			
			tabelsData1[i] = tabelsData[i];
			
		}
		
		int count = 0 + tabelsData.length;
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					for (int i = 0; i < tabelsData.length; i++) {
						
						tabelsData1[count++] = c.getString(i)==null?"":c.getString(i);
						
					}
				} while (c.moveToNext());
			}
		}
		c.close();
		
		adjustGridView(tabelsData1, tabelsData.length);
		
	}
	
	private void adjustGridView(String[] tabelsData, int columnLength) {
		
		adapter = new ArrayAdapter<String>(this, R.layout.gride_item, R.id.tvText, tabelsData);
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
