package com.intrist.agent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

public class Order {

	public static final int MAIN_SELL = 0, MAIN_ORDER = 1, MAIN_COMMENT = 2, MAIN_CLOSE = 3, MAIN_COUNT = 4;
	public static final int KOD = 0, NAME = 1, VALUE = 2;
	public static final int DATE = 0, NUMBER = 1, DATE_SALE = 2, KONTR = 3, TRT = 4, STOCK = 5, 
			P_TYPE = 6, OPER = 7, COMMENT = 8, SUM = 9, COUNT = 10;
	public static final int TABLE_KOD=0, TABLE_ITEM=1, TABLE_AMOUNT=2, TABLE_PRICE=3, TABLE_SUM=4, TABLE_COUNT=5;
	private static final String TAG = "myLogs";
	private DataBase sqdb;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	public ArrayList<String[]> list, table;
	public String[] listLine, table_line;
	public String[][] head;
	private ListData mListData;
	public String[] mainList;
	
	Order(DataBase sqdb, ListData mListData) {
		
		this.sqdb = sqdb;
		this.mListData = mListData;
		
		list = new ArrayList<String[]>();
		head = new String[COUNT][2];
		
		//formatter = new SimpleDateFormat("yyyyMMdd");
		
		setMainList();
	}
	
	Order(DataBase sqdb) {
		
		this.sqdb = sqdb;
	}
	
	private void setMainList() {
		
		mainList = new String[MAIN_COUNT];
		
		mainList[MAIN_SELL]		= "Условия продаж";
		mainList[MAIN_ORDER]	= "Текущий заказ";
		mainList[MAIN_COMMENT]	= "Комментарий";
		mainList[MAIN_CLOSE]	= "Закрыть заказ";
	}
	
	public void setOrderList() {
		
		Calendar ccc = Calendar.getInstance();
		
		String[] conditions = new String[] { formatter.format(ccc.getTime())};
		
		getOrderList("", conditions);
	}
	
	public void setOrderList(String kodTRT) {
		
		Calendar ccc = Calendar.getInstance();
		
		String add = " and trt = ?";
		
		String[] conditions = new String[] { formatter.format(ccc.getTime()), kodTRT };
		
		getOrderList(add, conditions);
	}
	
