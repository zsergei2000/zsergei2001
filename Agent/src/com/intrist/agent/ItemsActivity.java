package com.intrist.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ItemsActivity extends Activity {

	private LayoutInflater ltInflater;
	
	private DataBase sqdb;
	
	private String mainParentKod, тип÷ен;
	
	private static final String TAG = "myLogs";
	
	private View toChange;
	
	private ArrayList<Map<String, String>> список«аказа;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.items_menu);
		
		//sqdb = new DataBase(this);
		//sqdb.OpenDB();
		
		ltInflater = getLayoutInflater();
		
		Intent intent = getIntent();
		тип÷ен = intent.getStringExtra("“ип÷ен");
		Log.d(TAG, "тип÷ен - " + тип÷ен);
		
		mainParentKod = "A2135      ";
		
		список«аказа = new ArrayList<Map<String, String>>();
		
		createKategory();
		
	}

	private void createKategory() {
		
		ArrayList<Map<String, String>> listData = getKategoryList();
		
		createKategoryButtons(listData);
		
	}
	
	private ArrayList<Map<String, String>> getKategoryList() {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				//+ "where thisGroup = 1 "
				//+ "and parentKod = ?";
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mainParentKod });
		//Cursor c = sqdb.getSQLData(query, null);
		
		Log.d(TAG, " атегорий - " + c.getCount());
		
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
	
	private void createKategoryButtons(ArrayList<Map<String, String>> listData) {
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items_k);
		
		//Log.d(TAG, "create items");
		
		int k = 0;
		View item = ltInflater.inflate(R.layout.tree_item_k, items, false);
		
		items.removeAllViews();
		for (Map<String, String> m : listData) {
			
			k++;
			
			if (k==1) {
				
				((TextView) item.findViewById(R.id.kod1)).setText(m.get("kod"));
				((TextView) item.findViewById(R.id.name1)).setText(m.get("name"));
				((FrameLayout) item.findViewById(R.id.touch1)).setOnTouchListener(katTouch);
				
			} else if(k==2) {
				
				((TextView) item.findViewById(R.id.kod2)).setText(m.get("kod"));
				((TextView) item.findViewById(R.id.name2)).setText(m.get("name"));
				((FrameLayout) item.findViewById(R.id.touch2)).setOnTouchListener(katTouch);
				
			} else if(k==3) {
				
				((TextView) item.findViewById(R.id.kod3)).setText(m.get("kod"));
				((TextView) item.findViewById(R.id.name3)).setText(m.get("name"));
				((FrameLayout) item.findViewById(R.id.touch3)).setOnTouchListener(katTouch);
				
			} 
			
			if (k==3) {
				items.addView(item);
				k=0;
				item = ltInflater.inflate(R.layout.tree_item_k, items, false);
			}
		}
		
		if (k>0) {
			items.addView(item);
			if (k==1) {
				((FrameLayout) item.findViewById(R.id.touch2)).setVisibility(View.INVISIBLE);
				((FrameLayout) item.findViewById(R.id.touch3)).setVisibility(View.INVISIBLE);
			}
			item = ltInflater.inflate(R.layout.tree_item_k, items, false);
		}
		
	}
	
	OnTouchListener katTouch = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String mKatCod = "";
				
				switch (v.getId()) {
				case R.id.touch1:
					mKatCod = ((TextView) v.findViewById(R.id.kod1)).getText().toString();
					break;
				case R.id.touch2:
					mKatCod = ((TextView) v.findViewById(R.id.kod2)).getText().toString();
					break;
				case R.id.touch3:
					mKatCod = ((TextView) v.findViewById(R.id.kod3)).getText().toString();
					break;
				default:
					break;
				}

				createPKategory(mKatCod);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private void createPKategory(String mKatCod) {
		
		if (mKatCod.equals("")) {
			return;
		}
		
		ArrayList<Map<String, String>> listData = getPKategoryList(mKatCod);
		
		createPKategoryButtons(listData);
		
	}
	
	private ArrayList<Map<String, String>> getPKategoryList(String mKatCod) {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>();
		
		String query = "select kod, name, parentKod "
				+ "from items as items "
				//+ "where thisGroup = 1 "
				//+ "and parentKod = ?";
				+ "where parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { mKatCod });
		
		Log.d(TAG, " од подкатегории - " + mKatCod);
		Log.d(TAG, "ѕодкатегорий - " + c.getCount());
		
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
	
	private void createPKategoryButtons(ArrayList<Map<String, String>> listData) {
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items_pk);
		
		items.removeAllViews();
		for (Map<String, String> m : listData) {
			View item = ltInflater.inflate(R.layout.tree_item, items, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(m.get("kod"));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(m.get("name"));
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(mPKatTouch);
			
			items.addView(item);
		}
		
	}
	
	OnTouchListener mPKatTouch = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				
				createItems(mItemKod);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private void createItems(String mItemKod) {
		
		if (mItemKod.equals("")) {
			return;
		}
		
		ArrayList<Map<String, String>> listData = getItemsList(mItemKod);
		
		createItemsButtons(listData);
		
	}
	
	private ArrayList<Map<String, String>> getItemsList(String mItemKod) {
		
		ArrayList<Map<String, String>> listData;
		Map<String, String> m;
		
		listData = new ArrayList<Map<String, String>>();
		
		/*String query = "select kod, name, parentKod "
				+ "from items as items "
				//+ "where thisGroup = 1 "
				//+ "and parentKod = ?";
				+ "where parentKod = ?";*/
		
		String query1 = "select kodItem, kodPrice, price "
				+ "		from itemsPrice as itemsPrice "
				+ "		where itemsPrice.kodPrice = ?";
		
		Cursor c1 = sqdb.getSQLData(query1, new String[] { тип÷ен });
		
		Log.d(TAG, "÷ены - " + c1.getCount());
		
		
		String query = "select items.kod, items.name, items.parentKod, itemsPrice.price "
				+ "from items as items "
				+ "left join "
				+ " ("
				+ "		select kodItem, kodPrice, price "
				+ "		from itemsPrice as itemsPrice "
				+ "		where itemsPrice.kodPrice = ?"
				+ ") as itemsPrice "
				+ "on items.kod = itemsPrice.kodItem "
				+ "where thisGroup = 0 "
				+ "and parentKod = ?";
		
		Cursor c = sqdb.getSQLData(query, new String[] { тип÷ен, mItemKod });
		
		Log.d(TAG, " од номенклатуры - " + mItemKod);
		Log.d(TAG, "Ќоменклатуры - " + c.getCount());
		Log.d(TAG, "“ип цен - " + тип÷ен);
		
		if (c!=null) {
			if (c.moveToFirst()) {
				do {
					
					m = new HashMap<String, String>();
					m.put("kod", c.getString(0));
					m.put("name", c.getString(1));
					m.put("parentKod", c.getString(2));
					m.put("price", (c.getString(3)==null? "0": c.getString(3)));
					
					listData.add(m);
					
				} while (c.moveToNext());
			}
		}
		c.close();
		
		return listData;
		
	}
	
	private void createItemsButtons(ArrayList<Map<String, String>> listData) {
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		//Log.d(TAG, "create items");
		items.removeAllViews();
		for (Map<String, String> m : listData) {
			View item = ltInflater.inflate(R.layout.item, items, false);
			((TextView) item.findViewById(R.id.kod)).setText(m.get("kod"));
			((TextView) item.findViewById(R.id.name)).setText(m.get("name"));
			((TextView) item.findViewById(R.id.price)).setText(m.get("price"));
			//((TextView) item.findViewById(R.id.tree_item_name2)).setText(m.get("comm"));
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(mItemsTouch);
			//((FrameLayout) item.findViewById(R.id.task_main)).setOnClickListener(checkClick);
			
			//((CheckBox) item.findViewById(R.id.task_box)).setOnCheckedChangeListener(check);
			
			items.addView(item);
		}
	}
	
	OnTouchListener mItemsTouch = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				toChange = v;
				
				String mItemKod = ((TextView) v.findViewById(R.id.kod)).getText().toString();
				String mItemName = ((TextView) v.findViewById(R.id.name)).getText().toString();
				String цена = ((TextView) v.findViewById(R.id.price)).getText().toString();
				
				//createItems(mItemKod);
				
				Intent intent = new Intent(ItemsActivity.this, Calculator.class);
				intent.putExtra("name",		mItemName);
				intent.putExtra("price",	цена);
				intent.putExtra("parent",	"");
				intent.putExtra("check",	"");
				
				startActivityForResult(intent, 2);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);

		if (arg2 == null) return;
		String result = arg2.getStringExtra("result");
		
		String sPrice = ((TextView) toChange.findViewById(R.id.price)).getText().toString();
		
		Double price	= Double.parseDouble(sPrice);
		Double k		= Double.parseDouble(result);
		Double sum		= k * price;
		
		((TextView) toChange.findViewById(R.id.amount)).setText(result);
		((TextView) toChange.findViewById(R.id.sum)).setText(Double.toString(sum));
		
		String kod = ((TextView) toChange.findViewById(R.id.kod)).getText().toString();
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("item",	kod);
		m.put("amount",	result);
		m.put("price",	sPrice);
		m.put("sum",	Double.toString(sum));
		
		список«аказа.add(m);
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onDestroy() {
		super.onDestroy();
		
		
		
		sqdb.closeDB();
	}
	
}
