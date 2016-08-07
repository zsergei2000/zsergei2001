package com.intrist.agent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private LayoutInflater ltInflater;
	public DataBase sqdb;
	public static final String MAIN_PARENT = "04         ";
	private static final String TAG = "myLogs";
	private LinearLayout EL_MENU_ITEMS, EL_LIST_BUTTONS, EL_RIGHT_MENU;
	private FrameLayout EL_PODBOR, EL_TOP_BACK, EL_TOP_MENU, EL_BUTTONS, EL_HEAD_BUTTONS;
	private ScrollView EL_MENU, EL_BUTTONS_list;
	private static final int SCREEN_MAIN = 1, SCREEN_TRT = 2, SCREEN_ITEMS = 3, SCREEN_ORDER = 4, SCREEN_SETPRICE = 5;
	public static final int KOD = 0, NAME = 1, PRICE = 2, AMOUNT = 3, SUM = 4, REST = 5, K1 = 6, K2 = 7, K3 = 8, COUNT = 9;
	public static final int P_COMMENT = 2, P_PRICE1 = 3, P_PRICE2 = 4, P_COUNT = 5;
	private View currentButton = null, currButtonCondition = null, toChange, trtButton = null, deletedOrder, 
			EL_HEAD_BUTTONS1, EL_HEAD_SETPRICE;
	private String currentTRT = "", mainParentKod, superParent, thisItemParent, kodAgent;
	private String[] line;
	private String[][] settings;
	private int dayOfWeek = 0, thisScreen = 1, mainGroupIndex, widthRMenu, mMenu, mMenu2, LAYOUT_SETTINGS, 
			LAYOUT_HIERARCHY, mScreen;
	private int[] images1, images2, images3;
	boolean showAllTRT = false, workAllowed=true, itemsList = false, closeProgramm = false, callSetPriceAgain = false;
	private ListData mListData;
	private Product product;
	private PriceTRT priceTRT;
	private PriceTRT.NewData priceTRTNewLine;
	private TablePriceTRT tabPrice;
	private Typs.TrtSalloutTyps sellType = new Typs.TrtSalloutTyps();
	private TRT trt;
	private Order order;
	public static User user;
	private ArrayList<String[]> t_items;
	private ArrayList<View> menuMain, menuTRT, menuOrder;
	private SimpleDateFormat formatDate;
	private Drawable[] drawables;
	private callBack mCallBack;
	private Handler h;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.screen_work);
		
		mainParentKod = "A2135      ";
		superParent = "04         ";
		
		sqdb = new DataBase(this);
		sqdb.OpenDB();
		
		user			= new User(sqdb);
		mListData		= new ListData(sqdb);
		product			= new Product();
		//dataControll	= new DataControll(this, sqdb);
		order			= new Order(sqdb, mListData);
		tabPrice		= new TablePriceTRT();
		
		ltInflater = getLayoutInflater();
		
		LAYOUT_SETTINGS = R.layout.list_settings;
		LAYOUT_HIERARCHY = R.layout.list_hierarchy;
		
		EL_TOP_MENU		= (FrameLayout) findViewById(R.id.top_menu);
		EL_MENU			= (ScrollView) findViewById(R.id.left_menu);
		EL_MENU_ITEMS	= (LinearLayout) findViewById(R.id.items_pk);
		EL_HEAD_BUTTONS = (FrameLayout) findViewById(R.id.head_buttons);
		EL_RIGHT_MENU	= (LinearLayout) findViewById(R.id.right_menu);
		EL_LIST_BUTTONS	= (LinearLayout) findViewById(R.id.list_buttons);
		
		//EL_HEAD_BUTTONS = (LinearLayout) item.findViewById(R.id.product_head);
		//((FrameLayout) findViewById(R.id.podbor_back)).setVisibility(View.GONE);
		//((CheckBox) findViewById(R.id.checkRest)).setVisibility(View.GONE);
		//((CheckBox) findViewById(R.id.checkRest)).setOnClickListener(нажатиеЌольќстатки);
		//((FrameLayout) findViewById(R.id.podbor_back)).setOnTouchListener(selectTouch);
        //EL_TOP_MENU = (LinearLayout) findViewById(R.id.top_menu);
        //EL_TOP_BACK = (FrameLayout) findViewById(R.id.top_back);
        //EL_MENU = (LinearLayout) findViewById(R.id.left_menu);
        //EL_MENU_ITEMS = (LinearLayout) findViewById(R.id.left_menu);
        //EL_PODBOR = (FrameLayout) findViewById(R.id.podbor_back);
        //EL_TOP_BACK.setVisibility(View.GONE);
        
        Calendar cel = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		formatDate = new SimpleDateFormat("yyyyMMdd");
        
        dayOfWeek = ListData.getDayOfWeek();
        
        //viewSettings();
        getAllMenu();
        //viewMain();
		
        mScreen = SCREEN_MAIN;
        
        /*EL_HEAD_BUTTONS1 = ltInflater.inflate(R.layout.head_sell, EL_HEAD_BUTTONS, true);
        EL_HEAD_BUTTONS1.setVisibility(View.GONE);
        
        EL_HEAD_SETPRICE = ltInflater.inflate(R.layout.head_set_price, EL_HEAD_BUTTONS, true);
        EL_HEAD_SETPRICE.setVisibility(View.GONE);*/
        
        EL_HEAD_BUTTONS1 = ltInflater.inflate(R.layout.head_sell, EL_HEAD_BUTTONS, false);
        EL_HEAD_BUTTONS1.setVisibility(View.GONE);
        EL_HEAD_BUTTONS.addView(EL_HEAD_BUTTONS1);
        
        EL_HEAD_SETPRICE = ltInflater.inflate(R.layout.head_set_price, EL_HEAD_BUTTONS, false);
        EL_HEAD_SETPRICE.setVisibility(View.GONE);
        EL_HEAD_BUTTONS.addView(EL_HEAD_SETPRICE);
        
        
        h = new Handler() {
			public void handleMessage(android.os.Message msg) {
				viewSettings();
				changeScreen(SCREEN_MAIN);
			};
		};
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(1);
			    } catch (InterruptedException e) {
			      e.printStackTrace();}
				h.sendEmptyMessage(1);
			}
		});
		
		t.start();
	}
	
	private void changeScreen(int newScreen) {
		
		//Log.d(TAG, "newScreen = " + newScreen);
		//Log.d(TAG, "mScreen = " + mScreen);
		
		switch (newScreen) {
		case SCREEN_MAIN:
			switch (mScreen) {
			case SCREEN_MAIN:
				setMenuMain();
				mCallBack = viewMain;
				animate(newScreen);
				break;
			case SCREEN_TRT:
				setViewSettingsClear();
				mCallBack = viewMain;
				animate(newScreen);
				break;
			}
			break;
		case SCREEN_TRT:
			switch (mScreen) {
			case SCREEN_MAIN:
			case SCREEN_ORDER:
				mCallBack = viewTRT;
				animate(newScreen);
				break;
			case SCREEN_SETPRICE:
				EL_LIST_BUTTONS.removeAllViews();
				mCallBack = viewTRT;
				animate(newScreen);
				break;
			}
			break;
		case SCREEN_ITEMS:
			currentButton.setSelected(false);
			EL_HEAD_BUTTONS1.setVisibility(View.INVISIBLE);
			mCallBack = viewItems;
			animate(newScreen);
			break;
		case SCREEN_SETPRICE:
			currentButton.setSelected(false);
			EL_HEAD_SETPRICE.setVisibility(View.INVISIBLE);
			mCallBack = viewSetPrice;
			animate(newScreen);
			break;
		case SCREEN_ORDER:
			switch (mScreen) {
			case SCREEN_ITEMS:
				EL_LIST_BUTTONS.removeAllViews();
				mCallBack = viewOrder;
				animate(newScreen);
				break;
			case SCREEN_MAIN:
			case SCREEN_TRT:
				currentButton.setSelected(false);
				mCallBack = viewOrder1;
				animate(newScreen);
				break;
			}
			break;
		}
		mScreen = newScreen;
	}
	
	private void viewSettings() {
		
		View item = null;
		int weight = 0;
		switch (mScreen) {
		case SCREEN_ITEMS:
		case SCREEN_SETPRICE:
			item = ltInflater.inflate(LAYOUT_HIERARCHY, EL_TOP_MENU, false);
			weight = 7;
			break;
		default:
			item = ltInflater.inflate(LAYOUT_SETTINGS, EL_TOP_MENU, false);
			weight = 5;
		}
		EL_TOP_MENU.addView(item);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
		EL_TOP_MENU.getLayoutParams();
		params.weight = weight;
		EL_TOP_MENU.setLayoutParams(params);
	}
	
	private void getAllMenu() {
		
		menuMain	= ListData.getMenuMain(this, ltInflater, viewMainClick);
		menuTRT		= ListData.getMenuTRT(this, ltInflater, viewTRTClick);
		menuOrder	= ListData.getMenuOrder(this, ltInflater, viewOrderClick);
	}
	
	private void setMenuMain() {
		
		int i = 0;
		for (View item : menuMain) {
			
			if (i==ListData.VIEW_MAIN_TRT) { trtButton = item; }
			if (i==ListData.VIEW_MAIN_DAY) { currentButton = item;}
			i++;
			EL_MENU_ITEMS.addView(item);
		}
	}
	
	private void setMenuTRT() {
		
		int i = 0;
		for (View item : menuTRT) {
			
			if (i==TRT.MAIN_SELL) {currentButton = item;}
			i++;
			EL_MENU_ITEMS.addView(item);
		}
	}
	
	private void setMenuOrder() {
		
		int i = 0;
		for (View item : menuOrder) {
			
			if (i==Order.MAIN_ORDER) {currentButton = item;}
			i++;
			EL_MENU_ITEMS.addView(item);
		}
	}
	
	callBack viewMain = new callBack() {
		public void onBack() {
			currentButton.setSelected(true);
			mMenu = ListData.VIEW_MAIN_DAY;
			viewMainButtons();
		}
	};
	
	interface callBack {
		void onBack();
	}
	
	private void setDrawables() {
		
		drawables = new Drawable[10];
		
		drawables[0] = getResources().getDrawable(R.drawable.select_red1);
		drawables[1] = getResources().getDrawable(R.drawable.select_yellow);
		drawables[2] = getResources().getDrawable(R.drawable.select_blue);
		drawables[3] = getResources().getDrawable(R.drawable.select_green);
		drawables[4] = getResources().getDrawable(R.drawable.select_red2);
	}
	
	private void setImages() {
		
		images1 = new int[10];
		
		images1[0] = R.drawable.ic_calendar;
		images1[1] = R.drawable.ic_list;
		images1[2] = R.drawable.ic_text_document;
		images1[3] = R.drawable.ic_graph;
		images1[4] = R.drawable.ic_left_circle;
		
		images2 = new int[10];
		
		images2[0] = R.drawable.ic_favorite;
		images2[1] = R.drawable.ic_text_document;
		images2[2] = R.drawable.ic_edit;
		images2[3] = R.drawable.ic_pin;
		images2[4] = R.drawable.ic_left_circle;
		
		images3 = new int[10];
		
		images3[0] = R.drawable.ic_favorite;
		images3[1] = R.drawable.ic_text_document;
		images3[2] = R.drawable.ic_edit;
		images3[3] = R.drawable.ic_ok;
	}
	
	private void viewMain() {
		
		//settings = mListData.getSettings(new String[Order.COUNT][2]);
		//Double sum = order.getOrdersSum("");
		//updateSumResult(sum);
		//viewSettings();
		//setViewSettingsClear();
		viewMainButtons();
	}
	
	OnClickListener viewMainClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if (currentButton!=null) {
				currentButton.setSelected(false);
			}
			currentButton = v;
			v.setSelected(true);
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			mMenu = Integer.parseInt(kod);
			
			widthRMenu = EL_RIGHT_MENU.getMeasuredWidth();
			
			Log.d(TAG, "widthRMenu = " + widthRMenu);
			
			callBack back = new callBack() {
				public void onBack() {
					viewMainButtons();
				}
			};
			animateButtonsGone(back);
		}
	};
	
	void viewMainButtons() {
		
		widthRMenu = EL_RIGHT_MENU.getMeasuredWidth();
		
		switch (mMenu) {
		case ListData.VIEW_MAIN_OPTION:
			
			onBackPressed();
			break;
		case ListData.VIEW_MAIN_ORDERS:
			
			viewMainOrdersButtons();
			break;
		case ListData.VIEW_MAIN_TRT:
			
			viewMainTRTButtons();
			break;
		case ListData.VIEW_MAIN_DAY:

			viewMainDaysButtons();
			break;
		case ListData.VIEW_MAIN_SELL:
			
			viewMainReportsButtons();
			break;
		}
		
		animateButtonsShow();
	}
	
	private void viewMainDaysButtons() {
		
		ArrayList<Map<String, String>> listData = mListData.получить—писокƒнейЌедели();
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		//EL_LIST_BUTTONS.removeAllViews();
		for (Map<String, String> m : listData) {
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(m.get("kod"));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(m.get("name"));
		
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(нажатиеƒниЌедели);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewMainButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewMainDaysData(String kod) {
		
		//String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
		
		dayOfWeek = Integer.parseInt(kod);
		
		currentButton.setSelected(false);
		currentButton = trtButton;
		currentButton.setSelected(true);
		
		//trtButton.setBackgroundColor(R.drawable.button_check);
		//trtButton.setSelected(true);
		/*String деньЌеделиѕрописью = mListData.получитьƒеньЌеделиѕрописью(dayOfWeek);
		if (!деньЌеделиѕрописью.equals("")) {
			деньЌеделиѕрописью = "("+деньЌеделиѕрописью+")";
		}*/
		//((TextView) currentButton.findViewById(R.id.tree_item_name1)).setText("ƒень недели "+деньЌеделиѕрописью);
		//currentButton.setBackgroundColor(0);
		//currentButton.setSelected(false);
		//currentButton = trtButton;
		
		mMenu = ListData.VIEW_MAIN_TRT;
		
		//widthRMenu = EL_BUTTONS.getMeasuredWidth();
		
		callBack back = new callBack() {
			public void onBack() {
				viewMainButtons();
			}
		};
		animateButtonsGone(back);
		
		//viewMainTRTButtons();
	}
	
	private void viewMainTRTButtons() {
		
		ArrayList<String[]> data = TRT.getList(sqdb, user.kod, dayOfWeek);
		
		//EL_LIST_BUTTONS.removeAllViews();
		for (String[] line : data) {
			
			//View item = ltInflater.inflate(R.layout.tree_item, EL_LIST_BUTTONS, false);
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(line[0]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(line[1]);
		
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewMainTRTTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewMainButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewMainTRTData(String kod) {
		
		Log.d(TAG, "TRT kod = " + kod);
		
		trt = new TRT();
		trt.sqdb = sqdb;
		trt.kod = kod;
		
		changeScreen(SCREEN_TRT);
		
		/*callBack back = new callBack() {
			public void onBack() {
				viewTRTAnim();
			}
		};
		animateButtonsGone(back);*/
	}
	
	private void viewMainOrdersButtons() {
		
		//создатьѕравый—писок();
		//ArrayList<Map<String, String>> listData = order.setOrderList();
		
		order.setOrderList();
		
		/*DecimalFormat precision = new DecimalFormat("0.00");
		String newSSum = precision.format(newSum).replace(',', '.');*/
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		int i = 0;
		//EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : order.list) {
			
			//View item = ltInflater.inflate(R.layout.salout, EL_LIST_BUTTONS, false);
			View item = ltInflater.inflate(R.layout.item_orders, EL_LIST_BUTTONS, false);
			
			//((TextView) item.findViewById(R.id.kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.kod)).setText(data[Order.TRT]);
			((TextView) item.findViewById(R.id.name)).setText(data[Order.NUMBER]);
			((TextView) item.findViewById(R.id.sum)).setText(data[Order.SUM]);
			
		/*for (Map<String, String> m : listData) {
			View item = ltInflater.inflate(R.layout.salout, items, false);
			((TextView) item.findViewById(R.id.name)).setText(m.get("kod"));
			((TextView) item.findViewById(R.id.sum)).setText(m.get("sum"));
			((TextView) item.findViewById(R.id.kod)).setText(m.get("trt"));*/		
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewTRTOrderTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewMainButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewMainOrdersData(String kod, String index) {
		
		//changeScreen(SCREEN_TRT);
		//thisScreen = SCREEN_ORDER;
		
		trt = new TRT();
		trt.sqdb = sqdb;
		trt.kod = kod;
		trt.setTRT();
		
		order.setOrder(index);
		settings = order.head;
		//settings = mListData.getSettings(order.head);
		
		changeScreen(SCREEN_ORDER);
		
		//viewOrder();
	}
	
	private void viewMainReportsButtons() {
		
		String[] listData = mListData.getReportsNames();
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		EL_LIST_BUTTONS.removeAllViews();
		//for (Map<String, String> m : listData) {
		for (int i = 0; i < listData.length; i++) {
		
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(listData[i]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(listData[i]);
		
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(touchReports);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewMainButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewMainReportsData(String name) {
		
		//String name = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
		
		Intent intent = new Intent(MainActivity.this, SellData.class);
		
		intent.putExtra("name",	name);
		
		startActivityForResult(intent, 2);
	}
	
	OnClickListener viewMainButtonsClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if (currentButton!=null) {
				currentButton.setSelected(false);
			}
			currentButton = v;
			v.setSelected(true);
			
			String kod;
			String name;
			
			switch (mMenu) {
			case ListData.VIEW_MAIN_DAY:
				
				kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				viewMainDaysData(kod);
				break;
			case ListData.VIEW_MAIN_TRT:
				
				kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				viewMainTRTData(kod);
				break;
			case ListData.VIEW_MAIN_ORDERS:
				
				kod = ((TextView) v.findViewById(R.id.kod)).getText().toString();
				name = ((TextView) v.findViewById(R.id.name)).getText().toString();
				viewMainOrdersData(kod, name);
				break;
			case ListData.VIEW_MAIN_SELL:
				
				name = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				viewMainReportsData(name);
				break;
			}
		}
	};
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void viewTRTAnim() {
		
		final callBack back = new callBack() {
			public void onBack() {
				viewTRT();
				currentButton.setSelected(true);
				mMenu = TRT.MAIN_SELL;
				showTRTMenu();
			}
		};
		
		callBack back1 = new callBack() {
			public void onBack() {
				viewTRTButtons();
				//animateMenuRotateShow(back);
			}
		};
		
		//animateMenuRotateGone(back1);
	}
	
	private void viewTRT() {
		
		/*if (user.role==User.ROLE_PRICE) {
			thisScreen = SCREEN_SETPRICE;
			EL_BUTTONS.removeAllViews();
			изменитьЌаименование нопки("ѕодбор");
			viewSetPrice();
			return;
		}*/
		
		//thisScreen = SCREEN_TRT;
		
		trt.setTRT();
		
		settings = trt.settings;
		setViewSettings();
		
		order.newOrder();
		
		currentButton.setSelected(true);
		mMenu = TRT.MAIN_SELL;
		showTRTMenu();
		
		//settings = mListData.getSettings(trt.settings);
		//Double sum = order.getOrdersSum(trt.kod);
		//updateSumResult(sum);
		//изменитьЌаименование нопки("Ќовый заказ");
	}
	
	callBack viewTRT = new callBack() {
		public void onBack() {
			
			trt.setTRT();
			
			settings = trt.settings;
			setViewSettings();
			
			order.newOrder();
			
			currentButton.setSelected(true);
			mMenu = TRT.MAIN_SELL;
			showTRTMenu();
		}
	};
	
	private void viewTRTButtons() {
		
		String[] list = TRT.getMainList();
		
		//EL_MENU_ITEMS.removeAllViews();
		for (int i = 0; i < list.length; i++) {
			
			if (list[i]==null) continue;
			
			View item = ltInflater.inflate(R.layout.item_menu_1, EL_MENU_ITEMS, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			//((TextView) item.findViewById(R.id.tree_item_name1)).setText(trt.mainList[i]);
			
			item.setBackgroundDrawable(drawables[i]);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTClick);
			
			((ImageView) item.findViewById(R.id.menu1_image)).setImageResource(images2[i]);
			
			if (i==TRT.MAIN_SELL) {
				
				currentButton = item;
			}
			EL_MENU_ITEMS.addView(item);
		}
	}
	
	OnClickListener viewTRTClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if (currentButton!=null) {
				currentButton.setSelected(false);
			}
			currentButton = v;
			v.setSelected(true);
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			mMenu = Integer.parseInt(kod);
			
			callBack back = new callBack() {
				public void onBack() {
					showTRTMenu();
				}
			};
			animateButtonsGone(back);
		}
	};
	
	void showTRTMenu() {
		
		switch (mMenu) {
		case TRT.MAIN_SELL:
			viewTRTSellButtons();
			break;
		case TRT.MAIN_ORDER:
			viewTRTOrder();
			break;
		case TRT.MAIN_PRICE:
			changeScreen(SCREEN_SETPRICE);
			//viewSetPrice();
			break;
		case TRT.MAIN_ADDR:
			viewTRTAddrButtons();
			break;
		case TRT.MAIN_BACK:
			viewTRTBack();
			break;
		}
		animateButtonsShow();
	}
	
	private void viewTRTSell() {
		
		//EL_BUTTONS = (FrameLayout) findViewById(R.id.right_menu);
		//EL_BUTTONS.removeAllViews();
		//View item = ltInflater.inflate(R.layout.sale_menu, EL_BUTTONS, false);
		//EL_BUTTONS.addView(item);
		
		viewTRTSellButtons();
		
		//—оздатьќсновноећеню”словийѕродаж();
		//создатьћеню”слови€ѕродаж();
	}
	
	private void viewTRTSellButtons() {
		
		String text = "";
		for (int i = 0; i < trt.sellList.length; i++) {
			
			if (i==TRT.SELL_PRICE) continue;
			
			switch (i) {
			case TRT.SELL_DATE:
				text = settings[Order.DATE_SALE][NAME];
				break;
			case TRT.SELL_KONTR:
				text = settings[Order.KONTR][NAME];
				break;
			case TRT.SELL_OPER:
				text = settings[Order.OPER][NAME];
				break;
			case TRT.SELL_STOK:
				text = settings[Order.STOCK][NAME];
				break;
			default:
			text = "";		
			}
			text = trt.sellList[i] + "\n" + (text==null?"":text);
			
			//text = trt.sellList[i];
			if (i==TRT.SELL_ORDER) { 
				text = trt.sellList[i]; 
				if (mScreen==SCREEN_TRT) { 
					text = "Ќќ¬џ… «ј ј«";
				} else if (mScreen==SCREEN_ORDER) {
					text = "»«ћ≈Ќ»“№ «ј ј«"; 
				}
			}
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(text);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTSellClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	OnClickListener viewTRTSellClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			/*if (currentButton!=null) {
				currentButton.setSelected(false);
			}
			currentButton = v;
			v.setSelected(true);*/
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			mMenu2 = Integer.parseInt(kod);
			
			callBack back = new callBack() {
				public void onBack() {
					viewTRTSellShow();
				}
			};
			animateButtonsGone(back);
		}
	};
	
	private void viewTRTSellShow() {

		switch (mMenu2) {
		case TRT.SELL_DATE:
			mMenu2 = Order.DATE_SALE;
			viewTRTSellDate();
			break;
		case TRT.SELL_KONTR:
			mMenu2 = Order.KONTR;
			viewTRTSellKontr();
			break;
		case TRT.SELL_PRICE:
			mMenu2 = 100;
			viewTRTSellPriceType();
			break;
		case TRT.SELL_OPER:
			mMenu2 = Order.OPER;
			viewTRTSellOper();
			break;
		case TRT.SELL_STOK:
			mMenu2 = Order.STOCK;
			viewTRTSellStock();
			break;
		case TRT.SELL_ORDER:
			if (checkSettings()) {
				changeScreen(SCREEN_ITEMS);
				//thisScreen = SCREEN_ITEMS;
				//viewItems();
			} else {
				//mMenu = TRT.MAIN_SELL;
				showTRTMenu();
			}
			break;	
		}
		animateButtonsShow();
	}
	
	private void viewTRTSellDate() {

		ArrayList<String[]> listData = mListData.getSellDate();
		
		EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : listData) {
		
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(data[KOD]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data[NAME]);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(нажатиеƒатаќтгрузки);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTSellButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewTRTSellKontr() {

		ArrayList<String[]> listData = mListData.getSellKontr(trt.kod);

		EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : listData) {
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(data[KOD]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data[NAME]);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(нажатие онтрагент);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTSellButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewTRTSellPriceType() {

		ArrayList<String[]> listData = mListData.getSellPriceType(trt.kod);
		
		EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : listData) {
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(data[KOD]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data[NAME]);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(нажатие“ипы÷ен);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTSellButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewTRTSellOper() {

		ArrayList<String[]> listData = ListData.getSellOper();
		
		EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : listData) {
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(data[KOD]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data[NAME]);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(нажатие¬идќперации);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTSellButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewTRTSellStock() {

		ArrayList<String[]> listData = mListData.getSellStock(user.kod);
		
		EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : listData) {
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(data[KOD]);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data[NAME]);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(нажатие—клады);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTSellButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	OnClickListener viewTRTSellButtonsClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			String mItemName = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
			
			Log.d(TAG, "kod = " + mItemKod);
			Log.d(TAG, "name = " + mItemName);
			//setSetting(mMenu2, mItemName, mItemKod);
			
			settings[mMenu2][0] = mItemKod;
			settings[mMenu2][1] = mItemName;
			
			setViewSettings();
			
			if (mMenu2==Order.KONTR|mMenu2==Order.STOCK|mMenu2==Order.OPER|mMenu2==Order.COMMENT) {
				trt.saveSetting(mMenu2, mItemKod);
			}
			
			callBack back = new callBack() {
				public void onBack() {
					mMenu = TRT.MAIN_SELL;
					showTRTMenu();
				}
			};
			animateButtonsGone(back);
		}
	};
	
	private void viewTRTOrder() {
		
		//создатьѕравый—писок();
		
		viewTRTOrderButtons();
		
		//ArrayList<Map<String, String>> listData = mListData.получить—писок“екущих«аказов(trt.kod);
		//обновить»тогѕо«аказам(listData);
	}
	
	private void viewTRTOrderButtons() {
		
		//создатьѕравый—писок();
		order.setOrderList(trt.kod);
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		int i = 0;
		EL_LIST_BUTTONS.removeAllViews();
		for (String[] data : order.list) {
			
			//View item = ltInflater.inflate(R.layout.salout, EL_LIST_BUTTONS, false);
			View item = ltInflater.inflate(R.layout.item_orders, EL_LIST_BUTTONS, false);
			
			((TextView) item.findViewById(R.id.kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.name)).setText(data[Order.NUMBER]);
			((TextView) item.findViewById(R.id.sum)).setText(data[Order.SUM]);
			
			//((TextView) item.findViewById(R.id.kod)).setText(data[Order.TRT]);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewTRTOrderTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewTRTOrderData(String index) {
		
		order.setOrder(index);
		settings = order.head;
		//settings = mListData.getSettings(order.head);
		
		changeScreen(SCREEN_ORDER);
		
		/*thisScreen = SCREEN_ORDER;
		
		order.setOrder(index);
		settings = mListData.getSettings(order.head);
		viewOrder();*/
		
		//EL_PODBOR.setVisibility(View.VISIBLE);
	}
	
	private void viewTRTAddrButtons() {
		
		//создатьѕравый—писок();
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		trt.setAddrList();
		String trtType = "";
		
		EL_LIST_BUTTONS.removeAllViews();
		for (int i = 0; i < trt.addrList.length; i++) {
			
			if (i==TRT.ADDR_TYPE) {
				trtType = trt.addrList[i][NAME];
				
			} else if(i==TRT.ADDR_LOTOK) {
				if (!trtType.equals(Typs.TRTAddrType.market)) continue;
			}
			
			String text = trt.addrList[i][KOD] + "\n" + trt.addrList[i][NAME];
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, EL_LIST_BUTTONS, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(text);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewTRTAddrTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewTRTButtonsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewTRTAddrData(String kod) {
		
		//String sIndex = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
		
		int index = Integer.parseInt(kod);
		
		//viewTRTAddrSet(Integer.parseInt(index));
		switch (index) {
		case TRT.ADDR_TYPE:
			viewTRTAddrTypeSet(index);
			break;
		default:
			viewTRTAddrSet(index);
		}
	}
	
	OnClickListener viewTRTButtonsClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			switch (mMenu) {
			case TRT.MAIN_ORDER:
				
				String index = ((TextView) v.findViewById(R.id.name)).getText().toString();
				//viewMainOrdersData(index);
				viewTRTOrderData(index);
				break;
			case TRT.MAIN_ADDR:
				
				String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				toChange = v;
				viewTRTAddrData(kod);
				break;
			}
		}
	};
	
	private void viewTRTAddrSet(final int index) {
		
		String name = trt.addrList[index][NAME];
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(trt.addrList[index][KOD]);
		alert.setMessage("¬ведите текст");
		
		final EditText input = new EditText(this);
		input.setText(name);
		
		alert.setView(input);
		
		alert.setPositiveButton("ќ ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				String value = input.getText().toString();
				setAddress(index, value);
			}
		});
		alert.setNegativeButton("ќтмена", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		
		alert.show();
	}
	
	private void viewTRTAddrTypeSet(final int index) {
		
		String name = trt.addrList[TRT.ADDR_TYPE][NAME];
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(trt.addrList[index][KOD]);
		alert.setMessage("”кажите тип");
		
        final RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(LinearLayout.HORIZONTAL);
        
        RadioButton rdbtn = new RadioButton(this);
        rdbtn.setId(1);
        rdbtn.setText(Typs.TRTAddrType.house);
        rdbtn.setChecked(!name.equals(Typs.TRTAddrType.market));        
        rg.addView(rdbtn);
        
        rdbtn = new RadioButton(this);
        rdbtn.setId(2);
        rdbtn.setText(Typs.TRTAddrType.market);
        rdbtn.setChecked(name.equals(Typs.TRTAddrType.market));
        rg.addView(rdbtn);
        
        alert.setView(rg);
		
		alert.setPositiveButton("ќ ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				String value = "";
				
				int id = rg.getCheckedRadioButtonId();
				if (id==1) {
					value =Typs.TRTAddrType.house;
				} else {
					value =Typs.TRTAddrType.market;
				}
				setAddress(index, value);
				viewTRTAddrButtons();
			}
		});
		alert.setNegativeButton("ќтмена", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		
		alert.show();
	}
	
	private void setAddress(int index, String value) {
		
		trt.addrList[index][NAME] = value;
		
		String text = trt.addrList[index][KOD] + "\n" + value;
		
		((TextView) toChange.findViewById(R.id.tree_item_name1)).setText(text);
		
		trt.saveAddress(index, value);	
	}
	
	private void viewTRTBack() {
		
		currentButton.setSelected(false);
		changeScreen(SCREEN_MAIN);
	}
	
	callBack viewItems = new callBack() {
		public void onBack() {
			
			product.setHierarchy(sqdb);
			viewSettings();
			viewItemsTopButtons();
			EL_HEAD_BUTTONS1.setVisibility(View.VISIBLE);
			//viewItemsMenu();
			
			EL_TOP_BACK = (FrameLayout) EL_TOP_MENU.findViewById(R.id.top_back);
			EL_TOP_BACK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (mScreen) {
					case SCREEN_ITEMS:
						changeScreen(SCREEN_ORDER); 
						break;
					case SCREEN_SETPRICE:
						changeScreen(SCREEN_TRT); 
						break;
					}
				}
			});
			animate(SCREEN_ITEMS);
		}
	};
	
	private void viewItems() {
		
		//EL_LIST_BUTTONS.removeAllViews();
		
		callBack back1 = new callBack() {
			public void onBack() {
				//EL_TOP_MENU.removeAllViews();
			}
		};
		animateTopTranslateGone1(back1);
		//animateTopGone(back1);
		
		callBack back = new callBack() {
			public void onBack() {
				EL_MENU.setVisibility(View.GONE);
				viewItemsShow();
			}
		};
		animateMenuTranslateGone1(back);
	}
	
	private void viewItemsShow() {
		
	}
	
	private void viewItemsMenu() {
		
		View item = null;
		switch (mScreen) {
		case SCREEN_ITEMS:
			item = ltInflater.inflate(R.layout.head_sell, EL_HEAD_BUTTONS, true);
			//EL_HEAD_BUTTONS.addView(item);
			break;
		case SCREEN_SETPRICE:
			item = ltInflater.inflate(R.layout.head_set_price, EL_BUTTONS, false);
			break;
		}
	}
	
	private void viewItemsTopButtons() {
		
		//Product[] array = product.getHierarchy(index);
		//ArrayList<Map<String, String>> listData = product.getCategoryList(superParent);
		//product.hierarchyStack = new ArrayList<String>();
		//ArrayList<Map<String, String>> listData = product.getCategoryList(superParent);
		//product.productView = new View[product.hierarchyList.size()];
		
		product.productView = new ArrayList<View>();
		
		LinearLayout items = (LinearLayout) EL_TOP_MENU.findViewById(R.id.items_k);
		
		items.removeAllViews();
		int i = 0;
		for (Product data : product.hierarchyList) {
			
			View item = ltInflater.inflate(R.layout.item_main, items, false);			
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data.name);
			//((TextView) item.findViewById(R.id.tree_item_name2)).setText(Integer.toString(i));
			
			product.productView.add(item);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewItemsTopTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewItemsTopClick);
			
			items.addView(item);
		}
	}
	
	OnClickListener viewItemsTopClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			int index = product.productView.indexOf(v);
			
			Log.d(TAG, "view = " + v);
			Log.d(TAG, "index = " + index);
			
			Product mProduct = product.hierarchyList.get(index);
			
			//String index = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			//int hIndex = Integer.parseInt(index);
			
			//Product mProduct = product.hierarchyList.get(hIndex);
			
			product.hierarchy	= mProduct.hierarchy;
			
			//Log.d(TAG, "hierarchy top = " + product.hierarchy);
			
			if (mProduct.hierarchy==0) {
				product.mainParent = mProduct;
				product.setHierarchy(sqdb);
			} else {
				product.changeHierarchy(index);
			}
			
			viewItemsTopButtons();
			
			if (product.hierarchy==user.hierarchy) {
				createItems(mProduct.kod);
			} else {
				viewItemsGroupButtons(mProduct.kod);
			}
		}
	};
	
	private void viewItemsGroupButtons(String kod) {
		
		//ArrayList<Map<String, String>> listData = mListData.получить—писок атегорий(kod);
		ArrayList<Product> list = Product.getGroupList(sqdb, kod);
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		EL_LIST_BUTTONS.removeAllViews();
		//int i = 0;
		for (Product data : list) {
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(data.kod);
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(data.name);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewItemsGroupTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewItemsGroupClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	OnClickListener viewItemsGroupClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			String name = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
			
			for (Product data : product.hierarchyList) {
				
				if (data.hierarchy!=0||data==product.mainParent) continue;
				
				product.hierarchyList.clear();
				product.hierarchyList.add(product.mainParent);
				
				//product.hierarchyList.remove(data);
				break;
			}
			
			product.hierarchy++;
			
			Product mProduct	= new Product();
			mProduct.kod		= kod;
			mProduct.name		= name;
			mProduct.hierarchy	= product.hierarchy;
			
			product.hierarchyList.add(mProduct);
			
			if (product.hierarchy==user.hierarchy) {
				createItems(mProduct.kod);
			} else {
				viewItemsGroupButtons(mProduct.kod);
			}
			
			viewItemsTopButtons();
		}
	};
	
	private void addTopCategory(String kod, String name, String text) {
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items_k);
		
		//items.removeAllViews();
		//View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
		View item = ltInflater.inflate(R.layout.item_main, items, false);
		((TextView) item.findViewById(R.id.tree_item_kod)).setText(kod);
		((TextView) item.findViewById(R.id.tree_item_name1)).setText(name);
		((TextView) item.findViewById(R.id.tree_item_name2)).setText(text);
		
		((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(topCategoryTouch);
		
		items.addView(item);
		
	}
	
	private void removeTopCategory(int text) {
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items_k);
		
		try {
			items.removeViewAt(text);
		} catch (Exception e) {
		}
	}
	
	OnTouchListener topCategoryTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String код = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String ключ = ((TextView) v.findViewById(R.id.tree_item_name2)).getText().toString();
				
				if (ключ.equals("1")) {
					removeTopCategory(2);
					viewItemsPKatButtons(код);
				} else {
					//viewItemsPKatButtons(код);
				}
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private void viewItemsKatButtons(String kod) {
		
		//ArrayList<Map<String, String>> listData = mListData.получить—писок атегорий(kod);
		//ArrayList<Map<String, String>> listData = product.getCategoryList(kod);
		ArrayList<Map<String, String>> listData = new ArrayList<Map<String,String>>();
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		items.removeAllViews();
		for (Map<String, String> m : listData) {
			
			View item = ltInflater.inflate(R.layout.item_menu_2, EL_LIST_BUTTONS, false);
			//View item = ltInflater.inflate(R.layout.tree_item, items, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(m.get("kod"));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(m.get("name"));
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewItemsKatTouch);
			
			items.addView(item);
		}	
	}
	
	OnTouchListener viewItemsKatTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				String код = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String им€ = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				// delete top buttons
				int toDelete = mainGroupIndex;
				toDelete++;
				
				for (int i = toDelete; i < 5; i++) {
					removeTopCategory(i);
				}
				for (int i = 0; i < mainGroupIndex; i++) {
					removeTopCategory(i);
				}
				
				addTopCategory(код, им€, "1");
				
				viewItemsPKatButtons(код);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private void viewItemsPKatButtons(String kod) {
		
		if (kod.equals("")) {
			return;
		}
		
		//ArrayList<Map<String, String>> listData = mListData.получить—писокѕодкатегорий(kod);
		//ArrayList<Map<String, String>> listData = product.getCategoryList(kod);
		ArrayList<Map<String, String>> listData = new ArrayList<Map<String,String>>();
		
		LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		items.removeAllViews();
		for (Map<String, String> m : listData) {
			View item = ltInflater.inflate(R.layout.tree_item, items, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(m.get("kod"));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(m.get("name"));
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewItemsPKatTouch);
			
			items.addView(item);
		}
		
		itemsList = false;
	}
	
	OnTouchListener viewItemsPKatTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				String код = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String им€ = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				removeTopCategory(2);
				addTopCategory(код, им€, "2");
				
				createItems(код);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private void createItems(String kod) {
		
		if (kod.equals("")) {
			return;
		}
		thisItemParent = kod;
		itemsList = true;
		
		switch (mScreen) {
		case SCREEN_ITEMS:
			viewItemsButtons(kod);
			break;
		case SCREEN_SETPRICE:
			//tabPrice.fillTable(kod);
			createPriceItemsButtons(kod);
			break;
		}
	}
	
	private void viewItemsButtons(String kod) {
		
		//ArrayList<String[]> listData;
		//ArrayList<Map<String, String>> listData = null;
		//boolean noBalance = ((CheckBox) findViewById(R.id.checkRest)).isChecked();
		boolean noBalance = true;
		
		//String priceType = settings[ListData.SET_PRICE][ListData.VALUE];
		//String stock = settings[ListData.SET_STOCK][ListData.VALUE];
		String stock = settings[Order.STOCK][KOD];
		
		t_items = Product.getItemsListOrder(sqdb, kod, trt.kod, stock, noBalance);
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		EL_LIST_BUTTONS.removeAllViews();
		
		int i = 0;
		for (String[] data : t_items) {
			
			//View item = ltInflater.inflate(R.layout.items, items, false);
			View item = ltInflater.inflate(R.layout.items_products, EL_LIST_BUTTONS, false);
			((TextView) item.findViewById(R.id.kod)).setText(Integer.toString(i++));
			//((TextView) item.findViewById(R.id.kod)).setText(data[KOD]);
			((TextView) item.findViewById(R.id.name)).setText(data[NAME]);
			((TextView) item.findViewById(R.id.price)).setText(data[PRICE].replace(',', '.'));
			((TextView) item.findViewById(R.id.left)).setText(data[REST]);
			
			((TextView) item.findViewById(R.id.kaficient)).setText("/"+data[K2]+"/"+data[K3]);
			
			((LinearLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewItemsClick);
			
			int j = 0;
			for (String[] map : order.table) {
				j++;
				if (map[KOD].equals(data[KOD])) {
					((TextView) item.findViewById(R.id.kod_order)).setText(Integer.toString(j));
					((TextView) item.findViewById(R.id.amount)).setText(map[Order.TABLE_AMOUNT]);
					((TextView) item.findViewById(R.id.sum)).setText(map[Order.TABLE_SUM]);
				}
			}
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	OnClickListener viewItemsClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			toChange = v;
			
			String name = ((TextView) v.findViewById(R.id.name)).getText().toString();
			String price = ((TextView) v.findViewById(R.id.price)).getText().toString();
			String amount = ((TextView) v.findViewById(R.id.amount)).getText().toString();
			String rest = ((TextView) v.findViewById(R.id.left)).getText().toString();
			
			Intent intent = new Intent(MainActivity.this, Calculator.class);
			intent.putExtra("itemsType","0");
			intent.putExtra("name",		name);
			intent.putExtra("price",	price);
			intent.putExtra("parent",	"");
			intent.putExtra("check",	"");
			intent.putExtra("остаток",	rest);
			intent.putExtra("количество",amount);
			
			startActivityForResult(intent, 2);
		}
	};
	
	OnTouchListener viewItemsTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				toChange = v;
				
				String name = ((TextView) v.findViewById(R.id.name)).getText().toString();
				String price = ((TextView) v.findViewById(R.id.price)).getText().toString();
				String amount = ((TextView) v.findViewById(R.id.amount)).getText().toString();
				String rest = ((TextView) v.findViewById(R.id.left)).getText().toString();
				
				Intent intent = new Intent(MainActivity.this, Calculator.class);
				intent.putExtra("itemsType","0");
				intent.putExtra("name",		name);
				intent.putExtra("price",	price);
				intent.putExtra("parent",	"");
				intent.putExtra("check",	"");
				intent.putExtra("остаток",	rest);
				intent.putExtra("количество",amount);
				
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
		String itemsType = arg2.getStringExtra("itemsType");
		
		if (!itemsType.equals("0")) {
			
			/*switch (toChange.getId()) {
			case R.id.pt_item_price1:
				((TextView) toChange.findViewById(R.id.price1)).setText(result);
				tabPrice.line[PriceTRT.PRICE1] = result;
				break;
			case R.id.pt_item_price2:
				((TextView) toChange.findViewById(R.id.price2)).setText(result);
				tabPrice.line[PriceTRT.PRICE2] = result;
				break;
			case R.id.tree_item_main:
				if (priceTRTNewLine.priceType.equals(sellType.aboveShell)) {
					((TextView) toChange.findViewById(R.id.price1)).setText(result);
					tabPrice.line[TablePriceTRT.PRICE1] = result;
				} else if  (priceTRTNewLine.priceType.equals(sellType.underShell)) {
					((TextView) toChange.findViewById(R.id.price2)).setText(result);
					tabPrice.line[TablePriceTRT.PRICE2] = result;
				}
			}*/
			
			if (itemsType.equals("1")) {
				
				((TextView) toChange.findViewById(R.id.price1)).setText(result);
				tabPrice.line[PriceTRT.PRICE1] = result;
			} else {
				
				((TextView) toChange.findViewById(R.id.price2)).setText(result);
				tabPrice.line[PriceTRT.PRICE2] = result;
			}
			
			((TextView) toChange.findViewById(R.id.comment)).setText("("+tabPrice.line[PriceTRT.COMMENT]+")");
			
			priceTRTNewLine = priceTRT.addline();
			
			priceTRTNewLine.itemKod	= tabPrice.line[PriceTRT.ITEM_KOD];
			priceTRTNewLine.price1	= tabPrice.line[PriceTRT.PRICE1];
			priceTRTNewLine.price2	= tabPrice.line[PriceTRT.PRICE2];
			priceTRTNewLine.comment	= tabPrice.line[PriceTRT.COMMENT];
			
			//priceTRTNewLine.itemKod = tabPrice.line[TablePriceTRT.KOD];
			//priceTRTNewLine.price = result;
			
			priceTRT.save();
			if (callSetPriceAgain) {
				setPriceType();
				callSetPriceAgain = false;
			};
			return;
		}
		
		Double k = Double.parseDouble(result);
		
		String sIndexOrder = ((TextView) toChange.findViewById(R.id.kod_order)).getText().toString();
		
		int indexOrder = Integer.parseInt(sIndexOrder);
		
		if (indexOrder<0 & k==0.0) {return;}
		
		if (indexOrder<0) {
			
			String index = ((TextView) toChange.findViewById(R.id.kod)).getText().toString();
			line = t_items.get(Integer.parseInt(index));
			
			String[] newData = new String[Order.TABLE_COUNT];
			newData[Order.TABLE_KOD]	= line[KOD];
			newData[Order.TABLE_ITEM]	= line[NAME];
			newData[Order.TABLE_PRICE]	= line[PRICE];
			
			order.table_line = newData;
			
			((TextView) toChange.findViewById(R.id.kod_order)).setText(Integer.toString(order.table.size()));
			
		} else {
			
			order.table_line = order.table.get(indexOrder);
			
		}
		
		DecimalFormat precision = new DecimalFormat("0.00");
		
		//Double price	= Double.parseDouble(line[PRICE].replace(',', '.'));
		Double price	= Double.parseDouble(order.table_line[Order.TABLE_PRICE].replace(',', '.'));
		Double newSum	= k * price;
		
		newSum = new BigDecimal(newSum).setScale(2, RoundingMode.HALF_UP).doubleValue();
		String newSSum = precision.format(newSum).replace(',', '.');
		
		((TextView) toChange.findViewById(R.id.amount)).setText(result);
		((TextView) toChange.findViewById(R.id.sum)).setText(newSSum);
		
		if (price <= 0 | k <= 0) {
			
			Log.d(TAG, "indexOrder = "+indexOrder);
			if (indexOrder>=0) {
				order.table.remove(indexOrder);
			}
			
		} else {
			
			order.table_line[Order.TABLE_AMOUNT]	= result;
			order.table_line[Order.TABLE_SUM]		= newSSum;
			
			if (indexOrder<0) {
				order.table.add(order.table_line);
			}
		}
		
		/*switch (mScreen) {
		case SCREEN_ORDER:
			
			order.table_line = order.table.get(indexOrder);
			
			DecimalFormat precision = new DecimalFormat("0.00");
			
			Double price	= Double.parseDouble(line[PRICE].replace(',', '.'));
			Double newSum	= k * price;
			
			newSum = new BigDecimal(newSum).setScale(2, RoundingMode.HALF_UP).doubleValue();
			String newSSum = precision.format(newSum).replace(',', '.');
			
			((TextView) toChange.findViewById(R.id.amount)).setText(result);
			((TextView) toChange.findViewById(R.id.sum)).setText(newSSum);
			
			if (price <= 0 | k <= 0) {
				
				order.table.remove(indexOrder);
				
			} else {
				
				order.table_line[Order.TABLE_AMOUNT]	= result;
				order.table_line[Order.TABLE_SUM]		= newSSum;
			}
			break;
		case SCREEN_ITEMS:
			
			break;
		}
		
		String index = ((TextView) toChange.findViewById(R.id.kod)).getText().toString();
		line = t_items.get(Integer.parseInt(index));
		
		//String i = ((TextView) toChange.findViewById(R.id.kod_order)).getText().toString();
		
		//int indexOrder = Integer.parseInt(i);
		
		if (indexOrder>=0) {
			
			order.table_line = order.table.get(indexOrder);
			
		} else {
			
			String[] newData = new String[Order.TABLE_COUNT];
			newData[Order.TABLE_KOD]	= line[KOD];
			newData[Order.TABLE_ITEM]	= line[NAME];
			newData[Order.TABLE_PRICE]	= line[PRICE];
			
			order.table_line = newData;
		}
		
		DecimalFormat precision = new DecimalFormat("0.00");
		
		Double price	= Double.parseDouble(line[PRICE].replace(',', '.'));
		Double newSum	= k * price;
		
		newSum = new BigDecimal(newSum).setScale(2, RoundingMode.HALF_UP).doubleValue();
		String newSSum = precision.format(newSum).replace(',', '.');
		
		((TextView) toChange.findViewById(R.id.amount)).setText(result);
		((TextView) toChange.findViewById(R.id.sum)).setText(newSSum);
		
		if (price <= 0 | k <= 0) {
			
			Log.d(TAG, "indexOrder = "+indexOrder);
			if (indexOrder>=0) {
				order.table.remove(indexOrder);
			}
			
		} else {
			
			order.table_line[Order.TABLE_AMOUNT]	= result;
			order.table_line[Order.TABLE_SUM]		= newSSum;
			
			if (indexOrder<0) {
				order.table.add(order.table_line);
			}
		}*/
	}
	
	callBack viewOrder = new callBack() {
		public void onBack() {
			
			setMenuOrder();
			viewSettings();
			setViewSettings();
			EL_MENU.setVisibility(View.VISIBLE);
			
			animate(SCREEN_ORDER);
			
			/*currentButton.setSelected(true);
			mMenu = Order.MAIN_ORDER;
			showOrderMenu();*/
		}
	};
	
	callBack viewOrder1 = new callBack() {
		public void onBack() {
			
			//setMenuOrder();
			//viewSettings();
			setViewSettings();
			EL_MENU.setVisibility(View.VISIBLE);
			
			currentButton.setSelected(true);
			mMenu = Order.MAIN_ORDER;
			showOrderMenu();
		}
	};
	
	private void viewOrder() {
		
		viewSettings();
		setViewSettings();
		
		viewOrderButtons();
	
		callBack back = new callBack() {
			public void onBack() {}
		};
		animateTopTranslateShow1(back);
		
		callBack back1 = new callBack() {
			public void onBack() {
				mMenu = Order.MAIN_ORDER;
				showOrderMenu();
			}
		};
		animateMenuTranslateShow1(back1);
		
		//отобразить“екущий«аказ();
		//viewOrderTableButtons();
	}
	
	private void viewOrderBack() {
		
		viewSettings();
		
		viewOrderButtons();
	
		callBack back = new callBack() {
			public void onBack() {}
		};
		animateTopTranslateShow1(back);
		
		callBack back1 = new callBack() {
			public void onBack() {
				setViewSettings();
				//создатьѕравый—писок();
				currentButton.setSelected(true);
				mMenu = Order.MAIN_ORDER;
				showOrderMenu();
			}
		};
		animateMenuTranslateShow1(back1);
		
		//отобразить“екущий«аказ();
		//viewOrderTableButtons();
	}
	
	private void viewOrderButtons() {
		
		EL_MENU_ITEMS.removeAllViews();
		for (int i = 0; i < order.mainList.length; i++) {
			
			View item = ltInflater.inflate(R.layout.item_menu_1, EL_MENU_ITEMS, false);
			((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			((TextView) item.findViewById(R.id.tree_item_name1)).setText(order.mainList[i]);
			
			item.setBackgroundDrawable(drawables[i]);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewOrderClick);
			
			((ImageView) item.findViewById(R.id.menu1_image)).setImageResource(images3[i]);
			
			if (i==Order.MAIN_ORDER) {
				
				currentButton = item;
			}
			EL_MENU_ITEMS.addView(item);
		}
	}
	
	OnClickListener viewOrderClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if (currentButton!=null) {
				currentButton.setSelected(false);
			}
			currentButton = v;
			v.setSelected(true);
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			mMenu = Integer.parseInt(kod);
			
			if (EL_LIST_BUTTONS.getChildCount()==0) {
				showOrderMenu();
				return;
			}
			
			callBack back = new callBack() {
				public void onBack() {
					showOrderMenu();
				}
			};
			animateButtonsGone(back);
		}
	};
	
	void showOrderMenu() {
		
		switch (mMenu) {
		case Order.MAIN_SELL:
			viewTRTSell();
			break;
		case Order.MAIN_ORDER:
			viewOrderTableButtons();
			break;
		case Order.MAIN_COMMENT:
			viewOrderComment();
			break;
		case Order.MAIN_CLOSE:
			viewOrderSave();
			break;
		}
		animateButtonsShow();
	}
	
	private void viewOrderTableButtons() {
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		//items.removeAllViews();
		
		//for (int i = 0; i < order.table.length; i++) {
		int i = 0;
		for (String[] data : order.table) {
			
			//View item = ltInflater.inflate(R.layout.item, items, false);
			View item = ltInflater.inflate(R.layout.item_order, EL_LIST_BUTTONS, false);
			//((TextView) item.findViewById(R.id.tree_item_kod)).setText(Integer.toString(i));
			//((TextView) item.findViewById(R.id.tree_item_name1)).setText(trt.sellList[i]);
			
			((TextView) item.findViewById(R.id.kod)).setText(Integer.toString(i));
			//((TextView) item.findViewById(R.id.kod)).setText(data[Order.TABLE_KOD]);
			((TextView) item.findViewById(R.id.name)).setText(data[Order.TABLE_ITEM]);
			((TextView) item.findViewById(R.id.price)).setText(data[Order.TABLE_PRICE]);
			((TextView) item.findViewById(R.id.amount)).setText(data[Order.TABLE_AMOUNT]);
			((TextView) item.findViewById(R.id.sum)).setText(data[Order.TABLE_SUM]);
			
			((TextView) item.findViewById(R.id.kod_order)).setText(Integer.toString(i++));
			
			int[] mK = Product.getItemKoef(sqdb, data[Order.TABLE_KOD]);

			int amount = Integer.parseInt(data[Order.TABLE_AMOUNT]);
			//int k2 = Integer.parseInt(mK.get("k2"));
			//int k3 = Integer.parseInt(mK.get("k3"));
			
			int k2 = (mK[0]==0? 0: amount / mK[0]);
			int k3 = (mK[1]==0? 0: amount / mK[1]);
			
			((TextView) item.findViewById(R.id.kaficient)).setText("/"+k2+"/"+k3);
			
			//((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewItemsTouch);
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnClickListener(viewItemsClick);
			
			EL_LIST_BUTTONS.addView(item);
		}
	}
	
	private void viewOrderComment() {
		
		String name = settings[Order.COMMENT][NAME];
		
		/*String им€ = "";
		//String парметр = "";
		
		for (Map<String, String> map : списокЌастроек) {
			if (map.get("name").equals(" омментарий")) {
				им€ = map.get("valueName");
				//парметр = map.get("value");
			}
		}
		ввести омментарий(им€);*/
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(" омментарий");
		alert.setMessage("¬ведите текст");
		final EditText input = new EditText(this);
		input.setText(name);
		alert.setView(input);

		alert.setPositiveButton("ќ ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				//Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
				/*for (Map<String, String> m : списокЌастроек) {
					if (m.get("kod").equals("6")) {
						m.put("value", value);
						m.put("valueName", value);						
					}
				}*/
				//settings[ListData.SET_COMM][KOD] = value;
				//settings[ListData.SET_COMM][NAME] = value;
				//setSettings(" омментарий", value);
				//сохранитьЌастройку“–“(" омментарий", value);
				//trt.save(Order.COMMENT, value);
				
				settings[Order.COMMENT][NAME] = value;
				//setSetting(ListData.SET_COMM, value, value);
			}
		});
		alert.setNegativeButton("ќтмена", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		alert.show();
	}
	
	private void viewOrderSave() {
		
		order.setSettings(settings);
		order.save(trt.kod);
		
		currentButton.setSelected(false);
		changeScreen(SCREEN_TRT);
		
		/*Calendar ccc = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int дата = Integer.parseInt(formatter.format(ccc.getTime()));
		
        String датаќтгрузки	= "";
        String  онтрагент	= "";
        String тип÷ены		= "";
        String ¬идќперации	= "";
        String —клад		= "";        
        String  омментарий	= "";
        
        for (Map<String, String> m : списокЌастроек) {
        	
        	String mName = m.get("name");
        	String value = m.get("value");
        	
        	if (mName.equals("ƒата")) {
        		датаќтгрузки = value;
        	} else if (mName.equals(" онтрагент")) {
        		 онтрагент = value;
        	} else if (mName.equals("“ип цен")) {
        		тип÷ены = value;
        	} else if (mName.equals("¬ид операции")) {
        		¬идќперации = value;
        	} else if (mName.equals("—клад")) {
        		—клад = value;
        	} else if (mName.equals(" омментарий")) {
        		 омментарий = value;
        	}
		}
        Log.d(TAG, "ƒатаќтгрузки = "+датаќтгрузки);
        Log.d(TAG, " онтрагент = "	+ онтрагент);
        Log.d(TAG, "“ип÷ены = "		+тип÷ены);
        Log.d(TAG, "¬идќперации = "	+¬идќперации);
        Log.d(TAG, "—клад = "		+—клад);        
        Log.d(TAG, " омментарий = "	+ омментарий);
        
        if (!orderList.isEmpty()) {
        
	        String total = ((TextView) findViewById(R.id.total)).getText().toString();
			Double сумма»тог = Double.parseDouble(total);
	        
			int zakazId = 0;
			
			if (orderNumber==0) {
				zakazId = sqdb.saveZakazHeadData(currentTRT, дата, датаќтгрузки,  онтрагент, тип÷ены, ¬идќперации,  омментарий, —клад, сумма»тог);
				//if (zakazId < 0) return false;
			} else {
				zakazId = orderNumber;
				sqdb.updateDocData(датаќтгрузки,  онтрагент, тип÷ены, ¬идќперации,  омментарий, —клад, сумма»тог, zakazId);
				sqdb.deleteDocTable(Integer.toString(zakazId));
			}
			sqdb.saveZakazTableData(zakazId, orderList);
	        //if (count < 0) return false;
        }

        orderNumber = 0;
        
		orderList = new ArrayList<Map<String, String>>();
		
		создать—писокЌастроек();
		
		((TextView) findViewById(R.id.total)).setText("0.0");
		
		createMainOptions();
		
		viewTRT();*/
		
	}
	
	public void viewSetPrice() {
		
		thisScreen = SCREEN_SETPRICE;
		priceTRT = new PriceTRT(trt.kod, sqdb);
		viewItems();
	}
	
	callBack viewSetPrice = new callBack() {
		public void onBack() {
			
			priceTRT = new PriceTRT(trt.kod, sqdb);
			
			product.setHierarchy(sqdb);
			viewSettings();
			viewItemsTopButtons();
			EL_HEAD_SETPRICE.setVisibility(View.VISIBLE);
			//viewItemsMenu();
			
			EL_TOP_BACK = (FrameLayout) EL_TOP_MENU.findViewById(R.id.top_back);
			EL_TOP_BACK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeScreen(SCREEN_TRT); 
				}
			});
			animate(SCREEN_SETPRICE);
		}
	};
	
	private void createPriceItemsButtons(String kod) {
		
		tabPrice.setTable(kod);
		
		EL_LIST_BUTTONS.removeAllViews();
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		//items.removeAllViews();
		
		int i = 0;
		for (String[] data : tabPrice.data) {
			
			View item = ltInflater.inflate(R.layout.item_set_price, EL_LIST_BUTTONS, false);
			
			((TextView) item.findViewById(R.id.dataIndex)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.kod)).setText(data[PriceTRT.ITEM_KOD]);
			((TextView) item.findViewById(R.id.name)).setText(data[PriceTRT.ITEM_NAME]);
			((TextView) item.findViewById(R.id.comment)).setText("("+data[PriceTRT.COMMENT]+")");
			((TextView) item.findViewById(R.id.price1)).setText(data[PriceTRT.PRICE1]);
			((TextView) item.findViewById(R.id.price2)).setText(data[PriceTRT.PRICE2]);
			
			((FrameLayout) item.findViewById(R.id.pt_item_name)).setOnClickListener(viewPriceClick);
			((FrameLayout) item.findViewById(R.id.pt_item_price1)).setOnClickListener(viewPriceClick);
			((FrameLayout) item.findViewById(R.id.pt_item_price2)).setOnClickListener(viewPriceClick);
			
			//((LinearLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(touchPriceItems);
			/*((FrameLayout) item.findViewById(R.id.pt_item_name)).setOnTouchListener(touchPrice);
			((FrameLayout) item.findViewById(R.id.pt_item_price1)).setOnTouchListener(touchPrice);
			((FrameLayout) item.findViewById(R.id.pt_item_price2)).setOnTouchListener(touchPrice);*/
			EL_LIST_BUTTONS.addView(item);
		}
	}

	OnClickListener viewPriceClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			View parent = (View) v.getParent().getParent().getParent();
			
			toChange = parent;
			
			String dataIndex = ((TextView) parent.findViewById(R.id.dataIndex)).getText().toString();
			int index = Integer.parseInt(dataIndex);
			
			tabPrice.setCurrentLine(index);
			
			switch (v.getId()) {
			case R.id.pt_item_name:
				setPriceType();
				break;
			case R.id.pt_item_price1:
				startCalculator(tabPrice.line[PriceTRT.PRICE1], "1");
				break;
			case R.id.pt_item_price2:
				startCalculator(tabPrice.line[PriceTRT.PRICE2], "2");
				break;
			}
		}
	};
	
	OnTouchListener touchPrice = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				//toChange = v;
				View parent = (View) v.getParent().getParent().getParent();
				
				toChange = parent;
				
				String dataIndex = ((TextView) parent.findViewById(R.id.dataIndex)).getText().toString();
				int index = Integer.parseInt(dataIndex);
				
				tabPrice.setCurrentLine(index);
				
				/*priceTRTNewLine = priceTRT.addline();
				
				priceTRTNewLine.itemKod	= tabPrice.line[PriceTRT.ITEM_KOD];
				priceTRTNewLine.price1	= tabPrice.line[PriceTRT.PRICE1];
				priceTRTNewLine.price2	= tabPrice.line[PriceTRT.PRICE2];
				priceTRTNewLine.comment	= tabPrice.line[PriceTRT.COMMENT];*/
				
				switch (v.getId()) {
				case R.id.pt_item_name:
					//toChange = parent;
					setPriceType();
					break;
				case R.id.pt_item_price1:
					//priceTRTNewLine.priceType = sellType.aboveShell;
					//priceTRTNewLine.price = tabPrice.line[TablePriceTRT.PRICE1];
					startCalculator(tabPrice.line[PriceTRT.PRICE1], "1");
					break;
				case R.id.pt_item_price2:
					//priceTRTNewLine.priceType = sellType.underShell;
					//priceTRTNewLine.price = tabPrice.line[TablePriceTRT.PRICE2];
					startCalculator(tabPrice.line[PriceTRT.PRICE2], "2");
					break;
				}
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	private void setPriceType() {
		
		callSetPriceAgain = true;
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("“ип цены");
		String message = "";
		message = message + tabPrice.line[PriceTRT.ITEM_NAME] + "\n";
		message = message + sellType.aboveShell + " = " + tabPrice.line[PriceTRT.PRICE1] + "\n";
		message = message + sellType.underShell + " = " + tabPrice.line[PriceTRT.PRICE2] + "\n\n";
		message = message + " омментарий:";
		alert.setMessage(message);
		final EditText input = new EditText(this);
		input.setText(tabPrice.line[PriceTRT.COMMENT]);
		alert.setView(input);
		
		alert.setPositiveButton("ѕод полкой", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//priceTRTNewLine.priceType	= sellType.underShell;
				//priceTRTNewLine.price		= tabPrice.line[TablePriceTRT.PRICE2];
				tabPrice.line[PriceTRT.COMMENT] = input.getText().toString();
				startCalculator(tabPrice.line[PriceTRT.PRICE2], "2");
			}
		});
		alert.setNegativeButton("Ќад полкой", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//priceTRTNewLine.priceType	= sellType.aboveShell;
				//priceTRTNewLine.price		= tabPrice.line[TablePriceTRT.PRICE1];
				tabPrice.line[PriceTRT.COMMENT] = input.getText().toString();
				startCalculator(tabPrice.line[PriceTRT.PRICE1], "1");
			}
		});
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				tabPrice.line[PriceTRT.COMMENT] = input.getText().toString();
				
				priceTRTNewLine			= priceTRT.addline();
				priceTRTNewLine.itemKod	= tabPrice.line[PriceTRT.ITEM_KOD];
				priceTRTNewLine.price1	= tabPrice.line[PriceTRT.PRICE1];
				priceTRTNewLine.price2	= tabPrice.line[PriceTRT.PRICE2];
				priceTRTNewLine.comment	= tabPrice.line[PriceTRT.COMMENT];
				
				priceTRT.save();
				
				((TextView) toChange.findViewById(R.id.comment)).setText("("+tabPrice.line[PriceTRT.COMMENT]+")");
			}
		});

		alert.show();
	}
	
	public void startCalculator(String price, String type) {
		
		//String price = priceTRTNewLine.price;
		
		Intent intent = new Intent(MainActivity.this, CalculatorPrice.class);
		intent.putExtra("itemsType",type);
		intent.putExtra("price",	price);
		
		//intent.putExtra("name",	"");
		//intent.putExtra("parent",	"");
		//intent.putExtra("check",	"");
		//intent.putExtra("остаток",	остаток);
		//intent.putExtra("количество",количество);
		
		startActivityForResult(intent, 2);
		
	}
	
	private void setSetting(int column, String name, String value) {
		
		settings[column][ListData.NAME] = name;
		settings[column][ListData.VALUE] = value;
		
		int trtColumn = mListData.getSettingsColumn(column);
		
		setSettings(settings[column][KOD], name);
		
		if (column==ListData.SET_DATE | column==ListData.SET_COMM) { return; }
		trt.saveSetting(trtColumn, value);
	}
	
	private void setViewSettingsClear() {
		
		TextView item = null;
		
		item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_date);
		item.setText("ƒата");
		
		item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_kontr);
		item.setText(" онтрагент");
		
		item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_oper);
		item.setText("ќпераци€");
		
		item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_stock);
		item.setText("—клад");
	}
	
	private void setViewSettings() {
		
		TextView item = null;
		
		for (int i = 0; i < settings.length; i++) {
			
			switch (i) {
			case Order.DATE_SALE:
				item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_date);
				item.setText(settings[i][1]==null? "ƒата": settings[i][1]);
				break;
			case Order.KONTR:
				item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_kontr);
				item.setText(settings[i][1]);
				break;
			case Order.STOCK:
				item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_stock);
				item.setText(settings[i][1]);
				break;
			case Order.OPER:
				item = (TextView) EL_TOP_MENU.findViewById(R.id.setting_oper);
				item.setText(settings[i][1]);
				break;
			}
		}
	}
	
	private void setSettings(String valueName, String value) {
		
		/*boolean valueFind = false;
		TextView t = null;
		
		//Log.d(TAG, "им€ѕараметра - "+valueName+", параметр - " + value);
		
		LinearLayout l = (LinearLayout) findViewById(R.id.items_k);
		
		for (int i = 0; i < l.getChildCount(); i++) {
			
			View vv = l.getChildAt(i);
			String valueThis = "";
			
			t = (TextView) vv.findViewById(R.id.name1);
			if (t!=null) {
				valueThis = t.getText().toString();
				if (valueThis.equals(valueName)){
					valueFind = true;
					break;
			}}
			t = (TextView) vv.findViewById(R.id.name2);
			if (t!=null) {
				valueThis = t.getText().toString();
				if (valueThis.equals(valueName)){
					valueFind = true;
					break;
			}}
			t = (TextView) vv.findViewById(R.id.name3);
			if (t!=null) {
				valueThis = t.getText().toString();
				if (valueThis.equals(valueName)){
					valueFind = true;
					break;
			}}
		}
		if (!valueFind){
			return;
		}
		
		LinearLayout parent = (LinearLayout) t.getParent().getParent();
		
		switch (t.getId()) {
		case R.id.name1:
			((TextView) parent.findViewById(R.id.name11)).setText(value);
			break;
		case R.id.name2:
			((TextView) parent.findViewById(R.id.name22)).setText(value);
			break;
		case R.id.name3:
			((TextView) parent.findViewById(R.id.name33)).setText(value);
			break;
		default:
			break;
		}*/
		
	}
	
	private boolean checkSettings() {
		
		boolean result = true;
		
		String text = "";
        
		if (settings[Order.DATE_SALE][NAME]==null) {
        	text = "Ќе заполнена дата отгрузки.";
        } else if (settings[Order.KONTR][NAME]==null) {
        	text = "Ќе заполнен контрагент.";
        //} else if (settings[ListData.SET_PRICE][NAME]==null) {
        //	text = "Ќе заполнен тип цен.";
        } else if (settings[Order.STOCK][NAME]==null) {
        	text = "Ќе заполнен вид операции.";
        } else if (settings[Order.OPER][NAME]==null) {
        	text = "Ќе заполнен склад.";
        }
		
		/*if (settings[ListData.SET_DATE][NAME]==null) {
        	text = "Ќе заполнена дата отгрузки.";
        } else if (settings[ListData.SET_KONTR][NAME]==null) {
        	text = "Ќе заполнен контрагент.";
        //} else if (settings[ListData.SET_PRICE][NAME]==null) {
        //	text = "Ќе заполнен тип цен.";
        } else if (settings[ListData.SET_OPER][NAME]==null) {
        	text = "Ќе заполнен вид операции.";
        } else if (settings[ListData.SET_STOCK][NAME]==null) {
        	text = "Ќе заполнен склад.";
        }*/
		
        /*if (settings[ListData.SET_DATE][NAME].equals("")) {
        	text = "Ќе заполнена дата отгрузки.";
        } else if (settings[ListData.SET_KONTR][NAME].equals("")) {
        	text = "Ќе заполнен контрагент.";
        } else if (settings[ListData.SET_PRICE][NAME].equals("")) {
        	text = "Ќе заполнен тип цен.";
        } else if (settings[ListData.SET_OPER][NAME].equals("")) {
        	text = "Ќе заполнен вид операции.";
        } else if (settings[ListData.SET_STOCK][NAME].equals("")) {
        	text = "Ќе заполнен склад.";
        }*/
		
		/*String датаќтгрузки	= "";
        String  онтрагент	= "";
        String тип÷ены		= "";
        String ¬идќперации	= "";
        String —клад		= "";
        
        for (Map<String, String> m : списокЌастроек) {
        	
        	String mName = m.get("name");
        	String value = m.get("value");
        	
        	if (mName.equals("ƒата")) {
        		датаќтгрузки = value;
        	} else if (mName.equals(" онтрагент")) {
        		 онтрагент = value;
        	} else if (mName.equals("“ип цен")) {
        		тип÷ены = value;
        	} else if (mName.equals("¬ид операции")) {
        		¬идќперации = value;
        	} else if (mName.equals("—клад")) {
        		—клад = value;
        	}
		}
        
        if (датаќтгрузки.equals("")) {
        	text = "Ќе заполнена дата отгрузки.";
        } else if ( онтрагент.equals("")) {
        	text = "Ќе заполнен контрагент.";
        } else if (тип÷ены.equals("")) {
        	text = "Ќе заполнен тип цен.";
        } else if (¬идќперации.equals("")) {
        	text = "Ќе заполнен вид операции.";
        } else if (—клад.equals("")) {
        	text = "Ќе заполнен склад.";
        }*/
		
        if (!text.equals("")) {
        	
        	result = false;
        	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
		return result;
	}
	
	private void viewSettings1() {
		
		View item;
		
		switch (mScreen) {
		case SCREEN_TRT:
			
			item = ltInflater.inflate(LAYOUT_SETTINGS, EL_TOP_MENU, false);
			EL_TOP_MENU.addView(item);
			
			/*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
			EL_TOP_MENU.getLayoutParams();
			params.weight = 7;
			EL_TOP_MENU.setLayoutParams(params);*/
			break;
		case SCREEN_ITEMS:
			
			item = ltInflater.inflate(LAYOUT_HIERARCHY, EL_TOP_MENU, false);
			EL_TOP_MENU.addView(item);
			
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
			EL_TOP_MENU.getLayoutParams();
			params.weight = 7;
			EL_TOP_MENU.setLayoutParams(params);
			break;
		}
		
		
		
		/*LinearLayout items = (LinearLayout) findViewById(R.id.items_k);
		View item = ltInflater.inflate(R.layout.tree_item_k, items, false);
		
		int k = 0;
		items.removeAllViews();
		for (String[] data : settings) {
			
			k++;
			
			if (k==1) {
				((TextView) item.findViewById(R.id.name1)).setText(data[KOD]);
				((TextView) item.findViewById(R.id.name11)).setText(data[NAME]);
				((FrameLayout) item.findViewById(R.id.touch1)).setOnTouchListener(dateTouch);
			} else if(k==2) {
				((TextView) item.findViewById(R.id.name2)).setText(data[KOD]);
				((TextView) item.findViewById(R.id.name22)).setText(data[NAME]);
				((FrameLayout) item.findViewById(R.id.touch2)).setOnTouchListener(dateTouch);
			} else if(k==3) {
				((TextView) item.findViewById(R.id.name3)).setText(data[KOD]);
				((TextView) item.findViewById(R.id.name33)).setText(data[NAME]);
				((FrameLayout) item.findViewById(R.id.touch3)).setOnTouchListener(dateTouch);
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
		}*/
	}
	
	OnTouchListener selectTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				if (user.role!=User.ROLE_PRICE) {
					if (!(thisScreen == SCREEN_SETPRICE) && !checkSettings()) {
						v.setBackgroundColor(0);
						return true;
					}
				}
				
				if (thisScreen == SCREEN_TRT) {
					
					switch (user.role) {
					case User.ROLE_PRICE:
						thisScreen = SCREEN_SETPRICE;
						viewSetPrice();
						break;
					default:
						thisScreen = SCREEN_TRT;
						viewItems();
						//EL_BUTTONS.removeAllViews();
						изменитьЌаименование нопки("ѕодбор");
						break;
					}
					//изменитьЌаименование нопки("¬ заказ");
					//((CheckBox) findViewById(R.id.checkRest)).setChecked(true);
					//changeResult(0.0);
				
				} else if (thisScreen == SCREEN_ITEMS) {
					
					thisScreen = SCREEN_ORDER;
					
					//order.setSettings(settings);
					
					viewOrder();
					
					изменитьЌаименование нопки("ѕќƒЅќ–");
					
					/*создатьћенюќбработки“екущего«аказа();
					изменитьЌаименование нопки("¬ заказ");*/
					((CheckBox) findViewById(R.id.checkRest)).setVisibility(View.GONE);
					((Button) findViewById(R.id.calc_standart)).setVisibility(View.VISIBLE);
					
				} else if (thisScreen == SCREEN_ORDER) {
					
					thisScreen = SCREEN_ITEMS;
					viewItems();
					//изменитьЌаименование нопки("¬ заказ");
					
				} else if (thisScreen == SCREEN_SETPRICE) {
					
					if (user.role==User.ROLE_PRICE) {
						
						thisScreen = SCREEN_MAIN;
						EL_TOP_MENU.setVisibility(View.INVISIBLE);
						EL_PODBOR.setVisibility(View.INVISIBLE);
						viewMain();
						
					} else {
					
						thisScreen = SCREEN_TRT;
						
						((LinearLayout) findViewById(R.id.left_menu)).setVisibility(View.VISIBLE);
						
						//LinearLayout someView = (LinearLayout) findViewById(R.id.top_menu);
						LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
						EL_TOP_MENU.getLayoutParams();
						//params.height = 180;
						params.weight = 3;
						EL_TOP_MENU.setLayoutParams(params);
						
						viewTRT();
						//изменитьЌаименование нопки("Ќовый заказ");
						((CheckBox) findViewById(R.id.checkRest)).setVisibility(View.GONE);
						((Button) findViewById(R.id.calc_standart)).setVisibility(View.VISIBLE);
						//viewTRTBackListSetprice();
					}
				}
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	public void onBackPressed() {
		
		switch (mScreen) {
		case SCREEN_ITEMS:
			changeScreen(SCREEN_ORDER);
			break;
		case SCREEN_SETPRICE:
			changeScreen(SCREEN_TRT);
			break;
		case SCREEN_ORDER:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("—охранить заказ?");
			//alert.setMessage("”кажите тип");
			alert.setPositiveButton("ƒа", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					order.setSettings(settings);
					order.save(trt.kod);
					changeScreen(SCREEN_TRT);
				}
			});
			alert.setNegativeButton("Ќет", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					changeScreen(SCREEN_TRT);
				}
			});
			alert.show();
			break;
		case SCREEN_TRT:
			animateButtonsGone(callEmpty);
			currentButton.setSelected(false);
			changeScreen(SCREEN_MAIN);
			break;
		default:
			super.onBackPressed();			
			break;
		}
		
		/*if (thisScreen==SCREEN_ITEMS | thisScreen == SCREEN_SETPRICE) {
			
			final callBack back = new callBack() {
				public void onBack() {
					onBackShowView();
				}
			};
			callBack back1 = new callBack() {
				public void onBack() {
					//EL_HEAD_BUTTONS.setVisibility(View.GONE);
					animateTopTranslateGone1(back);
				}
			};
			//animateProductHeadGone(back1);
		} else {
			super.onBackPressed();
		}*/
	}
	
	
	public void onBackShowView() {
		
		if (thisScreen==SCREEN_ITEMS | thisScreen == SCREEN_SETPRICE) {
			
			EL_TOP_MENU.removeAllViews();
			
			//EL_TOP_BACK.setVisibility(View.GONE);
			EL_MENU.setVisibility(View.VISIBLE);
			
			//LinearLayout someView = (LinearLayout) findViewById(R.id.top_menu);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
			EL_TOP_MENU.getLayoutParams();
			//params.height = 180;
			params.weight = 5;
			EL_TOP_MENU.setLayoutParams(params);
		}
		
		if (thisScreen == SCREEN_ITEMS) {
	    	
			thisScreen = SCREEN_ORDER;
			
			viewOrderBack();
			//viewOrder();
			//создатьћенюќбработки“екущего«аказа();	
			//изменитьЌаименование нопки("в заказ");
			//((CheckBox) findViewById(R.id.checkRest)).setVisibility(View.GONE);
			//((Button) findViewById(R.id.calc_standart)).setVisibility(View.VISIBLE);
			
	    } else if (thisScreen == SCREEN_SETPRICE) {
	    	
			thisScreen = SCREEN_TRT;
			
			viewTRT();
			//viewTRTBackListSetprice();
			//изменитьЌаименование нопки("Ќовый заказ");
			//((CheckBox) findViewById(R.id.checkRest)).setVisibility(View.GONE);
			//((Button) findViewById(R.id.calc_standart)).setVisibility(View.VISIBLE);
		/*} else if (closeProgramm) {
			closeProgramm = false;
			super.onBackPressed();
		} else {
			programClose();*/
		} else {
			super.onBackPressed();
		}
	}
	
	private void updateSumResult(Double sum) {
		
		sum = new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP).doubleValue();
		
		DecimalFormat precision = new DecimalFormat("0.00");
		((TextView) findViewById(R.id.total)).setText(precision.format(sum).replace(',', '.'));
		
	}
	
	public void zakaz_delete(View v) {
		
		deletedOrder = v;
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("”далить заказ?");

		alert.setPositiveButton("ƒа", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				FrameLayout f = (FrameLayout) deletedOrder.getParent().getParent();
				
				String zakazId = ((TextView) f.findViewById(R.id.name)).getText().toString();

				sqdb.deleteDocTable(zakazId);
				sqdb.deleteDoc(zakazId);
				
				callBack back = new callBack() {
					public void onBack() {
						mMenu = ListData.VIEW_MAIN_ORDERS;
						viewMainButtons();
					}
				};
				animateButtonsGone(back);
			}
		});

		alert.setNegativeButton("ќтмена", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});

		alert.show();
	}
	
	private void refreshOrdersList() {
		
		//создатьѕравый—писок();
		//order.setOrderList(trt.kod);
		
		//LinearLayout items = (LinearLayout) findViewById(R.id.items);
		
		int i = 0;
		//items.removeAllViews();
		for (String[] data : order.list) {
			
			View item = ltInflater.inflate(R.layout.salout, EL_LIST_BUTTONS, false);
			
			((TextView) item.findViewById(R.id.kod)).setText(Integer.toString(i++));
			((TextView) item.findViewById(R.id.name)).setText(data[Order.NUMBER]);
			((TextView) item.findViewById(R.id.sum)).setText(data[Order.SUM]);
			
			//((TextView) item.findViewById(R.id.kod)).setText(data[Order.TRT]);
			
			((FrameLayout) item.findViewById(R.id.tree_item_main)).setOnTouchListener(viewTRTOrderTouch);
			
			EL_LIST_BUTTONS.addView(item);
		}
		
	}
	
	OnClickListener нажатиеЌольќстатки = new OnClickListener() {
		
		public void onClick(View v) {
			if (itemsList) {
				createItems(thisItemParent);
			}
		}
	};
	
	private void изменитьЌаименование нопки(String text) {
		
		((TextView) findViewById(R.id.total_text)).setText(text);
		
	}
	
	public void call_calc(View v) {

		String CALCULATOR_PACKAGE ="com.android.calculator2";
	    String CALCULATOR_CLASS ="com.android.calculator2.Calculator";
		
		Intent intent = new Intent();
		 
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(CALCULATOR_PACKAGE, CALCULATOR_CLASS));

        this.startActivity(intent);
		
	}
	
	/*@Override
	protected void onRestart() {
		//Log.d(TAG, "onRestart");
		//createTrtList();
		super.onRestart();
	}*/
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		
		menu.add("ќпции");
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		  
		Intent intent = new Intent(MainActivity.this, DataOptions.class);
		startActivity(intent);
		      
		return super.onOptionsItemSelected(item);
	}
	
	public void programClose() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("¬ыйти из программы?");
		alert.setPositiveButton("ƒа", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				closeProgramm = true;
			}
		});
		alert.setNegativeButton("ќтмена", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		alert.show();
	}
	
	private void animate(int newScreen) {
		
		switch (newScreen) {
		case SCREEN_MAIN:
			switch (mScreen) {
			case SCREEN_MAIN:
				animateMenuTranslateShow();
				animateTopTranslateShow();
				break;
			case SCREEN_TRT:
				callBack back = new callBack() {
					public void onBack() {
						setMenuMain();
						animateMenuRotateShow();
					}
				};
				animateMenuRotateGone(back);
				break;
			}
			break;
		case SCREEN_TRT:
			switch (mScreen) {
			case SCREEN_MAIN:
			case SCREEN_ORDER:
				callBack back = new callBack() {
					public void onBack() {
						setMenuTRT();
						animateMenuRotateShow();
					}
				};
				animateMenuRotateGone(back);
				animateButtonsGone(callEmpty);
				break;
			case SCREEN_SETPRICE:
				callBack back1 = new callBack() {
					public void onBack() {
						setMenuTRT();
						viewSettings();
						EL_MENU.setVisibility(View.VISIBLE);
						animateTopTranslateShow1(callEmpty);
						animateMenuTranslateShow1(mCallBack);
					}
				};
				animateTopTranslateGone2(back1);
				animateProductHeadGone();
				break;
			}
			break;
		case SCREEN_ITEMS:
			switch (mScreen) {
			case SCREEN_TRT:
			case SCREEN_ORDER:
				animateTopTranslateGone1(mCallBack);
				animateMenuTranslateGone2(callEmpty);
				break;
			case SCREEN_ITEMS:
				animateProductHeadShow();
				animateTopTranslateShow1(callEmpty);
				break;
			}
			break;
		case SCREEN_SETPRICE:
			switch (mScreen) {
			case SCREEN_TRT:
				animateTopTranslateGone1(mCallBack);
				animateMenuTranslateGone2(callEmpty);
				break;
			case SCREEN_SETPRICE:
				animateProductHeadShow();
				animateTopTranslateShow1(callEmpty);
				break;
			}
			break;
		case SCREEN_ORDER:
			switch (mScreen) {
			case SCREEN_ITEMS:
				animateTopTranslateGone2(mCallBack);
				animateProductHeadGone();
				break;
			case SCREEN_ORDER:
				callBack back = new callBack() {
					public void onBack() {
						currentButton.setSelected(true);
						mMenu = Order.MAIN_ORDER;
						showOrderMenu();
					}
				};
				animateTopTranslateShow1(callEmpty);
				animateMenuTranslateShow1(back);
				break;
			case SCREEN_MAIN:
			case SCREEN_TRT:
				callBack back1 = new callBack() {
					public void onBack() {
						setMenuOrder();
						animateMenuRotateShow();
					}
				};
				animateMenuRotateGone(back1);
				animateButtonsGone(callEmpty);
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private void animateMenuTranslateShow() {
		
		int width = EL_MENU.getMeasuredWidth();
		Log.d(TAG, "EL_MENU = " + width);
		
		Animation anim = new TranslateAnimation(-width, 0, 0, 0);
		
		anim.setDuration(300);
		anim.setStartOffset(300);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				mCallBack.onBack();
			}
		});
		EL_MENU.startAnimation(anim);
	}
	
	private void animateMenuTranslateShow1(final callBack back) {
		
		int width = EL_MENU.getMeasuredWidth();
		
		Animation anim = new TranslateAnimation(-width, 0, 0, 0);
		
		anim.setDuration(300);
		//anim.setStartOffset(300);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				back.onBack();
			}
		});
		EL_MENU.startAnimation(anim);
	}
	
	private void animateMenuTranslateGone1(final callBack back) {
		
		int width = EL_MENU.getMeasuredWidth();
		
		Animation anim = new TranslateAnimation(0, -width, 0, 0);
		
		anim.setDuration(200);
		//anim.setStartOffset(300);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				//EL_MENU_ITEMS.removeAllViews();
				EL_MENU.setVisibility(View.GONE);
				back.onBack();
			}
		});
		EL_MENU.startAnimation(anim);
	}
	
	private void animateMenuTranslateGone2(final callBack back) {
		
		int width = EL_MENU.getMeasuredWidth();
		
		Animation anim = new TranslateAnimation(0, -width, 0, 0);
		
		anim.setDuration(200);
		//anim.setStartOffset(300);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_MENU_ITEMS.removeAllViews();
				EL_MENU.setVisibility(View.GONE);
				back.onBack();
			}
		});
		EL_MENU.startAnimation(anim);
	}
	
	private void animateMenuRotateShow() {
		
		int width = EL_MENU.getMeasuredWidth();
		
		//Flip3dAnimation anim = new Flip3dAnimation(-90, 0, width/2.0f, height/2.0f);
		Flip3dAnimation anim = new Flip3dAnimation(-90, 0, width/2.0f, 0);
		
		//Flip3dAnimation anim = new Flip3dAnimation(0, 100, 0, 0);
		anim.setDuration(200);
		//anim3.setStartOffset(300);
		//anim.setInterpolator(new BounceInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				mCallBack.onBack();
			}
		});
		EL_MENU.startAnimation(anim);
	}
	
	private void animateMenuRotateGone(final callBack back) {
		
		int width = EL_MENU.getMeasuredWidth();
		
		Flip3dAnimation anim = new Flip3dAnimation(0, 90, width/2.0f, 0);
		
		anim.setDuration(200);
		//anim3.setStartOffset(300);
		//anim.setInterpolator(new BounceInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_MENU_ITEMS.removeAllViews();
				back.onBack();
			}
		});
		EL_MENU.startAnimation(anim);
	}	

	private void animateTopTranslateShow() {
		
		int height = EL_TOP_MENU.getMeasuredHeight();
		Log.d(TAG, "EL_TOP_MENU = " + height);
		
		Animation anim = new TranslateAnimation(0, 0, -height, 0);
		
		anim.setDuration(300);
		anim.setStartOffset(300);
		//anim.setInterpolator(new OvershootInterpolator());
		
		EL_TOP_MENU.startAnimation(anim);
	}
	
	private void animateTopTranslateShow1(final callBack back) {
		
		int height = EL_TOP_MENU.getMeasuredHeight();
		Log.d(TAG, "EL_TOP_MENU = " + height);
		
		Animation anim = new TranslateAnimation(0, 0, -height, 0);
		
		anim.setDuration(300);
		//anim.setFillAfter(true);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				back.onBack();
			}
		});
		
		EL_TOP_MENU.startAnimation(anim);
	}
	
	private void animateTopTranslateGone1(final callBack back) {
		
		int height = EL_TOP_MENU.getMeasuredHeight();
		
		Animation anim = new TranslateAnimation(0, 0, 0, -height);
		
		anim.setDuration(200);
		//anim.setFillAfter(true);
		//anim.setStartOffset(300);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_TOP_MENU.removeAllViews();
				back.onBack();
			}
		});
		EL_TOP_MENU.startAnimation(anim);
	}
	
	private void animateTopTranslateGone2(final callBack back) {
		
		int height = EL_TOP_MENU.getMeasuredHeight();
		
		Animation anim = new TranslateAnimation(0, 0, 0, -height);
		
		anim.setDuration(200);
		//anim.setFillAfter(true);
		anim.setStartOffset(200);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_TOP_MENU.removeAllViews();
				back.onBack();
			}
		});
		EL_TOP_MENU.startAnimation(anim);
	}
	
	private void animateProductHeadShow() {
		
		int height = EL_HEAD_BUTTONS.getMeasuredHeight();
		Log.d(TAG, "EL_HEAD_BUTTONS = " + height);
		
		Animation anim = new TranslateAnimation(0, 0, -height, 0);
		anim.setDuration(200);
		anim.setStartOffset(300);
		anim.setFillEnabled(true);
		anim.setFillBefore(true);
		//anim.setInterpolator(new OvershootInterpolator());
		EL_HEAD_BUTTONS.startAnimation(anim);
	}
	
	private void animateProductHeadGone() {
		
		int height = EL_HEAD_BUTTONS.getMeasuredHeight();
		Log.d(TAG, "EL_HEAD_BUTTONS = " + height);
		
		Animation anim = new TranslateAnimation(0, 0, 0, -height);
		
		anim.setDuration(200);
		//anim.setFillAfter(true);
		//anim.setInterpolator(new OvershootInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_HEAD_BUTTONS1.setVisibility(View.GONE);
				EL_HEAD_SETPRICE.setVisibility(View.GONE);
				//back.onBack();
			}
		});
		EL_HEAD_BUTTONS.startAnimation(anim);
	}
	
	private void animateButtonsShow() {
		
		Animation anim = new TranslateAnimation(widthRMenu, 0, 0, 0);
		
		anim.setDuration(200);
		//anim.setInterpolator(new OvershootInterpolator());
		EL_LIST_BUTTONS.startAnimation(anim);
	}
	
	private void animateButtonsGone(final callBack back) {
	
		Animation anim = new TranslateAnimation(0, -widthRMenu, 0, 0);
		 
		anim.setDuration(200);
		//anim.setFillAfter(true);
		//anim.setInterpolator(new LinearInterpolator());
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_LIST_BUTTONS.removeAllViews();
				back.onBack();
			}
		});
		
		EL_LIST_BUTTONS.startAnimation(anim);
	}	
	
	callBack callEmpty = new callBack() {
		public void onBack() {}
	};
	
	public class TablePriceTRT {

		//public final int KOD = 0, NAME = 1, PRICE1 = 2, PRICE2 = 3, COUNT = 4;
		//public static final int KOD = 0, NAME = 1, COMMENT = 2, PRICE1 = 3, PRICE2 = 4, COUNT = 5;
		public ArrayList<String[]> data;
		public String[] line;
		
		public void setTable(String itemKod) {
			
			Calendar ccc = Calendar.getInstance();
			String date = formatDate.format(ccc.getTime());
			data = Product.getItemsListPriceTRT(sqdb, date, trt.kod, itemKod);
		}
		
		public void setCurrentLine(int index) {			
			line = data.get(index);
		}
	}
	
	protected void onResume() {
		
		super.onResume();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		
		//sqdb.closeDB();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	OnTouchListener viewItemsGroupTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:

				String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String name = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				for (Product data : product.hierarchyList) {
					
					if (data.hierarchy!=0||data==product.mainParent) continue;
					
					product.hierarchyList.clear();
					product.hierarchyList.add(product.mainParent);
					
					//product.hierarchyList.remove(data);
					
					break;
				}
				/*if (product.hierarchy==0) {
					
					int hIndex = Integer.parseInt(product.parent);
					Product pProduct = product.hierarchyList.get(hIndex);
					Product mProduct	= new Product();
					mProduct.kod		= pProduct.kod;
					mProduct.name		= pProduct.name;
					mProduct.hierarchy	= 0;
					
					product.hierarchyList.clear();
					product.hierarchyList.add(mProduct);
				}*/
				
				product.hierarchy++;
				
				Product mProduct	= new Product();
				mProduct.kod		= kod;
				mProduct.name		= name;
				mProduct.hierarchy	= product.hierarchy;
				//mProduct.mainParent = data.mainParent
				
				product.hierarchyList.add(mProduct);
				
				//Log.d(TAG, "hierarchy group = " + product.hierarchy);
				//Log.d(TAG, "hierarchy user = " + user.hierarchy);
				
				if (product.hierarchy==user.hierarchy) {
					createItems(mProduct.kod);
				} else {
					viewItemsGroupButtons(mProduct.kod);
				}
				
				viewItemsTopButtons();
				
				// delete top buttons
				/*int toDelete = mainGroupIndex;
				toDelete++;
				
				for (int i = toDelete; i < 5; i++) {
					removeTopCategory(i);
				}
				for (int i = 0; i < mainGroupIndex; i++) {
					removeTopCategory(i);
				}
				
				addTopCategory(код, им€, "1");
				
				viewItemsPKatButtons(код);*/
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener viewOrderTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			
			boolean check = false;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				
				switch (Integer.parseInt(kod)) {
				case Order.MAIN_SELL:
					viewTRTSell();
					break;
				case Order.MAIN_ORDER:
					viewOrderTableButtons();
					break;
				case Order.MAIN_COMMENT:
					viewOrderComment();
					v.setBackgroundColor(0);
					break;
				case Order.MAIN_CLOSE:
					viewOrderSave();
					break;
				}
				check = true;
				
			default:
				if (check) {
					if (currentButton!=null) {
						currentButton.setBackgroundColor(0);
					}
					v.setBackgroundColor(R.drawable.button_check);
					currentButton = v;
				} else {
					v.setBackgroundColor(0);
				}
				break;
			}
			return true;
		}
	};
	
	OnTouchListener viewItemsTopTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				int index = product.productView.indexOf(v);
				
				Log.d(TAG, "view = " + v);
				Log.d(TAG, "index = " + index);
				
				Product mProduct = product.hierarchyList.get(index);
				
				//String index = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				
				//int hIndex = Integer.parseInt(index);
				
				//Product mProduct = product.hierarchyList.get(hIndex);
				
				product.hierarchy	= mProduct.hierarchy;
				
				//Log.d(TAG, "hierarchy top = " + product.hierarchy);
				
				if (mProduct.hierarchy==0) {
					product.mainParent = mProduct;
					product.setHierarchy(sqdb);
				} else {
					product.changeHierarchy(index);
				}
				
				viewItemsTopButtons();
				
				if (product.hierarchy==user.hierarchy) {
					createItems(mProduct.kod);
				} else {
					viewItemsGroupButtons(mProduct.kod);
				}
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener dateTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				//Intent intent = new Intent(MainActivity.this, ItemsActivity.class);
				//intent.putExtra("groupKod", groupKod);
				//intent.putExtra("itemKod", itemKod);
				//startActivityForResult(intent, 2);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	/*OnTouchListener ViewMainTouch = new OnTouchListener() {
	
		public boolean onTouch(View v, MotionEvent event) {
			
			boolean check = false;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				
				test(Integer.parseInt(kod));
				
				check = true;
				
			default:
				if (check) {
					if (currentButton!=null) {
						currentButton.setBackgroundColor(0);
					}
					v.setBackgroundColor(R.drawable.button_check);
					currentButton = v;
				} else {
					v.setBackgroundColor(0);
				}
				break;
			}
			return true;
		}
	};*/
	
	/*OnTouchListener touchReports = new OnTouchListener() {
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(R.drawable.style_click);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			
			String name = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
			
			Intent intent = new Intent(MainActivity.this, SellData.class);
			
			intent.putExtra("name",	name);
			
			startActivityForResult(intent, 2);
			
		default:
			v.setBackgroundColor(0);
			break;
		}
		return true;
	}
};

OnClickListener viewMainDaysClick = new OnClickListener() {
	@Override
	public void onClick(View v) {
		
		String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
		
		dayOfWeek = Integer.parseInt(mItemKod);
		
		//trtButton.setBackgroundColor(R.drawable.button_check);
		trtButton.setSelected(true);
		
		String деньЌеделиѕрописью = mListData.получитьƒеньЌеделиѕрописью(dayOfWeek);
		if (!деньЌеделиѕрописью.equals("")) {
			деньЌеделиѕрописью = "("+деньЌеделиѕрописью+")";
		}
		//((TextView) currentButton.findViewById(R.id.tree_item_name1)).setText("ƒень недели "+деньЌеделиѕрописью);
		
		//currentButton.setBackgroundColor(0);
		currentButton.setSelected(false);
		currentButton = trtButton;
		
		test();
		
		//viewMainTRTButtons();
	}
};

OnTouchListener нажатиеƒниЌедели = new OnTouchListener() {
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(R.drawable.style_click);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			
			String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			dayOfWeek = Integer.parseInt(mItemKod);
			
			//showAllTRT = dayOfWeek==8? true: false;
			
			//Log.d(TAG, "dayOfWeek = " + mItemKod);
			
			trtButton.setBackgroundColor(R.drawable.button_check);
			
			String деньЌеделиѕрописью = mListData.получитьƒеньЌеделиѕрописью(dayOfWeek);
			if (!деньЌеделиѕрописью.equals("")) {
				деньЌеделиѕрописью = "("+деньЌеделиѕрописью+")";
			}
			((TextView) currentButton.findViewById(R.id.tree_item_name1)).setText("ƒень недели "+деньЌеделиѕрописью);
			
			currentButton.setBackgroundColor(0);
			currentButton = trtButton;
			
			viewMainTRTButtons();
			
		default:
			v.setBackgroundColor(0);
			break;
		}
		return true;
	}
};

OnClickListener viewMainTRTClick = new OnClickListener() {
	@Override
	public void onClick(View v) {
		
		String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
		Log.d(TAG, "TRT kod = " + kod);
		
		trt = new TRT(sqdb, kod);
		
		//AnimationSet a = new AnimationSet(false);
		
		Animation anim = new TranslateAnimation(0, -widthRMenu, 0, 0);
		 
		anim.setDuration(200);
		anim.setInterpolator(new LinearInterpolator());
		//anim.setFillAfter(true);
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				EL_LIST_BUTTONS.removeAllViews();
			}
		});
		EL_LIST_BUTTONS.startAnimation(anim);
		
		int width = EL_MENU.getMeasuredWidth();
		//int height = EL_MENU.getMeasuredHeight();
		
		Flip3dAnimation anim3 = new Flip3dAnimation(0, -90, width, 0);
		anim3.setDuration(400);
		
		anim3.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				viewTRT();
			}
		});
		EL_MENU.startAnimation(anim3);
	}
};

OnTouchListener viewMainTRTTouch = new OnTouchListener() {
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(R.drawable.style_click);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			Log.d(TAG, "TRT kod = " + kod);
			
			//currentTRT = mItemKod;
			//orderNumber = 0;
			
			trt = new TRT(sqdb, kod);
			
			AnimationSet a = new AnimationSet(false);
			
			Animation anim = new TranslateAnimation(0, -widthRMenu, 0, 0);
			 
			anim.setDuration(200);
			anim.setInterpolator(new LinearInterpolator());
			//anim.setFillAfter(true);
			anim.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					EL_LIST_BUTTONS.removeAllViews();
				}
			});
			EL_LIST_BUTTONS.startAnimation(anim);
			
			int width = EL_MENU.getMeasuredWidth();
			int height = EL_MENU.getMeasuredHeight();
			
			Flip3dAnimation anim3 = new Flip3dAnimation(0, -90, width, 0);
			anim3.setDuration(400);
			//anim3.setStartOffset(300);
			//anim3.setInterpolator(new BounceInterpolator());
			
			anim3.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {}
				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {
					viewTRT();
				}
			});
			EL_MENU.startAnimation(anim3);
			
			//a.addAnimation(anim);
			//a.addAnimation(anim3);
			
			//viewTRT();
			
		default:
			v.setBackgroundColor(0);
			break;
		}
		return true;
		}
	};
	*/
	
	/*OnTouchListener viewTRTTouch = new OnTouchListener() {
	
	public boolean onTouch(View v, MotionEvent event) {
		
		boolean check = false;
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(R.drawable.style_click);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			
			String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
			
			switch (Integer.parseInt(kod)) {
			case TRT.MAIN_SELL:
				viewTRTSell();
				break;
			case TRT.MAIN_ORDER:
				viewTRTOrder();
				break;
			case TRT.MAIN_PRICE:
				viewSetPrice();
				break;
			case TRT.MAIN_ADDR:
				viewTRTAddrButtons();
				break;
			case TRT.MAIN_BACK:
				viewTRTBack();
				break;
			}
			check = true;
		default:
			if (check) {
				if (currentButton!=null) {
					currentButton.setBackgroundColor(0);
				}
				v.setBackgroundColor(R.drawable.button_check);
				currentButton = v;
			} else {
				v.setBackgroundColor(0);
			}
			break;
		}
		return true;
	}
};*/
	
	OnTouchListener viewTRTSellTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			
			boolean check = false;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				check = true;
				
				String kod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				
				/*if (mItemKod.equals("1")) {
					создатьћенюƒатаќтгрузки();
				} else if (mItemKod.equals("2")) {
					создатьћеню онтрагент();
				} else if (mItemKod.equals("3")) {
					—оздатьћеню“ипы÷ен();
				} else if (mItemKod.equals("4")) {
					—оздатьћеню¬идќперации();
				} else if (mItemKod.equals("5")) {
					создатьћеню—клад();
				}*/
				
				switch (Integer.parseInt(kod)) {
				case TRT.SELL_DATE:
					viewTRTSellDate();
					break;
				case TRT.SELL_KONTR:
					viewTRTSellKontr();
					break;
				case TRT.SELL_PRICE:
					viewTRTSellPriceType();
					break;
				case TRT.SELL_OPER:
					viewTRTSellOper();
					break;
				case TRT.SELL_STOK:
					viewTRTSellStock();
					break;
				}
			default:
				if (check) {
					if (currButtonCondition!=null) {
						currButtonCondition.setBackgroundColor(0);
					}
					v.setBackgroundColor(R.drawable.button_check);
					currButtonCondition = v;
				} else {
					v.setBackgroundColor(0);
				}
				break;
			}
			return true;
		}
	};
	
	OnTouchListener нажатиеƒатаќтгрузки = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String mItemName = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				/*for (Map<String, String> m : списокЌастроек) {
					if (m.get("kod").equals("1")) {
						m.put("value", mItemKod);
						m.put("valueName", mItemName);						
					}
				}*/
				
				Log.d(TAG, "kod = " + mItemKod);
				Log.d(TAG, "name = " + mItemName);
				setSetting(ListData.SET_DATE, mItemName, mItemKod);
				
				LinearLayout items = (LinearLayout) findViewById(R.id.items2);
				items.removeAllViews();
				
				currButtonCondition.setBackgroundColor(0);
				
				//setSettings("ƒата", mItemName);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener нажатие онтрагент = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String mItemName = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				/*for (Map<String, String> m : списокЌастроек) {
					if (m.get("kod").equals("2")) {
						m.put("value", mItemKod);
						m.put("valueName", mItemName);						
					}
				}*/
				
				Log.d(TAG, "kod = " + mItemKod);
				Log.d(TAG, "name = " + mItemName);
				setSetting(ListData.SET_KONTR, mItemName, mItemKod);
				//сохранитьЌастройку“–“(" онтрагент", mItemKod);
				
				LinearLayout items = (LinearLayout) findViewById(R.id.items2);
				items.removeAllViews();
				
				currButtonCondition.setBackgroundColor(0);
				
				//setSettings(" онтрагент", mItemName);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener нажатие“ипы÷ен = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String mItemName = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				/*for (Map<String, String> m : списокЌастроек) {
					if (m.get("kod").equals("3")) {
						m.put("value", mItemKod);
						m.put("valueName", mItemName);						
					}
				}*/
				
				Log.d(TAG, "kod = " + mItemKod);
				Log.d(TAG, "name = " + mItemName);
				setSetting(ListData.SET_PRICE, mItemName, mItemKod);
				//сохранитьЌастройку“–“("“ип÷ены", mItemKod);
				
				LinearLayout items = (LinearLayout) findViewById(R.id.items2);
				items.removeAllViews();
				
				currButtonCondition.setBackgroundColor(0);
				
				//setSettings("“ип цен", mItemName);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener нажатие¬идќперации = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String mItemName = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				/*for (Map<String, String> m : списокЌастроек) {
					if (m.get("kod").equals("4")) {
						m.put("value", mItemKod);
						m.put("valueName", mItemName);						
					}
				}*/
				
				Log.d(TAG, "kod = " + mItemKod);
				Log.d(TAG, "name = " + mItemName);
				setSetting(ListData.SET_OPER, mItemName, mItemKod);
				//сохранитьЌастройку“–“("¬идќперации", mItemKod);
				
				LinearLayout items = (LinearLayout) findViewById(R.id.items2);
				items.removeAllViews();
				
				currButtonCondition.setBackgroundColor(0);
				
				//setSettings("¬ид операции", mItemName);
				
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener нажатие—клады = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				String mItemKod = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				String mItemName = ((TextView) v.findViewById(R.id.tree_item_name1)).getText().toString();
				
				/*for (Map<String, String> m : списокЌастроек) {
					if (m.get("kod").equals("5")) {
						m.put("value", mItemKod);
						m.put("valueName", mItemName);						
					}
				}*/
				
				Log.d(TAG, "kod = " + mItemKod);
				Log.d(TAG, "name = " + mItemName);
				setSetting(ListData.SET_STOCK, mItemName, mItemKod);
				//сохранитьЌастройку“–“("—клад", mItemKod);
				
				LinearLayout items = (LinearLayout) findViewById(R.id.items2);
				items.removeAllViews();
				
				currButtonCondition.setBackgroundColor(0);
				
				//setSettings("—клад", mItemName);
				
				/*if (!orderList.isEmpty()) {
					if (!mItemKod.equals("¬се—клады")) {
						String toastText = "¬нимание, остатков на данном складе по текущему заказу может быть недостаточно.";
						Toast.makeText(MainActivity.this, toastText, 3000).show();
					}
				}*/
					
			default:
				v.setBackgroundColor(0);
				break;
			}
			return true;
		}
	};
	
	OnTouchListener viewTRTOrderTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			
			boolean check = false;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				thisScreen = SCREEN_ORDER;
				
				String index = ((TextView) v.findViewById(R.id.name)).getText().toString();
				
				//order.setListLine(Integer.parseInt(index));
				
				//currentButton = butonThisOrder;
				//currentButton.setBackgroundColor(R.drawable.button_check);
				
				order.setOrder(index);
				settings = mListData.getSettings(order.head);
				viewOrder();
				
				EL_PODBOR.setVisibility(View.VISIBLE);
				//изменитьЌаименование нопки("SCREEN_ITEMS");
				
				/*//Log.d(TAG, "наж currentTRT = "+currentTRT);
				
				String mItemKod = ((TextView) v.findViewById(R.id.name)).getText().toString();
				currentTRT = ((TextView) v.findViewById(R.id.kod)).getText().toString();
				
				//Log.d(TAG, "mItemKod = "+mItemKod);
				
				orderNumber = Integer.parseInt(mItemKod);
				
				thisScreen = SCREEN_ORDER;
				
				Map<String, String> map = mListData.получитьЌастройкиѕо“екущему«аказу(orderNumber);
				«аполнитьќсновныеЌастройки(map);
				
				создатьћенюќбработки“екущего«аказа();
				отобразить“екущий«аказ();
				
				Log.d(TAG, "наж currentTRT = "+currentTRT);*/
				
			default:
				if (check) {
					if (currentButton!=null) {
						currentButton.setBackgroundColor(0);
					}
					v.setBackgroundColor(R.drawable.button_check);
					currentButton = v;
				} else {
					v.setBackgroundColor(0);
				}
				break;
			}
			return true;
		}
	};
	
	OnTouchListener viewTRTAddrTouch = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			
			//boolean check = false;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.style_click);
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				toChange = v;
				
				String sIndex = ((TextView) v.findViewById(R.id.tree_item_kod)).getText().toString();
				
				int index = Integer.parseInt(sIndex);
				
				//viewTRTAddrSet(Integer.parseInt(index));
				switch (index) {
				case TRT.ADDR_TYPE:
					viewTRTAddrTypeSet(index);
					break;
				default:
					viewTRTAddrSet(index);
				}
				
				//check = true;
				
			default:
				v.setBackgroundColor(0);
				/*if (check) {
					if (currentButton!=null) {
						currentButton.setBackgroundColor(0);
					}
					v.setBackgroundColor(R.drawable.button_check);
					currentButton = v;
				} else {
					v.setBackgroundColor(0);
				}*/
			}
			return true;
		}
	};
	
}

