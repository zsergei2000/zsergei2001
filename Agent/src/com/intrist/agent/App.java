package com.intrist.agent;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class App extends Application {

	private static final int DB_VERSION = 13;
	private static Context mContext;
	//public static DataBase sqdb;
	public static DBHelper dbh;
	public Handler h;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		//super.onCreate();
	}
	
	/*public void onCreate(){
		super.onCreate();
		
		h = new Handler() {@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}};
		
		//h.
		
		dbh = new DBHelper(this, "myDB", null, DB_VERSION);
		
		//sqdb = new DataBase(this);
		//sqdb.OpenDB();
		
		this.mContext = this;
	}*/

	public static Context getContext(){
		return mContext;
	}
	
}
