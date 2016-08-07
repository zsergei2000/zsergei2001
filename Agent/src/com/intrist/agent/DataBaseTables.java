package com.intrist.agent;

public class DataBaseTables {

	public static final int AGENTS = 0, CURRENT_AGENT = 1, TRT_ITEMS_PRICE = 2, TRT_ADDRESS = 3, SALES_DATA = 4, REPORTS = 5;
	
	public static String[] getAllTables() {
		
		String[] tables = new String[6];
		
		tables[0] = "agents";
		tables[1] = "currentAgent";
		tables[2] = "itemsPriceTRT";
		tables[3] = "trtAddress";
		tables[4] = "salesData";
		tables[5] = "reports";
		
		return tables;
	}
	
	public static String getTableQuery(int key, String name) {
		
		String[] table = getTable(key);
		
		String tableStr = "create table " + name + " (";
		for (int i = 0; i < table.length; i++) {
			
			tableStr = tableStr + table[i] + ", ";
		}
		
		tableStr = tableStr.substring(0, (tableStr.length() - 2));
		tableStr = tableStr + ");";
		
		return tableStr;
	}
	
	private static String[] getTable(int key) {
		
		switch (key) {
		case AGENTS:
			return getAgents();
		case CURRENT_AGENT:
			return getCurrentAgent();
		case TRT_ITEMS_PRICE:
			return getItemsPriceTRT();
		case TRT_ADDRESS:
			return getTrtAdress();
		case SALES_DATA:
			return getSalesData();
		case REPORTS:
			return getReports();
		default:
			return new String[0];
		}
		
	}
	
	private static String[] getAgents() {
		
		String[] array = new String[6];
		
		array[0] = "_id integer primary key autoincrement";
		array[1] = "kod text";
		array[2] = "name text";
		array[3] = "merch_id text";
		array[4] = "hierarchy text";
		array[5] = "role text";
		
		return array;
	}

	private static String[] getCurrentAgent() {
	
		String[] array = new String[5];
		
		array[0] = "_id integer primary key autoincrement";
		array[1] = "kod text";
		array[2] = "name text";
		array[3] = "merch_id text";
		array[4] = "hierarchy text";
		
		return array;
	}
	
	private static String[] getItemsPriceTRT() {
		
		String[] array = new String[7];
		
		array[0] = "_id integer primary key autoincrement";
		array[1] = "date integer";
		array[2] = "kodTRT text";
		array[3] = "kodItem text";
		//array[5] = "priceType text";
		array[4] = "price1 Double";
		array[5] = "price2 Double";
		array[6] = "comment text";
		
		return array;
	}
	
	private static String[] getTrtAdress() {
		
		String[] array = new String[9];
		
		array[0] = "_id integer primary key autoincrement";
		array[1] = "kodTRT text";
		array[2] = "rayon text";
		array[3] = "punkt text";
		array[4] = "street text";
		array[5] = "number text";
		array[6] = "type text";
		array[7] = "lotok text";
		array[8] = "comment text";
		
		return array;
	}
	
	private static String[] getSalesData() {
		
		String[] array = new String[6];
		
		array[0] = "_id integer primary key autoincrement";
		array[1] = "object text";
		array[2] = "lastMF text";
		array[3] = "lastMP text";
		array[4] = "diferent text";
		array[5] = "today text";
		
		return array;
	}
	
	private static String[] getReports() {
		
		String[] array = new String[5];
		
		array[0] = "_id integer primary key autoincrement";
		array[1] = "report text";
		array[2] = "column text";
		array[3] = "move text";
		array[4] = "value text";
		
		return array;
	}
	
}
