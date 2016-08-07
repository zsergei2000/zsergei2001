package com.intrist.agent;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

public class TRT {

	private static final String TAG = "myLogs";	
	public static final int MAIN_SELL = 0, MAIN_ORDER = 1, MAIN_PRICE = 2, MAIN_ADDR = 3, MAIN_BACK = 4, MAIN_COUNT = 5;
	public static final int SELL_DATE = 0, SELL_KONTR = 1, SELL_PRICE = 2, SELL_OPER = 3, SELL_STOK = 4, SELL_ORDER = 5;
	public static final int ADDR_RAYON = 0, ADDR_PUNKT = 1, ADDR_STREET = 2, ADDR_NUMBER = 3, ADDR_TYPE = 4, 
			ADDR_LOTOK = 5, ADDR_COMMENT = 6, ADDR_COUNT = 7;
	public static final int KOD = 0, NAME = 1;
	public String kod;
	public DataBase sqdb;
	public String[] sellList, mainList;
	public String[][] settings, addrList;
	private int settCount, addrCount;
	
	//public String kontragent = "", priceType = "", operation = "", comment = "", dateSale = "", stock, sum = "0.0";
	//public String[] settings;
	
	TRT() {
		
		//this.sqdb = sqdb;
		//this.kod = kod;
		
		//setNamesettings();
		/*setSettings();
		setMainList();
		setSellList();*/
		//setAddrList();
	}
	
	public void setTRT() {
		
		setSettings();
		//setMainList();
		setSellList();
	}
	
