package com.intrist.agent;

import android.database.Cursor;
import android.util.Log;

public class User {

	public static final int HIERARCHY_0 = 0, HIERARCHY_1 = 1, HIERARCHY_2 = 2, HIERARCHY_3 = 3;
	public static final int ROLE_NO = 0, ROLE_ADMIN = 1, ROLE_USER = 2, ROLE_PRICE = 3, ROLE_REPORT = 4;
	private static final String TAG = "myLogs";
	public String kod, name, merchId;
	public int hierarchy, role;
	
	User(DataBase sqdb) {
		
		kod		= "";
		name	= "";
		role	= 0;
		
		setUser(sqdb);
	}
	
	private void setUser(DataBase sqdb) {
		
		String query = "select kod, name, merch_id, hierarchy, role "
				+ "from agents as agents";
		
		Cursor c = null;
		
		//start
		
		c = sqdb.sqdb.rawQuery(query, null);
		
		//c = sqdb.getSQLData(query, null);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					kod			= c.getString(0);
					name		= c.getString(1);
					merchId		= c.getString(2);
					hierarchy	= Integer.parseInt(c.getString(3))-1;
					role		= c.getInt(4);
					
					Log.d(TAG, "agent kod = "+kod+", name = "+name+", hierarchy = "+hierarchy+", role = "+role);
					
				} while (c.moveToNext());
			}
		}
		c.close();
	}
	
}