	private void getOrderList(String add, String[] conditions) {
		
		list = new ArrayList<String[]>();
		
		DecimalFormat precision = new DecimalFormat("0.00");
		//String newSSum = precision.format(newSum).replace(',', '.');
		
		String query = "select _id, trt, sum "
				+ "from saloutH as saloutH "
				+ "where date = ?" + add;
		
		Cursor c = sqdb.getSQLData(query, conditions);
		
		Log.d(TAG, "orders - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[COUNT];
					
					newData[NUMBER]	= c.getString(0);
					newData[TRT]	= c.getString(1);
					
					newData[SUM] 	= String.format("%.2f", c.getDouble(2)).replace(',', '.');
					
					list.add(newData);
					
				} while (c.moveToNext());
			}
		}
		c.close();
	}
	
	public void setListLine(int index) {
		
		listLine = list.get(index);
	}

	public void newOrder() {
		
		list = new ArrayList<String[]>();
		head = new String[COUNT][2];
		table = new ArrayList<String[]>();
	}
	
	public void setOrder(String number) {
		
		//head[NUMBER][KOD] = number;
		
		setHead(number);
		setTable(number);	
	}
	
	private void setHead(String number) {
		
		head = new String[COUNT][2];
		
		head[NUMBER][KOD]	= number;
		
		//Map<String, String> m = new HashMap<String, String>();
		
		/*String query = "select kontragent, priceType, operation, comm, dateSale, sum, stock "
				+ "from saloutH as saloutH "
				+ "where _id = ?";*/
		
		String query = "select kontragent, kontr.name as kontrName, priceType, priceT.name as priceName, "
				+ "stock, stock1.name as stockName, operation, comm, dateSale, sum "
				+ "from saloutH as saloutH "
				+ "left join kontragent as kontr "
				+ "on saloutH.kontragent = kontr.kod "
				+ "left join priceType as priceT "
				+ "on saloutH.priceType = priceT.kod "
				+ "left join stock as stock1 "
				+ "on saloutH.stock = stock1.kod "
				+ "where saloutH._id = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { number });
		
		Log.d(TAG, "orders = " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					head[KONTR]		[KOD]	= c.getString(0);
					head[KONTR]		[NAME]	= c.getString(1);
					head[P_TYPE]	[KOD]	= c.getString(2);
					head[P_TYPE]	[NAME]	= c.getString(3);
					head[STOCK]		[KOD]	= c.getString(4);
					head[STOCK]		[NAME]	= c.getString(5);
					head[OPER]		[KOD]	= c.getString(6);
					head[OPER]		[NAME]	= ListData.getSellOper(head[OPER][KOD]);
					head[COMMENT]	[KOD]	= "comment";
					head[COMMENT]	[NAME]	= c.getString(7);
					//head[DATE_SALE]	[KOD]	= "date";
					head[DATE_SALE]	[KOD]	= c.getString(8);
					
					String date = c.getString(8);
					String date1 = date.substring(6, 8)+"."+date.substring(4, 6)+"."+date.substring(0, 4);
					
					head[DATE_SALE]	[NAME]	= date1;
					
					Log.d(TAG, "KONTR		= " + head[KONTR]		[NAME]);
					Log.d(TAG, "PRICE_TYPE	= " + head[P_TYPE]		[NAME]);
					Log.d(TAG, "OPER		= "	+ head[OPER]		[NAME]);
					Log.d(TAG, "COMMENT		= " + head[COMMENT]		[NAME]);
					Log.d(TAG, "STOCK		= " + head[STOCK]		[KOD]);
					Log.d(TAG, "STOCK		= " + head[STOCK]		[NAME]);
					Log.d(TAG, "DATE_SALE	= " + head[DATE_SALE]	[KOD]);
					Log.d(TAG, "DATE_SALE	= " + head[DATE_SALE]	[NAME]);
					
					if (head[STOCK][KOD].equals("ВсеСклады")) {
						head[STOCK][NAME] = "Все склады";
					}
					
				} while (c.moveToNext());
			}
		}
		c.close();
	}
	
	private void setTable(String number) {
		
		table = new ArrayList<String[]>();
		
		String query = "select kodItem, nameItem, amount, price, sum "
				+ "from saloutT as saloutT "
				+ "where headId = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { number });
		
		Log.d(TAG, "orders tables = " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[TABLE_COUNT];
					
					newData[TABLE_KOD]		= c.getString(0);
					newData[TABLE_ITEM]		= c.getString(1);
					newData[TABLE_AMOUNT]	= c.getString(2);
					newData[TABLE_PRICE]	= c.getString(3);
					newData[TABLE_SUM]		= c.getString(4);
					
					table.add(newData);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		//return m;
	}

	public void addTableLine() {
		
		String[] newData = table_line;
		
		int amount = Integer.parseInt(table_line[TABLE_AMOUNT]);
		if (amount <= 0) {
			return;
		}
		table.add(newData);
	}
	
	public Double getOrdersSum(String kodTRT) {
		
		Double sum = 0.0;
		Calendar ccc = Calendar.getInstance();
		
		String query = "select SUM(sum) "
				+ "from saloutH as saloutH "
				+ "where date = ?"
				+ (kodTRT.equals("")? "": " and trt = ?");
		
		String[] condition;
		
		if (kodTRT.equals("")) {
			condition = new String[] { formatter.format(ccc.getTime()) };
		} else {
			condition = new String[] { formatter.format(ccc.getTime()), kodTRT };
		}
		Cursor c = sqdb.getSQLData(query, condition);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					sum = c.getDouble(0);
				} while (c.moveToNext());
			}
		}
		c.close();
		return sum;	
	}
	
	public Double getOrderSum() {
		
		Double sum = 0.0;
		for (String[] data : table) {
			String sSum = data[TABLE_SUM];
			sum = sum + Double.parseDouble(sSum);
		}
		return sum;	
	}
	
	public void setSettings(String[][] settings) {
		
		head[DATE_SALE]	[KOD] = settings[DATE_SALE]	[KOD];
		head[KONTR]		[KOD] = settings[KONTR]		[KOD];
		head[OPER]		[KOD] = settings[OPER]		[KOD];
		head[STOCK]		[KOD] = settings[STOCK]		[KOD];        
		head[COMMENT]	[NAME] = settings[COMMENT]	[NAME];
		
		//for (int i = 0; i < settings.length; i++) {
			
			/*int column = mListData.getSettingsColumn(i);
			
			if (column > 0) {
				try {
					head[column][KOD] = settings[i][VALUE];
					head[column][NAME] = settings[i][NAME];
				} catch (Exception e) {
					// TODO: handle exception
				}
			}*/
		//}
	}
	
	public void save(String kodTRT) {
		
		if (table.size()==0) {return;}
		
		Calendar ccc = Calendar.getInstance();
		int date = Integer.parseInt(formatter.format(ccc.getTime()));
		
		Double sum = getOrderSum();
		
		Log.d(TAG, "номер        = "+head[NUMBER]		[KOD]);
		Log.d(TAG, "ДатаОтгрузки = "+head[DATE_SALE]	[KOD]);
		Log.d(TAG, "Контрагент   = "+head[KONTR]		[KOD]);
		Log.d(TAG, "ТипЦены      = "+head[P_TYPE]		[KOD]);
		Log.d(TAG, "ВидОперации  = "+head[OPER]			[KOD]);
		Log.d(TAG, "Склад        = "+head[STOCK]		[KOD]);        
		Log.d(TAG, "Комментарий  = "+head[COMMENT]		[NAME]);
		//Log.d(TAG, "сумма = "		+head[SUM]			[KOD]);
		Log.d(TAG, "сумма        = "		+sum);
		
		
		//String total = ((TextView) findViewById(R.id.total)).getText().toString();
		//Double суммаИтог = Double.parseDouble(total);
		
		//int zakazId = 0;
		String number = head[NUMBER][KOD];
		
		if (number==null) {
			int zakazId = sqdb.saveZakazHeadData(kodTRT, head, date, sum);
			number = Integer.toString(zakazId);
			//if (zakazId < 0) return false;
		} else {
			//zakazId = number;
			sqdb.updateDocData(head, sum, number);
			sqdb.deleteDocTable(number);
		}
		sqdb.saveZakazTableData(Integer.parseInt(number), table);
		
	}
	
	public JSONArray getOrdersListToday(String merch_id) {
		
		Calendar ccc = Calendar.getInstance();
        String дата = formatter.format(ccc.getTime());
		
		String query = "select saloutH._id, saloutH.date, saloutH.kontragent, saloutH.priceType, "
				+ "saloutH.operation, saloutH.comm, saloutH.dateSale, saloutH.sum, "
				+ "saloutT.nameItem, saloutT.kodItem, saloutT.amount, saloutT.price, saloutT.sum, "
				+ "items.prodcode, TRT.ol_id, saloutH.stock "
				+ "from saloutH as saloutH "
				+ "left join saloutT as saloutT "
				+ "on saloutH._id = saloutT.headId "
				+ "left join items as items "
				+ "on saloutT.kodItem = items.kod "
				+ "left join TRT as TRT "
				+ "on saloutH.trt = TRT.kod "
				+ "where saloutH.date = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { дата });
		
		Log.d(TAG, "Строк заказов - " + c.getCount());
		
		JSONArray jsonArr = new JSONArray();
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					JSONObject pnObj = new JSONObject();
					try {
						
						pnObj.put("_id",		c.getString(0));
						pnObj.put("date",		c.getString(1));
						pnObj.put("kontragent",	c.getString(2));
						pnObj.put("priceType",	c.getString(3));
						pnObj.put("operation",	c.getString(4));
						pnObj.put("comm",		c.getString(5));
						pnObj.put("dateSale",	c.getString(6));
						pnObj.put("sumDok",		c.getString(7));
						pnObj.put("nameItem",	c.getString(8));
						pnObj.put("kodItem",	c.getString(9));
						pnObj.put("amount",		c.getString(10));
						pnObj.put("price",		c.getString(11));
						pnObj.put("sum",		c.getString(12));
						//pnObj.put("agent",		agent.get("kod"));
						pnObj.put("merch_id",	merch_id);
						pnObj.put("prodcode",	c.getString(13));
						pnObj.put("ol_id",		c.getString(14));
						pnObj.put("stock",		c.getString(15));
						
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
	
}