	public static ArrayList<String[]> getList(DataBase sqdb, String kodAgent, int dayOfWeek) {
		
		//Log.d(TAG, "dayOfWeek = " + dayOfWeek);
		
		String query = "select TRT.kod, TRT.name "
				+ "from route as route "
				+ "INNER join TRT as TRT "
				+ "on route.kodTRT = TRT.kod "
				+ "where route.kodAgent = ? " + (dayOfWeek==8? "": "and route.day = "+dayOfWeek)
				+ " order by TRT.name";
		
		//Log.d(TAG, "query = " + query);
		
		Cursor c = sqdb.getSQLData(query, new String[] { kodAgent });
		
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		Log.d(TAG, "trt count = " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[2];
					newData[KOD] = c.getString(0);
					newData[NAME] = c.getString(1);
					
					data.add(newData);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return data;
		
	}
	
	private void setSettings() {
		
		settings = new String[Order.COUNT][2];
		
		String query = "select kontr, kontragent.name as kontrName, price, priceType.name as priceName, "
				+ "trtSetting.stock, stock1.name as stockName, oper, comm "
				+ "from trtSetting as trtSetting "
				+ "left join kontragent as kontragent "
				+ "on trtSetting.kontr = kontragent.kod "
				+ "left join priceType as priceType "
				+ "on trtSetting.price = priceType.kod "
				+ "left join stock as stock1 "
				+ "on trtSetting.stock = stock1.kod "
				+ "where kodTRT = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { kod });
		
		settCount = c.getCount();
		//Log.d(TAG, "trt settings - " + settCount);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					Log.d(TAG, "STOCK = " + c.getString(4));
					
					settings[Order.KONTR]	[KOD]	= c.getString(0);
					settings[Order.KONTR]	[NAME]	= c.getString(1);
					settings[Order.P_TYPE]	[KOD]	= c.getString(2);
					settings[Order.P_TYPE]	[NAME]	= c.getString(3);
					settings[Order.STOCK]	[KOD]	= c.getString(4);
					settings[Order.STOCK]	[NAME]	= c.getString(5);
					settings[Order.OPER]	[KOD]	= c.getString(6);
					settings[Order.OPER]	[NAME]	= ListData.getSellOper(settings[Order.OPER][KOD]);
					//settings[Order.COMMENT]	[KOD]	= "comment";
					//settings[Order.COMMENT]	[NAME]	= c.getString(7);
					
					if (settings[Order.STOCK][KOD]!=null&&settings[Order.STOCK][KOD].equals("ВсеСклады")) {
						settings[Order.STOCK][NAME] = "Все склады";
					}
					
					Log.d(TAG, "KONTR      = " + settings[Order.KONTR]		[NAME]);
					Log.d(TAG, "PRICE_TYPE = " + settings[Order.P_TYPE]	[NAME]);
					Log.d(TAG, "OPER       = "	+ settings[Order.OPER]		[NAME]);
					//Log.d(TAG, "COMMENT		= " + settings[Order.COMMENT]	[NAME]);
					Log.d(TAG, "STOCK      = " + settings[Order.STOCK]		[KOD]);
					//Log.d(TAG, "STOCK		= " + settings[Order.STOCK]		[NAME]);
					
				} while (c.moveToNext());
			}
		}
	}
	
	public static String[] getMainList() {
		
		String[] mainList;
		
		if (MainActivity.user.role==User.ROLE_PRICE) {
			
			mainList = new String[MAIN_COUNT];
			
			mainList[MAIN_PRICE]	= "Установить цену";
			mainList[MAIN_ADDR]		= "Адрес";
			mainList[MAIN_BACK]		= "<- Назад";
			
		} else {
			
			mainList = new String[MAIN_COUNT];
			
			mainList[MAIN_SELL]		= "Условия продаж";
			mainList[MAIN_ORDER]	= "Заказы в ТРТ за сегодня";
			mainList[MAIN_PRICE]	= "Установить цену";
			mainList[MAIN_ADDR]		= "Адрес";
			mainList[MAIN_BACK]		= "<- Назад";
		}
		
		return mainList;
	}
	
	private void setMainList() {
		
		if (MainActivity.user.role==User.ROLE_PRICE) {
			
			mainList = new String[MAIN_COUNT];
			
			mainList[MAIN_PRICE]	= "Установить цену";
			mainList[MAIN_ADDR]		= "Адрес";
			mainList[MAIN_BACK]		= "<- Назад";
			
		} else {
			
			mainList = new String[MAIN_COUNT];
			
			mainList[MAIN_SELL]		= "Условия продаж";
			mainList[MAIN_ORDER]	= "Заказы в ТРТ за сегодня";
			mainList[MAIN_PRICE]	= "Установить цену";
			mainList[MAIN_ADDR]		= "Адрес";
			mainList[MAIN_BACK]		= "<- Назад";
		}
	}
	
	private void setSellList() {
		
		sellList = new String[6];
		
		sellList[SELL_DATE]		= "Дата отгрузки:";
		sellList[SELL_KONTR]	= "Контрагент/Факт:";
		//sellList[SELL_PRICE]	= "Тип цен";
		sellList[SELL_OPER]		= "Вид операции:";
		sellList[SELL_STOK]		= "Склад:";
		sellList[SELL_ORDER]	= "НОВЫЙ ЗАКАЗ";
	}
	
	public void setAddrList() {
		
		addrList = new String[ADDR_COUNT][2];
		
		addrList[ADDR_RAYON]	[KOD]	= "Район:";
		addrList[ADDR_PUNKT]	[KOD]	= "Населенный пункт:";
		addrList[ADDR_STREET]	[KOD]	= "Улица:";
		addrList[ADDR_NUMBER]	[KOD]	= "Номер дома/рынка:";
		addrList[ADDR_TYPE]		[KOD]	= "Тип ТРТ:";
		addrList[ADDR_LOTOK]	[KOD]	= "Лоток номер:";
		addrList[ADDR_COMMENT]	[KOD]	= "Комментарий:";
		
		addrList[ADDR_RAYON]	[NAME]	= "";
		addrList[ADDR_PUNKT]	[NAME]	= "";
		addrList[ADDR_STREET]	[NAME]	= "";
		addrList[ADDR_NUMBER]	[NAME]	= "";
		addrList[ADDR_TYPE]		[NAME]	= "";
		addrList[ADDR_LOTOK]	[NAME]	= "";
		addrList[ADDR_COMMENT]	[NAME]	= "";
		
		getAddrList();
		
	}
	
	private void getAddrList() {
		
		String query = "select rayon, punkt, street, number, type, lotok, comment "
				+ "from trtAddress as trtAddress "
				+ "where kodTRT = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { kod });
		
		addrCount = c.getCount();
		//Log.d(TAG, "trt settings - " + settCount);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					addrList[ADDR_RAYON]	[NAME]	= c.getString(0);
					addrList[ADDR_PUNKT]	[NAME]	= c.getString(1);
					addrList[ADDR_STREET]	[NAME]	= c.getString(2);
					addrList[ADDR_NUMBER]	[NAME]	= c.getString(3);
					addrList[ADDR_TYPE]		[NAME]	= c.getString(4);
					addrList[ADDR_LOTOK]	[NAME]	= c.getString(5);
					addrList[ADDR_COMMENT]	[NAME]	= c.getString(6);
					
					Log.d(TAG, "ADDR_RAYON		= " + addrList[ADDR_RAYON]	[NAME]);
					Log.d(TAG, "ADDR_PUNKT		= " + addrList[ADDR_PUNKT]	[NAME]);
					Log.d(TAG, "ADDR_STREET		= "	+ addrList[ADDR_STREET]	[NAME]);
					Log.d(TAG, "ADDR_NUMBER		= " + addrList[ADDR_NUMBER]	[NAME]);
					Log.d(TAG, "ADDR_TYPE		= " + addrList[ADDR_TYPE]	[NAME]);
					Log.d(TAG, "ADDR_LOTOK		= " + addrList[ADDR_LOTOK]	[NAME]);
					Log.d(TAG, "ADDR_COMMENT	= " + addrList[ADDR_COMMENT][NAME]);
					
				} while (c.moveToNext());
			}
		}
	}
	
	public void saveSetting(int setting, String value) {
		
		String field = "";
		
		switch (setting) {
		case Order.DATE_SALE:
			field = "date";
			break;
		case Order.KONTR:
			field = "kontr";
			break;
		case Order.P_TYPE:
			field = "price";
			break;
		case Order.OPER:
			field = "oper";
			break;
		case Order.STOCK:
			field = "stock";
			break;
		case Order.COMMENT:
			field = "comm";
			break;
		}
		
		if (settCount==0) { sqdb.createTRTSettings(kod); settCount++; }
		
		sqdb.updateTRTData(field, value, kod);
	}
	
	public void saveAddress(int index, String value) {
		
		String field = "";
		
		switch (index) {
		case ADDR_RAYON:
			field = "rayon";
			break;
		case ADDR_PUNKT:
			field = "punkt";
			break;
		case ADDR_STREET:
			field = "street";
			break;
		case ADDR_NUMBER:
			field = "number";
			break;
		case ADDR_TYPE:
			field = "type";
			break;
		case ADDR_LOTOK:
			field = "lotok";
			break;
		case ADDR_COMMENT:
			field = "comment";
			break;
		}
		
		if (addrCount==0) { sqdb.createTRTAddress(kod); addrCount++; }
		
		sqdb.updateTRTAddressData(field, value, kod);
	}
	
	public static JSONArray getTRTAddress(DataBase sqdb) {
		
		//getDate();
		
		/*String query = "select date, kodTRT, kodItem, priceType, price "
				+ "from itemsPriceTRT as itemsPriceTRT "
				+ "where date = ?";*/
		
		String query = "select kodTRT, rayon, punkt, street, number, type, lotok, comment "
				+ "from trtAddress as trtAddress";
		
		Cursor c = sqdb.getSQLData(query, null);
		//Cursor c = sqdb.getSQLData(query, new String[] { date });
		
		Log.d(TAG, "TRT address - " + c.getCount());
		
		JSONArray jsonArr = new JSONArray();
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					JSONObject pnObj = new JSONObject();
					try {
						
						pnObj.put("kodTRT",		c.getString(0));
						pnObj.put("rayon",		c.getString(1));
						pnObj.put("punkt",		c.getString(2));
						pnObj.put("street",		c.getString(3));
						pnObj.put("number",		c.getString(4));
						pnObj.put("type",		c.getString(5));
						pnObj.put("lotok",		c.getString(6));
						pnObj.put("comment",	c.getString(7));
						
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
