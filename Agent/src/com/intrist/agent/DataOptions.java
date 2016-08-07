package com.intrist.agent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DataOptions extends Activity {

	private static final String TAG = "myLogs";
	public DataBase sqdb;
	//public SQLiteDatabase sqdb;
	public DBHelper dbh;
	private ProgressDialog prDialog;
	private XmlTask xTask;
	private User user;
	private FrameLayout viewUser, viewReport, viewStart, viewExchange;
	private LinearLayout viewAll, im_agent, im_report, im_start, im_exchange;
	private LinearLayout[] arrView;
	//private Drawable resClick1, resNow1;
	private DisplayMetrics dm;
	private Handler h;
	private Thread t;
	private TextView agentText;
	
	//test83/dostavka_2015010415/android1;
	private String deviceId, serverUrl = "http://176.111.63.76:43440", baseName = "android1";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.data_options);
		setContentView(R.layout.screen_main);
		
		sqdb = new DataBase(this);
		sqdb.OpenDB();
		
		agentText = (TextView) findViewById(R.id.agent_text);
		
		viewAll			= (LinearLayout) findViewById(R.id.main_ll);
		
		viewUser		= (FrameLayout) findViewById(R.id.main_user);
		viewReport		= (FrameLayout) findViewById(R.id.main_report);
		viewStart		= (FrameLayout) findViewById(R.id.main_start);
		viewExchange	= (FrameLayout) findViewById(R.id.main_exchange);
		
		im_agent	= (LinearLayout) findViewById(R.id.im_agent);
		im_report	= (LinearLayout) findViewById(R.id.im_report);
		im_start	= (LinearLayout) findViewById(R.id.im_start);
		im_exchange = (LinearLayout) findViewById(R.id.im_exchange);
		
		viewUser.setOnClickListener(click1);
		viewReport.setOnClickListener(click1);
		viewStart.setOnClickListener(click1);
		viewExchange.setOnClickListener(click1);
		
		setAgent();
		получитьИдентификатор();
		
		((TextView) findViewById(R.id.device_id)).setText("ИД: "+deviceId);
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		h = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (viewUser.getMeasuredWidth()==0) {
					//t.start();
					t.run();
				} else {
					setImages();
					animRotateScreen();
				}
			};
		};
		t = new Thread(new Runnable() {
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
			    } catch (InterruptedException e) {
			      e.printStackTrace();}
				h.sendEmptyMessage(1);
			}
		});
		
		t.start();
	}
	
	private void animRotateScreen() {
		
		int height = dm.heightPixels+dm.heightPixels/4;
		int width = dm.widthPixels+dm.widthPixels;
		
		//Log.d(TAG, "dm.heightPixels = " + dm.heightPixels);
		//Log.d(TAG, "dm.widthPixels = " + dm.widthPixels);
		
		//Log.d(TAG, "height" + height);
		//Log.d(TAG, "width" + width);
		
		LayoutParams params = (LayoutParams) viewAll.getLayoutParams();
		params.height = height;
		params.width = width;
		viewAll.setLayoutParams(params);
		
		RotateAnimation rAnim1 = new RotateAnimation(-17, -17, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rAnim1.setFillAfter(true);
		//rAnim1.setDuration(0);
		rAnim1.setFillEnabled(true);
		rAnim1.setFillBefore(true);
		rAnim1.setRepeatCount(Animation.INFINITE);
		viewAll.setAnimation(rAnim1);
		
		/*viewUser.setAnimation(rAnim1);
		viewReport.setAnimation(rAnim1);
		viewStart.setAnimation(rAnim1);
		viewExchange.setAnimation(rAnim1);*/
		
	}
	
	private void setImages() {
		
		int correction = dm.widthPixels/6;
		
		int width = dm.widthPixels - correction;
		int height = dm.heightPixels;
		
		Log.d(TAG, "width = " + width);
		Log.d(TAG, "height = " + height);
		
		int partWidth = width/4;
		int partHeight = height/4;
		
		Log.d(TAG, "partWidth = " + partWidth);
		Log.d(TAG, "partHeight = " + partHeight);
		
		int widthStart = partWidth/2+correction/3;
		int heightStart = partHeight/2;
		
		Log.d(TAG, "widthStart = " + widthStart);
		Log.d(TAG, "heightStart = " + heightStart);
		
		arrView = new LinearLayout[4];
		arrView[0] = im_agent;
		arrView[1] = im_report;
		arrView[2] = im_start;
		arrView[3] = im_exchange;
		
		for (int i = 0; i < arrView.length; i++) {
			
			LinearLayout ll = arrView[i];
			
			int mWidth = ll.getMeasuredWidth();
			int mHeight = ll.getMeasuredHeight();
			
			int mWidth1 = mWidth/2;
			int mHeight1 = mHeight/2;
			
			Log.d(TAG, "mWidth = " + mWidth + ", mHeight = " + mHeight);
			Log.d(TAG, "mWidth1 = " + mWidth1 + ", mHeight1 = " + mHeight1);
			
			int left = widthStart-mWidth1;
			int top = heightStart-mHeight1;
			
			Log.d(TAG, "left = " + left + ", top = " + top);
			
	        if (i==0) {
	        
	        	int j = mWidth - (left<0?left*-1:left);
	        	left = widthStart-j/2;
	        	LayoutParams pText = (LayoutParams) agentText.getLayoutParams();
	        	pText.width = j;
	        	agentText.setGravity(Gravity.CENTER);
	        	agentText.setLayoutParams(pText);
	        }
	        
	        LayoutParams params = (LayoutParams) ll.getLayoutParams();
			
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) params;
	        p.setMargins(left, top, 0, 0);
	        ll.requestLayout();
	        
	        widthStart = widthStart + partWidth;
	        heightStart = heightStart + partHeight;	
		}
	}

	OnClickListener click1 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.main_user:
				if (user.role == User.ROLE_ADMIN) {
					/*Intent intent = new Intent(DataOptions.this, ActivityTech.class);
					startActivity(intent);*/
					viewAgent();
				}
				break;
			case R.id.main_report:
				
				break;
			case R.id.main_start:
				click_start();
				break;
			case R.id.main_exchange:
				click_download();
				click_upload();
				break;
			}
		}
	};
	
	private void viewAgent() {
		
		//String name = trt.addrList[TRT.ADDR_TYPE][NAME];
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Настройки");
		//alert.setMessage("Укажите тип");
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
        final RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(LinearLayout.HORIZONTAL);
        
        RadioButton rdbtn = new RadioButton(this);
        rdbtn.setId(1);
        rdbtn.setText("server: test");
        rdbtn.setChecked(true);        
        rg.addView(rdbtn);
        
        rdbtn = new RadioButton(this);
        rdbtn.setId(2);
        rdbtn.setText("server: work");
        rdbtn.setChecked(false);
        rg.addView(rdbtn);
        
        Button btn = new Button(this);
        btn.setText("technical tab");
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DataOptions.this, ActivityTech.class);
				startActivity(intent);
			}
		});
        
        ll.addView(rg);
        ll.addView(btn);
        
        alert.setView(ll);
		
		alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				String value = "";
				
				int id = rg.getCheckedRadioButtonId();
				if (id==1) {
					baseName = "android1";
				} else {
					baseName = "dostavka_2015010415";
				}
				//setAddress(index, value);
				//viewTRTAddrButtons();
			}
		});
		alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		
		alert.show();
	}
	
	interface downloadCallback {
		void onResult(String result);
	}
	
	downloadCallback callback = new downloadCallback() {
		public void onResult(String result) {
			if (result.equals("0")) {
				Toast.makeText(DataOptions.this, "Доступ запрещен", Toast.LENGTH_LONG).show();
			} else if (result.equals("1")) {
				Toast.makeText(DataOptions.this, "ADMIN", Toast.LENGTH_LONG).show();
			} else {
				
				AlertDialog.Builder alert = new AlertDialog.Builder(DataOptions.this);
				alert.setTitle("Загрузка завершена");
				alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {}});
				alert.show();
			}
			setAgent();
		}
	}; 
	
	private void setAgent() {
		
		user = new User(sqdb);
		agentText.setText(user.name);
		
		//((TextView) findViewById(R.id.agent_text)).setText("Агент: "+user.name);
		//((TextView) findViewById(R.id.agent_text)).setText(user.name);
		
		//int adminPanel = user.role==User.ROLE_ADMIN? View.VISIBLE: View.INVISIBLE;
		//((LinearLayout) findViewById(R.id.admin_panel)).setVisibility(adminPanel);
		
	}
	
	private void получитьИдентификатор() {
		
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    deviceId = deviceUuid.toString();
		
	}
	
	public void click_start() {
		
		if (user.kod.equals("")) {
			Toast.makeText(this, "Не заполнен Агент!", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (user.role==User.ROLE_NO) {
			Toast.makeText(this, "Не заполнены права агента!", Toast.LENGTH_LONG).show();
			return;
		}
		
		DataControll dataControll	= new DataControll(this, sqdb);
		boolean workAllowed = dataControll.deviceData();
		
		if (!workAllowed) {
			return;
		}
		
		Intent intent = new Intent(this, MainActivity.class);
		
		//Bundle bundle = new Bundle();
		
		//bundle.p
		
		//intent.putExtra("data", sqdb);
		
		startActivity(intent);
	}
	
	public void click_download() {
		
		try {
			
			prDialog = new ProgressDialog(this);
			prDialog.setIcon(android.R.drawable.ic_dialog_info);
			prDialog.setTitle("Загрузка данных...");
			prDialog.setMessage("");
			prDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			prDialog.setCancelable(false);
			prDialog.setMax(0);
			prDialog.show();
			xTask = new XmlTask(sqdb, prDialog, deviceId, baseName, user.role, callback);
			xTask.execute();
			Log.d(TAG, "All data ready.");
			//prDialog.dismiss();
			setAgent();
			
		} catch (Exception e) {
			Toast.makeText(this, "cannot disconnect", Toast.LENGTH_LONG).show();
			Log.d(TAG, "disconnect failed!!!");
		}
		
		/*if (!baseAcces) {
			Toast.makeText(this, "Доступ запрещен", Toast.LENGTH_LONG).show();
		}*/
		
	}
	
	public void click_upload() {
		
		//Map<String, String> agent = получитьТекущегоАгента();
		
		Order order = new Order(sqdb);
		JSONArray jsonArr = order.getOrdersListToday(user.merchId);
		final JSONObject jsonOrder = new JSONObject();
		try {
			jsonOrder.put("item", jsonArr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		PriceTRT priceTRT = new PriceTRT(sqdb);
		jsonArr = priceTRT.getPriceTRTListToday(user.merchId);
		final JSONObject jsonPriceTRT = new JSONObject();
		try {
			jsonPriceTRT.put("item", jsonArr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		jsonArr = TRT.getTRTAddress(sqdb);
		final JSONObject jsonTRTAddress = new JSONObject();
		try {
			jsonTRTAddress.put("item", jsonArr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		Thread t = new Thread() {

            public void run() {
                Looper.prepare();
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;

                try {
                    HttpPost post = new HttpPost(serverUrl+"/"+baseName+"/hs/dataPost");
                    StringEntity se = new StringEntity(jsonOrder.toString(), HTTP.UTF_8);  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    if(response!=null){
                        //InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error order upload");
                }
                
                try {
                    HttpPost post = new HttpPost(serverUrl+"/"+baseName+"/hs/trtItemPrice");
                    StringEntity se = new StringEntity(jsonPriceTRT.toString(), HTTP.UTF_8);  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    if(response!=null) {
                        //InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error price TRT upload");
                }
                
                try {
                    HttpPost post = new HttpPost(serverUrl+"/"+baseName+"/hs/trtAddress");
                    StringEntity se = new StringEntity(jsonTRTAddress.toString(), HTTP.UTF_8);  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    if(response!=null) {
                        //InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error address TRT upload");
                }
                
                uploadComplete();
                Looper.loop(); //Loop in the message queue
            }
        };
        
        t.start();
	}

	public void uploadComplete() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Выгрузка завершена");
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}
	
	public void check_server(View v) {
		
		if (((CheckBox) v).isChecked()) {
			baseName = "android1";
		} else {
			baseName = "dostavka_2015010415";
		}
	}
	
	public void check_server1(View v) {
		
		if (((CheckBox) v).isChecked()) {
			baseName = "april_demo";
		} else {
			baseName = "dostavka_2015010415";
		}
	}
	
	public void click_technical(View v) {
		
		Intent intent = new Intent(DataOptions.this, ActivityTech.class);
		startActivity(intent);
		
	}
	
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Close DataBase");
		sqdb.closeDB();
	}
	
	
}
