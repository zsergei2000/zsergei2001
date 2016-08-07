package com.intrist.agent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorPrice  extends Activity {

EditText text;
	
	String itemName, price, parent, остаток, itemsType;
	
	private static final String TAG = "myLogs";
	
	private TextView текст÷ена;
	
	private boolean start = true;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.calc_price);
        
        text = (EditText) findViewById(R.id.calc_text);
        текст÷ена = (TextView) findViewById(R.id.price);
        
    	Intent intent = getIntent();
    	
    	itemsType = intent.getStringExtra("itemsType");
    	price = intent.getStringExtra("price");
    	Log.d(TAG, "price = " + price);
    	//price = price.replace(',', '.');
    	Log.d(TAG, "price = " + price);
    	
		itemName = "";
		parent = "";
		text.setText(price);
		
		((TextView) findViewById(R.id.name)).setText(itemName);
		
		((TextView) findViewById(R.id.net_ostatka)).setText("");
		текст÷ена.setText("");
    }
	
	public void calc_click(View v) {
		
		String textButton = ((TextView) v).getText().toString();
		String textNow = text.getText().toString();
		
		if (start) {
			start = false;
			textNow = "";
		}
		if (textButton.equals(".")) {
			if (textNow.indexOf(".")!=-1) {
				return;
			}
		}
		if (textNow.equals("0") && textButton.equals(".")) {
			textNow = "0.";
		}
		if (textNow.equals("0.0")|textNow.equals("0.00")|textNow.equals("0")) {
			textNow = "";
		}
		if (!textButton.equals("OK")) {
			text.setText(textNow+textButton);
		}
		
		switch (v.getId()) {
		case R.id.calc_c:
			text.setText("0.0");
			break;
		case R.id.calc_ok:
			save();
			break;
		}
	}
	
	public void delete_click(View v) {
		
		text.setText("0");
		save();
	}
	
	public void calc_back_click(View v) {
		switch (v.getId()) {
		case R.id.calc_back:
			finish();
			break;
		}
	}
	
	private void save() {
        
		String textNow = text.getText().toString();
		
		if (textNow.equals("0")) {
			text.setText("0.0");
		}
		/*if (!textNow.equals(".")) {
			text.setText(textNow+".");
		}*/
		
		String textNum = text.getText().toString();
        Double numDouble = Double.parseDouble(textNum);
        
        DecimalFormat precision = new DecimalFormat("0.00");
        //numDouble = precision.format(numDouble);
        
        Log.d(TAG, "калькул€тор результат = " + textNum);
        
        Intent intent = new Intent();
		intent.putExtra("result", precision.format(numDouble).replace(',', '.'));
		intent.putExtra("itemsType", itemsType);
		setResult(RESULT_OK, intent);
		finish();
		
	}

	protected void onDestroy() {
		super.onDestroy();
	}
	
}
