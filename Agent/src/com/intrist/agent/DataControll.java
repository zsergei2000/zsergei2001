package com.intrist.agent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class DataControll {

	private static final String TAG = "myLogs";
	private DataBase sqdb;
	private Context Ctx;
	
	DataControll(Context Ctx, DataBase sqdb) {
		
		this.sqdb = sqdb;
		this.Ctx = Ctx;
		
	}
	
	public boolean deviceData() {
		
		String текДата;
		String текДатаМинусДень;
		String датаСервера = "";
		
		String query = "select date from dateServer";
		
		Cursor c = sqdb.getSQLData(query, null);
		
		Calendar cel = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        текДата = formatter.format(cel.getTime());
		
        cel.add(Calendar.DATE, -1);
        текДатаМинусДень = formatter.format(cel.getTime());
        
        Log.d(TAG, "Дата андроид - " + текДата);
        
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					Log.d(TAG, "Дата сервера - " + c.getString(0));
					датаСервера = c.getString(0);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		if (датаСервера.equals("")) {
			String toastText = "Необходима инициализация!";
			Log.d(TAG, toastText);
			Toast.makeText(Ctx, toastText, Toast.LENGTH_LONG).show();
			return false;
		}
		
		try {
			Date date1 = formatter.parse(текДата);
			Date date2 = formatter.parse(датаСервера);
			Date date4 = formatter.parse(текДатаМинусДень);

			int com = date2.compareTo(date1);
			
			if (com>0) {
				String toastText = "Ошибка. Проверте настройки даты!";
				Log.d(TAG, toastText);
				Toast.makeText(Ctx, toastText, Toast.LENGTH_LONG).show();
				return false;
			}
			
			com = date2.compareTo(date4);
			
			if (com<0) {
				String toastText = "Данные устарели. \nНеобходима инициализация!";
				Log.d(TAG, toastText);
				Toast.makeText(Ctx, toastText, Toast.LENGTH_LONG).show();
				return false;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
}
