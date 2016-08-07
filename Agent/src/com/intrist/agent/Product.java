package com.intrist.agent;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;
import android.view.View;

public class Product {
	
	public String kod, name, parent;
	public int hierarchy;
	public Product mainParent;
	public View view;
	
	public static final int KOD = 0, NAME = 1, PARENT = 2, HIERARCHY = 3;
	private static final String TAG = "myLogs";
	//private String date;
	//private DataBase sqdb;
	public ArrayList<Product> hierarchyList;
	//public ArrayList<Product[]> hierarchy;
	public ArrayList<View> productView;
	
	public Product(Cursor c) {
		
		kod		= c.getString(KOD);
		name	= c.getString(NAME);
		parent	= c.getString(PARENT);
		
	}
	
	public Product() {}
	
	public static int[] getItemKoef(DataBase sqdb, String kod) {
		
		int[] k = new int[2];
		//Map<String, String> m = null;
		
		String query = "select k2, k3 "
				+ "from items as items "
				+ "where kod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { kod });
		
		Log.d(TAG, "Таблица заказов - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					k[0] = c.getInt(0);
					k[1] = c.getInt(1);
					
					//m = new HashMap<String, String>();
					//m.put("k2",	c.getString(0));
					//m.put("k3",	c.getString(1));
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return k;
		
	}
	
	public void setHierarchy(DataBase sqdb) {
		
		hierarchyList = new ArrayList<Product>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { MainActivity.MAIN_PARENT });
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					Product product = new Product(c);
					product.hierarchy = 0;
					hierarchyList.add(product);
					
				} while (c.moveToNext());
			}
		}
		c.close();
	}
	
	public void changeHierarchy(int index) {
		
		Log.d(TAG, "index - " + index);
		
		ArrayList<Product> hierarchyList1 = new ArrayList<Product>();
		
		for (int i = 0; i < index+1; i++) {
			
			Log.d(TAG, "i - " + i);
			
			hierarchyList1.add(hierarchyList.get(i));
			
			
			
		}
		
		hierarchyList = hierarchyList1;
		
		//for (int i = index+2; i < hierarchyList.size(); i++) {
			
			//hierarchyList.remove(i);
		//}
		
		//hierarchyList = new ArrayList<Product>();
	}
	
	public static ArrayList<Product> getGroupList(DataBase sqdb, String mainParentKod) {
		
		ArrayList<Product> data = new ArrayList<Product>();
		
		//ArrayList<Map<String, String>> listData;
		//Map<String, String> m;
		
		//listData = new ArrayList<Map<String, String>>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mainParentKod });
		
		//Log.d(TAG, "Категорий - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					Product product = new Product();
					product.kod		= c.getString(KOD);
					product.name	= c.getString(NAME);
					product.parent	= c.getString(PARENT);
					
					data.add(product);
					
					/*m = new HashMap<String, String>();
					m.put("kod", c.getString(0));
					m.put("name", c.getString(1));
					m.put("parentKod", c.getString(2));
					
					listData.add(m);*/
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return data;	
	}
	
	public static ArrayList<String[]> getItemsListPriceTRT(DataBase sqdb, String date, String kodTRT, String mainParentKod) {
		
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		//Typs.TrtSalloutTyps t = new Typs.TrtSalloutTyps();
		
		String query = "select kod, name, tabPrice.price1, tabPrice.price2, tabPrice.comment "
				+ "from items as items "
				
				+ "left join "
				+ "("
				+ "		select kodItem, price1, price2, comment "
				+ "		from itemsPriceTRT as itemsPriceTRT "
				+ "		where date = ? "
				+ "		and kodTRT = ?"
				//+ "		and priceType = ?"
				+ ") as tabPrice "
				+ "on items.kod = tabPrice.kodItem "
				
				/*+ "left join "
				+ "("
				+ "		select kodItem, price as price2 "
				+ "		from itemsPriceTRT as itemsPriceTRT "
				+ "		where date = ? "
				+ "		and kodTRT = ? "
				+ "		and priceType = ?"
				+ ") as tabPrice2 "
				+ "on items.kod = tabPrice2.kodItem "*/
				
				+ "where parentKod = ?";
		
		String[] con = new String[] { date, kodTRT, mainParentKod };
		//String[] con = new String[] { date, kodTRT, t.aboveShell, date, kodTRT, t.underShell, mainParentKod };
		
		Cursor c = sqdb.getSQLData(query, con);
		
		Log.d(TAG, "items - " + c.getCount());
		
		DecimalFormat precision = new DecimalFormat("0.00");
		
		if (c.moveToFirst()) {
			do {
				
				String[] newData = new String[PriceTRT.COUNT];
				
				newData[PriceTRT.ITEM_KOD]	= c.getString(0);
				newData[PriceTRT.ITEM_NAME]	= c.getString(1);
				newData[PriceTRT.PRICE1]	= precision.format(c.getDouble(2)).replace(',', '.');
				newData[PriceTRT.PRICE2]	= precision.format(c.getDouble(3)).replace(',', '.');
				newData[PriceTRT.COMMENT]	= c.getString(4)==null?"":c.getString(4);
				
				data.add(newData);
				
			} while (c.moveToNext());
		}
		
		c.close();
		
		return data;
	}
	
	public static ArrayList<String[]> getItemsListOrder(DataBase sqdb, String mItemKod, String trtKod, String stock, boolean noBalance) {
		
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		stock = stock.equals("ВсеСклады")?"":stock;
		
		int i = stock.equals("")?2:4;
		
		String[] conditions = new String[i];
		
		conditions[0] = trtKod;
		if (stock.equals("")) {
			conditions[1] = mItemKod;
		} else {
			conditions[1] = stock;
			conditions[2] = stock;
			conditions[3] = mItemKod;
		}
		
		String query = "select items.kod, items.name, items.parentKod, itemsPrice.price, items.k1, items.k2, items.k3, "
				+ "(balance.rest-(ifnull(balanceSalout.amount, 0))) as rest "
				+ "from items as items "
				
				+ "left join "
				+ "("
				+ "		select kodManuf, kodItem, price "
				+ "		from priceTypeTRT as priceTypeTRT "
				+ "		left join itemsPrice as itemsPrice "
				+ "		on priceTypeTRT.kodPrice = itemsPrice.kodPrice "
				+ "		where priceTypeTRT.kodTRT = ?"
				+ ") as itemsPrice "
				+ "on items.kodManuf = itemsPrice.kodManuf "
				+ "and items.kod = itemsPrice.kodItem "
				
				/*+ "left join "
				+ "("
				+ "		select kod "
				+ "		from priceTypeTRT as priceTypeTRT "
				+ "		where priceTypeTRT.kodTRT = ?"
				+ ") as priceTypeTRT "
				+ "on items.kodManuf = priceTypeTRT.kodManuf "
				
				+ "left join "
				+ "("
				+ "		select kodItem, kodPrice, price "
				+ "		from itemsPrice as itemsPrice "
				+ "		where itemsPrice.kodPrice = ?"
				+ ") as itemsPrice "
				+ "on items.kod = itemsPrice.kodItem "*/
				
				+ "left join "
				+ "("
				+ "		select kod, SUM(rest) as rest "
				+ "		from balance as balance "
				+ "		"+(stock.equals("")?"":"where stock = ?")
				+ " 	GROUP BY kod "
				+ ") as balance "
				+ "on items.kod = balance.kod "
				
				+ "left join "
				+ "("
				+ "		select saloutT.kodItem, SUM(saloutT.amount) as amount "
				+ "		from saloutH as saloutH "
				+ "		left join saloutT as saloutT "
				+ "		on saloutH._id = saloutT.headId "
				+ "		"+(stock.equals("")?"":"where saloutH.stock = ?")
				+ " 	GROUP BY saloutT.kodItem "
				+ ") as balanceSalout "
				+ "on items.kod = balanceSalout.kodItem "
				
				+ "where thisGroup = 0 "
				+ (noBalance? "": "and balance.rest > 0 ")
				+ "and parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, conditions);
		
		Log.d(TAG, "trtKod = " + trtKod);
		Log.d(TAG, "stock = " + stock);
		Log.d(TAG, "item kod - " + mItemKod);
		Log.d(TAG, "items = " + c.getCount());
		
		DecimalFormat precision;
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					String[] newData = new String[MainActivity.COUNT];
					precision = new DecimalFormat("0.00");
					
					newData[MainActivity.KOD]	= c.getString(0);
					newData[MainActivity.NAME]	= c.getString(1);
					newData[MainActivity.REST]	= c.getString(7)==null? "0": c.getString(7);
					
					newData[MainActivity.K1]	= c.getString(4);
					newData[MainActivity.K2]	= c.getString(5);
					newData[MainActivity.K3]	= c.getString(6);
					
					if (c.getDouble(3)!=0.0) {
						Log.d(TAG, "name = " + c.getString(1) + ", price = " + c.getString(3));
					}
					
					newData[MainActivity.PRICE]	= precision.format(c.getDouble(3)).replace(',', '.');
					
					data.add(newData);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return data;
	}

	/*public String[][] getItemsList(String mainParentKod) {
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mainParentKod });
		
		Log.d(TAG, "номенклатур - " + c.getCount());
		
		String[][] data = new String[c.getCount()][5];
		int n = 0;
		
		if (c.moveToFirst()) {
			do {
				
				data[n][0] = c.getString(0);
				data[n][1] = c.getString(1);
				data[n][2] = c.getString(2);
				data[n][3] = "0.0";
				data[n++][4] = "0.0";
				//data[n++][5] = "";
				
			} while (c.moveToNext());
		}
		
		c.close();
		
		return data;
	}*/
	
	/*public ArrayList<Map<String, String>> getItemsList1(String mainParentKod) {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mainParentKod });
		
		Log.d(TAG, "Категорий - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					m = new HashMap<String, String>();
					m.put("kod",		c.getString(0));
					m.put("name",		c.getString(1));
					m.put("parentKod",	c.getString(2));
					m.put("price1",		"");
					m.put("price2",		"");
					m.put("type",		"");
					
					listData.add(m);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
	}*/
	
	/*public ArrayList<Map<String, String>> getMainCategoryList(String mainParentKod) {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				+ "where kod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mainParentKod });
		
		Log.d(TAG, "Категорий - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					m = new HashMap<String, String>();
					m.put("kod", c.getString(0));
					m.put("name", c.getString(1));
					m.put("parentKod", c.getString(2));
					
					listData.add(m);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
		
	}
	
	public ArrayList<Map<String, String>> getCategoryList(String mainParentKod) {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mainParentKod });
		
		//Log.d(TAG, "Категорий - " + c.getCount());
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					m = new HashMap<String, String>();
					m.put("kod", c.getString(0));
					m.put("name", c.getString(1));
					m.put("parentKod", c.getString(2));
					
					listData.add(m);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;	
	}*/
	
}
