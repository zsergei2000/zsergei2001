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
		
		String �������;
		String ����������������;
		String ����������� = "";
		
		String query = "select date from dateServer";
		
		Cursor c = sqdb.getSQLData(query, null);
		
		Calendar cel = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ������� = formatter.format(cel.getTime());
		
        cel.add(Calendar.DATE, -1);
        ���������������� = formatter.format(cel.getTime());
        
        Log.d(TAG, "���� ������� - " + �������);
        
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					Log.d(TAG, "���� ������� - " + c.getString(0));
					����������� = c.getString(0);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		if (�����������.equals("")) {
			String toastText = "���������� �������������!";
			Log.d(TAG, toastText);
			Toast.makeText(Ctx, toastText, Toast.LENGTH_LONG).show();
			return false;
		}
		
		try {
			Date date1 = formatter.parse(�������);
			Date date2 = formatter.parse(�����������);
			Date date4 = formatter.parse(����������������);

			int com = date2.compareTo(date1);
			
			if (com>0) {
				String toastText = "������. �������� ��������� ����!";
				Log.d(TAG, toastText);
				Toast.makeText(Ctx, toastText, Toast.LENGTH_LONG).show();
				return false;
			}
			
			com = date2.compareTo(date4);
			
			if (com<0) {
				String toastText = "������ ��������. \n���������� �������������!";
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
