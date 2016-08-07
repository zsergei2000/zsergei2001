package com.intrist.agent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListData {

	private static final String TAG = "myLogs";
	public static final int VIEW_MAIN_DAY = 0, VIEW_MAIN_TRT = 1, VIEW_MAIN_ORDERS = 2, VIEW_MAIN_SELL = 3, 
			VIEW_MAIN_OPTION = 4, VIEW_MAIN_COUNT = 5;
	public static final int SELL_KOD = 0, SELL_NAME = 1;
	public static final int KOD = 0, NAME = 1, VALUE = 2;
	public static final int SET_DATE=0, SET_STOCK=2, SET_OPER=3, SET_PRICE=4, SET_KONTR=6, SET_COMM=7, SET_COUNT=12;
	private DataBase sqdb;
	
	ListData (DataBase sqdb) {
		
		this.sqdb = sqdb;
		
	}
	
	public static int getDayOfWeek() {
		
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek--;
		Log.d(TAG, "dayOfWeek = " + dayOfWeek);
		return dayOfWeek;
	}
	
	public static ArrayList<View> getMenuMain(Context ctx, LayoutInflater ltInflater, OnClickListener onClick) {
		
		int[] images = getImagesMain();
		
		Drawable[] drawables = getDrawables(ctx);
		
		LinearLayout ll = new LinearLayout(ctx);
		
		ArrayList<View> array = new ArrayList<View>();
		
		for (int i = 0; i < VIEW_MAIN_COUNT; i++) {
			
			View item = ltInflater.inflate(R.layout.item_menu_1, ll, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			
			item.setBackgroundDrawable(drawables[i]);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(onClick);
			
			((ImageView) item.findViewById(R.id.menu1_image)).setImageResource(images[i]);
			
			array.add(item);
		}
		return array;
	}
	
	public static ArrayList<View> getMenuTRT(Context ctx, LayoutInflater ltInflater, OnClickListener onClick) {
		
		int[] images = getImagesTRT();
		
		Drawable[] drawables = getDrawables(ctx);
		
		LinearLayout ll = new LinearLayout(ctx);
		
		ArrayList<View> array = new ArrayList<View>();
		
		for (int i = 0; i < TRT.MAIN_COUNT; i++) {
			
			View item = ltInflater.inflate(R.layout.item_menu_1, ll, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			
			item.setBackgroundDrawable(drawables[i]);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(onClick);
			
			((ImageView) item.findViewById(R.id.menu1_image)).setImageResource(images[i]);
			
			array.add(item);
		}
		return array;
	}
	
	public static ArrayList<View> getMenuOrder(Context ctx, LayoutInflater ltInflater, OnClickListener onClick) {
		
		int[] images = getImagesOrder();
		
		Drawable[] drawables = getDrawables(ctx);
		
		LinearLayout ll = new LinearLayout(ctx);
		ArrayList<View> array = new ArrayList<View>();
		
		for (int i = 0; i < Order.MAIN_COUNT; i++) {
			
			View item = ltInflater.inflate(R.layout.item_menu_1, ll, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			
			item.setBackgroundDrawable(drawables[i]);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(onClick);
			
			((ImageView) item.findViewById(R.id.menu1_image)).setImageResource(images[i]);
			
			array.add(item);
		}
		return array;
	}
	
	private static Drawable[] getDrawables(Context ctx) {
		
		Drawable[] drawables = new Drawable[10];
		
		drawables[0] = ctx.getResources().getDrawable(R.drawable.select_red1);
		drawables[1] = ctx.getResources().getDrawable(R.drawable.select_yellow);
		drawables[2] = ctx.getResources().getDrawable(R.drawable.select_blue);
		drawables[3] = ctx.getResources().getDrawable(R.drawable.select_green);
		drawables[4] = ctx.getResources().getDrawable(R.drawable.select_red2);
		
		return drawables;
	}
	
	private static int[] getImagesMain() {
		
		int[] images = new int[10];
		
		images[0] = R.drawable.ic_calendar;
		images[1] = R.drawable.ic_list;
		images[2] = R.drawable.ic_text_document;
		images[3] = R.drawable.ic_graph;
		images[4] = R.drawable.ic_left_circle;
		
		return images;
	}
	
	private static int[] getImagesTRT() {
		
		int[] images = new int[10];
		
		images[0] = R.drawable.ic_favorite;
		images[1] = R.drawable.ic_text_document;
		images[2] = R.drawable.ic_edit;
		images[3] = R.drawable.ic_pin;
		images[4] = R.drawable.ic_left_circle;
		
		return images;
	}
	
	private static int[] getImagesOrder() {
		
		int[] images = new int[10];
		
		images[0] = R.drawable.ic_favorite;
		images[1] = R.drawable.ic_text_document;
		images[2] = R.drawable.ic_edit;
		images[3] = R.drawable.ic_ok;
		
		return images;
	}
	
	public String[] getMainMenuList(int dayOfWeek) {
		
		String[] data;
		
		String деньНеделиПрописью = получитьДеньНеделиПрописью(dayOfWeek);
		
		if (MainActivity.user.role==User.ROLE_PRICE) {
			
			data = new String[5];
			
			data[VIEW_MAIN_DAY]		= "День недели "+"("+деньНеделиПрописью+")";
			data[VIEW_MAIN_TRT]		= "ТРТ по маршруту";
			data[VIEW_MAIN_OPTION]	= "<- Назад";
			//data[VIEW_MAIN_OPTION]	= "Опции";
			
		} else {
			
			data = new String[5];
			
			data[VIEW_MAIN_DAY]		= "День недели "+"("+деньНеделиПрописью+")";
			data[VIEW_MAIN_TRT]		= "ТРТ по маршруту";
			data[VIEW_MAIN_ORDERS]	= "Все заказы";
			data[VIEW_MAIN_SELL]	= "Отчеты";
			data[VIEW_MAIN_OPTION]	= "<- Назад";
			//data[VIEW_MAIN_OPTION]	= "Опции";
		}
		
		return data;
		
	}
	
	public String получитьДеньНеделиПрописью(int dayOfWeek) {
		
		String деньНеделиПрописью = "";
		
		if (dayOfWeek==1) {
			деньНеделиПрописью = "пн";
		} else if (dayOfWeek==2) {
			деньНеделиПрописью = "вт";
		} else if (dayOfWeek==3) {
			деньНеделиПрописью = "ср";
		} else if (dayOfWeek==4) {
			деньНеделиПрописью = "чт";
		} else if (dayOfWeek==5) {
			деньНеделиПрописью = "пт";
		} else if (dayOfWeek==6) {
			деньНеделиПрописью = "сб";
		} else if (dayOfWeek==7) {
			деньНеделиПрописью = "вс";
		}
		
		return деньНеделиПрописью;
		
	}
	
	public String getKPKAgent() {
		
		String kodAgent = "";
		
		String query = "select * "
				+ "from agents as agents";
		
		Cursor c = sqdb.getSQLData(query, null);
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					kodAgent = c.getString(1);
					
					Log.d(TAG, "kpk agent - "+ c.getString(2) + ", kod = " + kodAgent);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return kodAgent;
		
	}
	
	public String[] getReportsNames() {
		
		String query = "select report "
				+ "from reports "
				+ "group by report";
		
		Cursor c = sqdb.getSQLData(query, null);
		
		Log.d(TAG, "reports = " + c.getCount());
		
		String[] listData = new String[c.getCount()];
		
		int i = 0;
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					listData[i++] = c.getString(0);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
	}
	
	public String[][] getSettings(String[][] data) {
		
		//ArrayList<String[]> listData = new ArrayList<String[]>();
		
		String[][] settings = getSettings();
		
		for (int i = 0; i < settings.length; i++) {
			
			int column = getSettingsColumn(i);
			
			//String name = (column>0? data[column][NAME]: "");
			
			String name = "";
			String value = "";
			
			if (column > 0) {
				
				name = data[column][NAME];
				value = data[column][KOD];
			}
			
			settings[i][NAME] = name;
			settings[i][VALUE] = value;
			
			//String[] newData = new String[2];
			
			//newData[KOD] = settings[i];
			//newData[NAME] = name;
			
			//listData.add(newData);
			
		}
		
		return settings;
	}
	
	private String[][] getSettings() {
		
		String[][] data = new String[SET_COUNT][3];
		
		data[SET_DATE]	[KOD]	= "Дата";
		data[1]			[KOD]	= "Адрес";
		data[SET_STOCK]	[KOD]	= "Склад";
		data[SET_OPER]	[KOD]	= "Вид операции";
		data[SET_PRICE]	[KOD]	= "Тип цен";
		data[5]			[KOD]	= "Пусто";
		data[SET_KONTR]	[KOD]	= "Контрагент";
		data[SET_COMM]	[KOD]	= "Комментарий";
		data[8]			[KOD]	= "Пусто";
		data[9]			[KOD]	= "Пусто";
		data[10]		[KOD]	= "Пусто";
		data[11]		[KOD]	= "Пусто";
		
		return data;
	}
	
	public int getSettingsColumn(int column) {
		
		int columnBack = -1; 
		
		switch (column) {
		case SET_DATE:
			columnBack = Order.DATE_SALE;
			break;
		case SET_STOCK:
			columnBack = Order.STOCK;
			break;
		case SET_OPER:
			columnBack = Order.OPER;
			break;
		case SET_PRICE:
			columnBack = Order.P_TYPE;
			break;
		case SET_KONTR:
			columnBack = Order.KONTR;
			break;
		case SET_COMM:
			columnBack = Order.COMMENT;
			break;
		}
		
		return columnBack;
	}
	
	public ArrayList<String[]> getSellDate() {
		
		//ArrayList<Map<String, String>> listData;
		//Map<String, String> m;
		
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat sdfKod = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		
		String dateKod = sdfKod.format(c.getTime());
        String date = sdf.format(c.getTime());
        
        try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return data;
		}
		
        for (int i = 0; i < 5; i++) {
			
        	String[] newData = new String[2];
			
			newData[SELL_KOD] = dateKod;
			newData[SELL_NAME] = date;
			
			data.add(newData);
        	
			c.add(Calendar.DATE, 1);
			dateKod = sdfKod.format(c.getTime());
	        date = sdf.format(c.getTime());
	        
		}
		return data;
	}
	
	public ArrayList<String[]> getSellKontr(String kodTRT) {
		
		ArrayList<String[]> listData = new ArrayList<String[]>();
		
		//ArrayList<Map<String, String>> listData = new ArrayList<Map<String, String>>();;
		//Map<String, String> m = new HashMap<String, String>();
		
		String query = "select kontragent.kod, kontragent.name "
				+ "from kontragentTRT as kontragentTRT "
				+ "left join kontragent as kontragent "
				+ "on kontragentTRT.kodKontr = kontragent.kod "
				+ "where kontragentTRT.kodTRT = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { kodTRT });
		
		//Log.d(TAG, "Таблица контрагентов - " + c.getCount());
		//Log.d(TAG, "currentTRT - " + ТРТ);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[2];
					
					newData[SELL_KOD] = c.getString(0);
					newData[SELL_NAME] = c.getString(1);
					
					listData.add(newData);
					
					/*m = new HashMap<String, String>();
					m.put("kod",	c.getString(0));
					m.put("name",	c.getString(1));
					listData.add(m);*/
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
	}
	
	public ArrayList<String[]> getSellPriceType(String kodTRT) {
		
		ArrayList<String[]> listData = new ArrayList<String[]>();
		//ArrayList<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		//Map<String, String> m;
		
		String query = "select priceType.kod, priceType.name "
				+ "from priceTypeTRT as priceTypeTRT "
				+ "INNER join priceType as priceType "
				+ "on priceTypeTRT.kodPrice = priceType.kod "
				+ "where priceTypeTRT.kodTRT = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { kodTRT });
		
		Log.d(TAG, "sell price typs = " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[2];
					
					newData[SELL_KOD] = c.getString(0);
					newData[SELL_NAME] = c.getString(1);
					
					listData.add(newData);
					
					/*m = new HashMap<String, String>();
					m.put("kod", c.getString(0));
					m.put("name", c.getString(1));
					listData.add(m);*/
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
		
	}
	
	public static ArrayList<String[]> getSellOper() {
		
		ArrayList<String[]> listData = new ArrayList<String[]>();
		
		String[] newData = new String[2];
		
		newData[SELL_KOD] = "Заказ";
		newData[SELL_NAME] = "Заказ товара";
		
		listData.add(newData);
		
		newData = new String[2];
		
		newData[SELL_KOD] = "Возврат";
		newData[SELL_NAME] = "Возврат товара";
		
		listData.add(newData);
		
		return listData;
		
	}
	
	public static String getSellOper(String kod) {
		
		ArrayList<String[]> listData = getSellOper();
		
		for (String[] data : listData) {
			
			if (data[SELL_KOD].equals(kod)) {
				
				return data[SELL_NAME];
				
			}
		}
		
		return "";
	}
	
	public ArrayList<String[]> getSellStock(String kodAgent) {
		
		ArrayList<String[]> listData = new ArrayList<String[]>();
		//ArrayList<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		//Map<String, String> m;
		
		//String агент = получитьТекущегоАгента();
		
		String query = "select stock.kod, stock.name "
				+ "from stockAgent as stockAgent "
				+ "INNER join stock as stock "
				+ "on stockAgent.kodStock = stock.kod "
				+ "where stockAgent.kodAgent = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { kodAgent });
		
		Log.d(TAG, "sell stock = " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[2];
					
					newData[SELL_KOD] = c.getString(0);
					newData[SELL_NAME] = c.getString(1);
					
					listData.add(newData);
					
					/*m = new HashMap<String, String>();
					m.put("kod", c.getString(0));
					m.put("name", c.getString(1));
					listData.add(m);*/
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		String[] newData = new String[2];
		
		newData[SELL_KOD] = "ВсеСклады";
		newData[SELL_NAME] = "Все склады";
		
		listData.add(newData);
		
		/*m = new HashMap<String, String>();
		m.put("kod",	"ВсеСклады");
		m.put("name",	"Все склады");
		listData.add(m);*/
		
		return listData;
		
	}
	
	public ArrayList<String[]> getTRTList(String kodAgent, int dayOfWeek, boolean showAllTRT) {
		
		String query = "";
		
		if (showAllTRT) {
		
			query = "select TRT.kod, TRT.name "
					+ "from route as route "
					+ "INNER join TRT as TRT "
					+ "on route.kodTRT = TRT.kod "
					+ "where route.kodAgent = ? "
					+ "order by number";
		
		} else {
			
			query = "select TRT.kod, TRT.name "
					+ "from route as route "
					+ "INNER join TRT as TRT "
					+ "on route.kodTRT = TRT.kod "
					+ "where route.kodAgent = ? "
					+ "and route.day = "+dayOfWeek
					+ " order by number";
		}
		
		Cursor c = sqdb.getSQLData(query, new String[] { kodAgent });
		
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		Log.d(TAG, "строк ТРТ - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[2];
					newData[0] = c.getString(0);
					newData[1] = c.getString(1);
					
					data.add(newData);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return data;
		
	}
	
	public ArrayList<Map<String, String>> получитьСписокДнейНедели() {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>(); 
		m = new HashMap<String, String>();
		m.put("kod", "1");
		m.put("name", "Понедельник");
		listData.add(m);
			
		m = new HashMap<String, String>();
		m.put("kod", "2");
		m.put("name", "Вторник");
		listData.add(m);

		m = new HashMap<String, String>();
		m.put("kod", "3");
		m.put("name", "Среда");
		listData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod", "4");
		m.put("name", "Четверг");
		listData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod", "5");
		m.put("name", "Пятница");
		listData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod", "6");
		m.put("name", "Суббота");
		listData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod", "7");
		m.put("name", "Воскресенье");
		listData.add(m);
		
		m = new HashMap<String, String>();
		m.put("kod", "8");
		m.put("name", "Сбросить");
		listData.add(m);
		
		return listData;
		
	}	
	
}
