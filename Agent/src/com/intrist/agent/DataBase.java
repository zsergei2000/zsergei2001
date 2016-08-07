package com.intrist.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {

	public static final int KOD = 0, NAME = 1, VALUE = 2;
	private static final String TAG = "myLogs";
	private Context mCtx;
	public DBHelper dbh;
	public SQLiteDatabase sqdb;
	
	public DataBase(Context Ctx){
		mCtx = Ctx;
	}
	
	public void OpenDB() {
		
		dbh = DBHelper.getInstance(mCtx);
		sqdb = dbh.getWritableDatabase();
	}
	
	public void closeDB() {
		if (dbh != null) dbh.close();
	}
	
	public Cursor getSQLData(String sqlQuery, String[] Condition){
		return sqdb.rawQuery(sqlQuery, Condition);
	}
	
	public void clearTable(String table){
		int count = sqdb.delete(table, null, null);
		Log.d(TAG, "delete " + count + " rows, table - " + table);
	}
	
	public int saveZakazHeadData(String kodTRT, String[][] table, int date, Double sum) {
        
		int _id = получитьПоследнийЗаказ();
		
        ContentValues cv = new ContentValues();
        int zakazId = -1;
        
		sqdb.beginTransaction();
		Log.d(TAG, "Сохранение заказа.");
		try {
			
			cv.clear();
			
			cv.put("_id",		_id);
			
			cv.put("trt",		kodTRT);
			cv.put("date",		date);
			cv.put("dateSale",	table[Order.DATE_SALE]	[KOD]);
			cv.put("kontragent",table[Order.KONTR]		[KOD]);
			cv.put("priceType",	table[Order.P_TYPE]		[KOD]);
			cv.put("operation",	table[Order.OPER]		[KOD]);
			cv.put("stock",		table[Order.STOCK]		[KOD]);
			cv.put("comm",		table[Order.COMMENT]	[NAME]);			
			cv.put("sum",		sum);
			
			zakazId = (int) sqdb.insert("saloutH", null, cv);

			sqdb.setTransactionSuccessful();
			Log.d(TAG, "writing to database Ok.");
		} catch (Exception e) {
			Log.d(TAG, "writing to database failed!!!");
		} finally {
			sqdb.endTransaction();
		}
		return zakazId;
	}
	
	private int получитьПоследнийЗаказ() {
		
		int номерЗаказа = 1;
		int номерЗаказаА = 1;
		int номерЗаказа1С = 1;
		
		String query = "select MAX(_id) "
				+ "from saloutH as saloutH";
		
		Cursor c = getSQLData(query, null );
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					номерЗаказаА = c.getInt(0);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		Map<String, String> agent = получитьТекущегоАгента();
		
		query = "select zakaz "
				+ "from currentOrders as currentOrders "
				+ "where kod = ?";
		
		c = getSQLData(query, new String[] { agent.get("kod")});
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					номерЗаказа1С = c.getInt(0);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		if (номерЗаказаА>номерЗаказа1С) {
			номерЗаказа = номерЗаказаА;
		} else {
			номерЗаказа = номерЗаказа1С;
		}
		
		Log.d(TAG, "Последний заказ = "+номерЗаказа);
		
		номерЗаказа++;
		
		return номерЗаказа;
		
	}
	
	public int updateDocData(String[][] table, Double sum, String number) {
		
		ContentValues cv = new ContentValues();
		
		cv.put("dateSale",	table[Order.DATE_SALE]	[KOD]);
		cv.put("kontragent",table[Order.KONTR]		[KOD]);
		cv.put("priceType",	table[Order.P_TYPE]		[KOD]);
		cv.put("operation",	table[Order.OPER]		[KOD]);
		cv.put("comm",		table[Order.COMMENT]	[NAME]);
		cv.put("stock",		table[Order.STOCK]		[KOD]);
		cv.put("sum",		sum);
		
		sqdb.update("saloutH", cv, "_id=?", new String[] { number });
		return 0;
	}
	
	private Map<String, String> получитьТекущегоАгента() {
		
		String query = "select * "
				+ "from agents as agents";
		
		Cursor cAgent = getSQLData(query, null);
		Map<String, String> find = null;
		if (cAgent!=null) {
			if (cAgent.moveToFirst()) {
				do {
					find = new HashMap<String, String>();
					find.put("kod", cAgent.getString(1));
					find.put("name", cAgent.getString(2));
					find.put("merch_id", cAgent.getString(3));					
					Log.d(TAG, "Текущий агент - "+ cAgent.getString(2));
					
				} while (cAgent.moveToNext());
			}
		}
		cAgent.close();
		
		return find;
		
	}
	
	public int saveZakazTableData(int zakazId, ArrayList<String[]> table) {
		
        ContentValues cv = new ContentValues();
        int insertCount = -1;
        
		sqdb.beginTransaction();
		Log.d(TAG, "save order table");
		try {

			for (String[] data : table) {

				Log.d(TAG, "headId = "		+Integer.toString(zakazId));
				Log.d(TAG, "kodItem = "		+data[Order.TABLE_KOD]);
				Log.d(TAG, "nameItem = "	+data[Order.TABLE_ITEM]);
				Log.d(TAG, "amount = "		+data[Order.TABLE_AMOUNT]);
				Log.d(TAG, "price = "		+data[Order.TABLE_PRICE]);
				Log.d(TAG, "sum = "			+data[Order.TABLE_SUM]);
				
				cv.clear();
				cv.put("headId",	zakazId);
				cv.put("kodItem",	data[Order.TABLE_KOD]);
				cv.put("nameItem",	data[Order.TABLE_ITEM]);
				cv.put("amount",	Integer.parseInt(data[Order.TABLE_AMOUNT]));
				cv.put("price",		Double.parseDouble(data[Order.TABLE_PRICE]));
				cv.put("sum",		Double.parseDouble(data[Order.TABLE_SUM]));
				
				/*cv.put("kodItem",	m.get("kodItem").toString());
				cv.put("nameItem",	m.get("nameItem").toString());
				cv.put("amount",	Integer.parseInt(m.get("amount")));
				cv.put("price",		Double.parseDouble(m.get("price")));
				cv.put("sum",		Double.parseDouble(m.get("sum")));*/
				
				insertCount = (int) sqdb.insert("saloutT", null, cv);

			}
			sqdb.setTransactionSuccessful();
			Log.d(TAG, "writing to database Ok.");
		} catch (Exception e) {
			Log.d(TAG, "writing to database failed!!!");
		} finally {
			sqdb.endTransaction();
		}
		return insertCount;
	}
	
	public void deleteDoc(String condition) {
		
		sqdb.delete("saloutH", "_id=?", new String[] { condition });
	}
	
	public void deleteDocTable(String condition) {
		
		sqdb.delete("saloutT", "headId=?", new String[] { condition });
	}
	
	public void deletePriceTRT(String date, String kodTRT, String kodItem) {
		
		String[] args = new String[] { date, kodTRT, kodItem };
		//String[] args = new String[] { date, kodTRT, kodItem, priceType };
		sqdb.delete("itemsPriceTRT", "date=? and kodTRT=? and kodItem=?", args);
		//sqdb.delete("itemsPriceTRT", "date=? and kodTRT=? and kodItem=? and priceType=?", args);
	}
	
	public int savePriceTRT(String date, String kodTRT, String kodItem, String price1, String price2, String comment) {
        
		int dateInt = Integer.parseInt(date);
		
        ContentValues cv = new ContentValues();
        int zakazId = -1;
        
		sqdb.beginTransaction();
		Log.d(TAG, "Сохранение заказа.");
		try {
			
			cv.clear();
			
			cv.put("date",		dateInt);
			cv.put("kodTRT",	kodTRT);
			cv.put("kodItem",	kodItem);
			cv.put("price1",	price1);
			cv.put("price2",	price2);
			cv.put("comment",	comment);
			
			zakazId = (int) sqdb.insert("itemsPriceTRT", null, cv);

			sqdb.setTransactionSuccessful();
			Log.d(TAG, "writing to database Ok.");
		} catch (Exception e) {
			Log.d(TAG, "writing to database failed!!!");
		} finally {
			sqdb.endTransaction();
		}
		return zakazId;
	}
	
	public int createTRTSettings(String kod) {
		
        ContentValues cv = new ContentValues();
        
		sqdb.beginTransaction();
		try {
			
			cv.put("kodTRT",	kod);
			cv.put("date",		"");
			cv.put("kontr",		"");
			cv.put("price",		"");
			cv.put("oper",		"");
			cv.put("comm",		"");
			
			sqdb.insert("trtSetting", null, cv);

			sqdb.setTransactionSuccessful();
			Log.d(TAG, "writing to database Ok.");
		} catch (Exception e) {
			Log.d(TAG, "writing to database failed!!!");
		} finally {
			sqdb.endTransaction();
		}
		return 0;
	}
	
	public int updateTRTData(String поле, String значение, String трт) {
		
		ContentValues cv = new ContentValues();
		cv.put(поле,	значение);
		sqdb.update("trtSetting", cv, "kodTRT=?", new String[] { трт });
		return 0;
	}
	
	public int createTRTAddress(String kod) {
		
        ContentValues cv = new ContentValues();
        
		sqdb.beginTransaction();
		try {
			
			cv.put("kodTRT",	kod);
			cv.put("rayon",		"");
			cv.put("punkt",		"");
			cv.put("street",	"");
			cv.put("number",	"");
			cv.put("type",		"");
			cv.put("lotok",		"");
			cv.put("comment",	"");
			
			sqdb.insert("trtAddress", null, cv);

			sqdb.setTransactionSuccessful();
			Log.d(TAG, "writing to database Ok.");
		} catch (Exception e) {
			Log.d(TAG, "writing to database failed!!!");
		} finally {
			sqdb.endTransaction();
		}
		return 0;
	}
	
	public int updateTRTAddressData(String поле, String значение, String трт) {
		
		ContentValues cv = new ContentValues();
		cv.put(поле, значение);
		sqdb.update("trtAddress", cv, "kodTRT=?", new String[] { трт });
		return 0;
	}
	
	public void addData(String table, ArrayList<Map<String, String>> data) {

		Runtime.getRuntime().gc();
		sqdb.beginTransaction();
		try {
			if (table.equals("TRT")) {
				writeTRT(table, data);
			} else if (table.equals("agents")) {
				writeAgents(table, data);
			} else if (table.equals("AgentTRT")) {
				writeAgentTRT(table, data);
			} else if (table.equals("currentAgent")) {
				writeAgents(table, data);
			} else if (table.equals("items")) {
				writeItems(table, data);
			} else if (table.equals("priceType")) {
				writeSuperData(table, data);
			} else if (table.equals("priceTypeTRT")) {
				writePriceTypeTRT(table, data);
			} else if (table.equals("itemsPrice")) {
				writeItemsPrice(table, data);
			} else if (table.equals("route")) {
				writeRoute(table, data);
			} else if (table.equals("kontragent")) {
				writeSuperData(table, data);
			} else if (table.equals("kontragentTRT")) {
				writeKontragentTRT(table, data);
			} else if (table.equals("currentOrders")) {
				writeСurrentOrders(table, data);
			} else if (table.equals("stock")) {
				writeSuperData(table, data);
			} else if (table.equals("stockAgent")) {
				writeStockAgent(table, data);
			} else if (table.equals("balance")) {
				writeBalance(table, data);
			} else if (table.equals("salesData")) {
				writeSalesData(table, data);
			} else if (table.equals("reports")) {
				writeReports(table, data);
			}
			
			sqdb.setTransactionSuccessful();
			Log.d(TAG, "writing to data base successful");
			
		} catch (Exception e) {
			Log.e(TAG, "writing to data base failed!!!");
		} finally {
			sqdb.endTransaction();
		}
	}
	
	private void writeTRT(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {

			cv.clear();
			cv.put("kod",	map.get("1").toString());
			cv.put("name",	map.get("2").toString());
			cv.put("ol_id",	map.get("3").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeSuperData(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {

			cv.clear();
			cv.put("kod", map.get("1").toString());
			cv.put("name", map.get("2").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeAgents(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {

			cv.clear();
			cv.put("kod", map.get("1").toString());
			cv.put("name", map.get("2").toString());
			cv.put("merch_id", map.get("3").toString());
			cv.put("hierarchy", map.get("4").toString());
			cv.put("role", map.get("5").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeItems(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kod",		map.get("1").toString());
			cv.put("name",		map.get("2").toString());
			cv.put("thisGroup",	(map.get("3").toString().equals("истина") ? true: false));
			cv.put("parentKod",	map.get("4").toString());
			cv.put("prodcode",	map.get("5").toString());
			cv.put("kodManuf",	map.get("6").toString());
			cv.put("k1",		map.get("7").toString());
			cv.put("k2",		map.get("8").toString());
			cv.put("k3",		map.get("9").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeAgentTRT(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {

			cv.clear();
			cv.put("kodAgent", map.get("1").toString());
			cv.put("kodTRT", map.get("2").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writePriceTypeTRT(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kodTRT",	map.get("1").toString());
			cv.put("kodManuf",	map.get("2").toString());
			cv.put("kodPrice",	map.get("3").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeItemsPrice(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kodItem",	map.get("1").toString());
			cv.put("kodPrice",	map.get("2").toString());
			cv.put("price",		Double.parseDouble(map.get("3").toString()));
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeRoute(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kod",		map.get("1").toString());
			cv.put("day",		Integer.parseInt(map.get("2").toString()));
			cv.put("kodTRT",	map.get("3").toString());
			cv.put("kodAgent",	map.get("4").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeKontragentTRT(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kodTRT",	map.get("1").toString());
			cv.put("kodKontr",	map.get("2").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeСurrentOrders(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kod",	map.get("1").toString());
			cv.put("zakaz",	map.get("2").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeStockAgent(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kodStock",	map.get("1").toString());
			cv.put("kodAgent",	map.get("2").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeBalance(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("kod",	map.get("1").toString());
			cv.put("rest",	map.get("2").toString());
			cv.put("stock",	map.get("3").toString());
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeSalesData(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("object",	map.get("1").toString());
			cv.put("lastMF",	map.get("2").toString());
			cv.put("lastMP",	map.get("3").toString());
			cv.put("diferent",	map.get("4").toString());
			cv.put("today",		map.get("5").toString());
			
			sqdb.insert(table, null, cv);
		}
	}
	
	private void writeReports(String table, ArrayList<Map<String, String>> data) throws IOException {
		
		ContentValues cv = new ContentValues();
		for (Map<String, String> map : data) {
			
			cv.clear();
			cv.put("report",	map.get("1").toString());
			cv.put("column",	map.get("2").toString());
			cv.put("move",		map.get("3").toString());
			cv.put("value",		map.get("4").toString());
			
			
			sqdb.insert(table, null, cv);
		}
	}
	
	public void writeDateServer(String data) throws IOException {
		
		ContentValues cv = new ContentValues();
		
		cv.put("date", data);
		sqdb.insert("dateServer", null, cv);
	}
	
}
