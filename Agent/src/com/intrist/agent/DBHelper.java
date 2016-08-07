package com.intrist.agent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {

	private static DBHelper sInstance;
		
	private static final String DATABASE_NAME = "myDB";
	private static final int DATABASE_VERSION = 13;
	private static final String TAG = "myLogs";
	
	public static synchronized DBHelper getInstance(Context context) {
		
		if (sInstance == null) {
			Log.d(TAG, "sInstance == null");
			sInstance = new DBHelper(context.getApplicationContext());
		}
		return sInstance;
	}
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqdb) {

		sqdb.beginTransaction();
		try {
			
			sqdb.execSQL("create table TRT ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text, "
					+ "ol_id text, channel text);");

			/*sqdb.execSQL("create table Agents ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text, "
					+ "merch_id text, hierarchy text);");
			
			sqdb.execSQL("create table currentAgent ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text, "
					+ "merch_id text, hierarchy text);");*/
			
			sqdb.execSQL("create table AgentTRT ("
					+ "_id integer primary key autoincrement, "
					+ "kodAgent text, kodTRT text);");
			
			sqdb.execSQL("create table items ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text, "
					+ "thisGroup boolean, parentKod text, "
					+ "prodcode text, kodManuf text, "
					+ "k1 text, k2 text, k3 text);");
			
			sqdb.execSQL("create table priceType ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text);");
			
			sqdb.execSQL("create table priceTypeTRT ("
					+ "_id integer primary key autoincrement, "
					+ "kodTRT text, kodManuf text, kodPrice text);");
			
			sqdb.execSQL("create table itemsPrice ("
					+ "_id integer primary key autoincrement, "
					+ "kodItem text, kodPrice text, "
					+ "price double);");
			
			sqdb.execSQL("create table route ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, day integer, "
					+ "kodTRT text, kodAgent text, "
					+ "number integer);");
			
			sqdb.execSQL("create table saloutT ("
					+ "_id integer primary key autoincrement, "
					+ "headId integer, nameItem text, "
					+ "kodItem text, amount integer, "
					+ "price Double, sum Double);");
			
			sqdb.execSQL("create table saloutH ("
					+ "_id integer primary key autoincrement, "
					+ "trt text, "
					+ "date integer, kontragent text, "
					+ "priceType text, operation text, "
					+ "comm text, dateSale integer, "
					+ "stock text, sum Double);");
			
			sqdb.execSQL("create table kontragent ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text);");
			
			sqdb.execSQL("create table kontragentTRT ("
					+ "_id integer primary key autoincrement, "
					+ "kodTRT text, kodKontr text);");
			
			sqdb.execSQL("create table currentOrders ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, zakaz text);");
			
			sqdb.execSQL("create table stock ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, name text);");
			
			sqdb.execSQL("create table stockAgent ("
					+ "_id integer primary key autoincrement, "
					+ "kodStock text, kodAgent text);");
			
			sqdb.execSQL("create table trtSetting ("
					+ "_id integer primary key autoincrement, "
					+ "kodTRT text, date text, "
					+ "kontr text, price text, "
					+ "oper text, stock text, "
					+ "comm text);");
			
			sqdb.execSQL("create table balance ("
					+ "_id integer primary key autoincrement, "
					+ "kod text, rest text, "
					+ "stock text);");
			
			sqdb.execSQL("create table dateServer ("
					+ "date text);");
			
			sqdb.execSQL("create table channelPrice ("
					+ "_id integer primary key autoincrement, "
					+ "kodItem text, channel text, "
					+ "kodPrice text);");
			
			/*sqdb.execSQL("create table itemsPriceTRT ("
					+ "_id integer primary key autoincrement, "
					+ "date integer, kodTRT text, kodItem text, "
					+ "priceType text, price Double);");*/
			
			String[] tables = DataBaseTables.getAllTables();
			
			for (int i = 0; i < tables.length; i++) {
				
				String table = DataBaseTables.getTableQuery(i, tables[i]);
				
				Log.d(TAG, table);
				
				sqdb.execSQL(table);
			}
			
			sqdb.setTransactionSuccessful();
			Log.d(TAG, "SQL database's are created");
		} catch (Exception e) {
			Log.d(TAG, "SQL database doesn't created");
		} finally {
			sqdb.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqdb, int oldVersion, int newVersion) {
		
		if (oldVersion == 12 & newVersion == 13) {
			
			sqdb.beginTransaction();
			Log.d(TAG, "begin transaction");
			try {
				
				sqdb.execSQL("create table dateServer ("
						+ "date text);");
				
				Log.d(TAG, "Transaction successful");
				
				sqdb.setTransactionSuccessful();
			} finally {
				sqdb.endTransaction();
			}
		}
	}
}
