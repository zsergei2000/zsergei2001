package com.intrist.agent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

public class PriceTRT {

	private static final String TAG = "myLogs";	
	private String kodTRT = "";	
	boolean listData = false;	
	private DataBase sqdb;
	//public String[] newline;
	private NewData newline = new NewData();
	//public static final int TABLE_KOD_ITEM = 0, TABLE_PRICE_TYPE = 1, TABLE_PRICE = 2, TABLE_NAME_ITEM = 3;
	//public static final int ITEM_KOD = 0, ITEM_NAME = 1, PRICE_TYPE = 2, PRICE = 3, COMMENT = 4;
	public static final int ITEM_KOD = 0, ITEM_NAME = 1, PRICE1 = 2, PRICE2 = 3, COMMENT = 4, COUNT = 5;
	private String date;
	private ArrayList<String[]> table;
	//private String[][] table;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	PriceTRT(String kod, DataBase sqdb) {
		
		kodTRT = kod;
		this.sqdb = sqdb;
		
		Calendar ccc = Calendar.getInstance();
        date = formatter.format(ccc.getTime());
		
		newline();
		 
        Log.d(TAG, "date = " + date);
        
        setTableData();
	}
		
	PriceTRT(DataBase sqdb) {
		
		this.sqdb = sqdb;
		Calendar ccc = Calendar.getInstance();
        date = formatter.format(ccc.getTime());
	}
	
	private void setTableData() {
		
		//String query = "select kodItem, priceType, price "
		String query = "select kodItem, price1, price2, comment "
				+ "from itemsPriceTRT as itemsPriceTRT "
				+ "where date = ? "
				+ "and kodTRT = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { date, kodTRT });
		
		Log.d(TAG, "itemsPriceTRT - " + c.getCount());
		
		table = new ArrayList<String[]>();
		//table = new String[c.getCount()][3];
		//int n = 0;
		
		DecimalFormat precision = new DecimalFormat("0.00");
		
		if (c.moveToFirst()) {
			do {
				
				
				//String price = precision.format(c.getDouble(2));
				
				String[] data = new String[COUNT];
				
				data[ITEM_KOD]	= c.getString(0);
				data[PRICE1] 	= precision.format(c.getDouble(1));
				data[PRICE2]	= precision.format(c.getDouble(2));
				data[COMMENT]	= c.getString(3);
				
				table.add(data);
				
				/*table[n][0] = c.getString(0);
				table[n][1] = c.getString(1);
				table[n++][2] = price;*/
				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	public NewData addline() {
		
		newline.add = true;
		return newline;
	}
	
	public void newline() {
		
		//String[] columnNames = tableColumnNames();
		//newline = new String[columnNames.length];
	}
	
	/*private String[] tableColumnNames() {
		
		String[] names = new String[4];
		
		names[0] = "kodItem";
		names[1] = "priceType";
		names[2] = "price";
		names[3] = "nameItem";
		
		return names;
	}*/

	/*public String[][] getTableData() {
		
		return table;
	}*/
	
	/*public void setLineData(int column, String data) {
		
		//newline[column] = data;
		
	}
	
	public void newLineSetPrice(String data) {
		
		//newline[TABLE_PRICE_TYPE] = data;
		
	}*/
	
	public void save() {
        
		if (!newline.add) {return;}
		
		newline.add = false;
		
        /*String kod			= newline.itemKod;
		String priceType	= newline.priceType;
		String price		= newline.price1;*/
        
		Log.d(TAG, "ITEM_KOD = "+newline.itemKod);
		Log.d(TAG, "PRICE1 = "	+newline.price1);
		Log.d(TAG, "PRICE2 = "	+newline.price2);
		Log.d(TAG, "COMMENT = "	+newline.comment);
        
        sqdb.deletePriceTRT(date, kodTRT, newline.itemKod);
        sqdb.savePriceTRT(date, kodTRT, newline.itemKod, newline.price1, newline.price2, newline.comment);
        
        setTableData();
	}
	
	public JSONArray getPriceTRTListToday(String merch_id) {
		
		//getDate();
		
		String query = "select date, kodTRT, kodItem, price1, price2, comment "
				+ "from itemsPriceTRT as itemsPriceTRT "
				+ "where date = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { date });
		
		Log.d(TAG, "TRT prices - " + c.getCount());
		
		JSONArray jsonArr = new JSONArray();
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					JSONObject pnObj = new JSONObject();
					try {
						
						pnObj.put("date",		c.getString(0));
						pnObj.put("kodTRT",		c.getString(1));
						pnObj.put("kodItem",	c.getString(2));
						pnObj.put("price1",		c.getString(3));
						pnObj.put("price2",		c.getString(4));
						pnObj.put("merch_id",	merch_id);
						pnObj.put("comment",	c.getString(5));
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					jsonArr.put(pnObj);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return jsonArr;
	}
	
	
	public class NewData {

		public boolean add = false;
		public String itemKod, itemName, price1, price2, comment;
		
	}
	
}
