package com.intrist.agent;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends Activity {
	
	EditText text;
	
	String itemName, price, parent, �������, itemsType;
	
	//private DataBase sqdb;
	
	private static final String TAG = "myLogs";
	
	private TextView ���������;
	
	//private int check;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.calc);
        
        text = (EditText) findViewById(R.id.calc_text);
        
    	Intent intent = getIntent();
    	
    	itemsType = intent.getStringExtra("itemsType");
    	
    	��������� = (TextView) findViewById(R.id.price);
    	price = intent.getStringExtra("price");
    	//if (itemsType.equals("0")) {
    		
    		itemName = intent.getStringExtra("name");
    		parent = intent.getStringExtra("parent");
    		//check = intent.getIntExtra("check", 0);
    		������� = intent.getStringExtra("�������");
    		text.setText(intent.getStringExtra("����������"));
    		
    		((TextView) findViewById(R.id.name)).setText(itemName);
    		
    		�������������();
    		
    		((TextView) findViewById(R.id.net_ostatka)).setText(�������.equals("0")?"��� �������":"");
    		
    	/*} else {
    		
    		itemName = "";
    		parent = "";
    		������� = "0";
    		text.setText(price);
    		
    		((TextView) findViewById(R.id.name)).setText(itemName);
    		
    		�������������();
    		
    		((TextView) findViewById(R.id.net_ostatka)).setText("");
    		���������.setText("");
    	}*/
    }
	
	private void �������������() {
		
		if (itemsType.equals("1")) return;
		
		String textNow = text.getText().toString();
		
		Double amount = Double.parseDouble(textNow);
		Double mPrice = Double.parseDouble(price);
		Double sum = amount * mPrice;  
		
		sum = new BigDecimal(sum).setScale(2, RoundingMode.HALF_UP).doubleValue();
		
		���������.setText(textNow + " x " + price + " = " + sum);
		
	}
	
	public void calc_click(View v) {
		
		String textButton = ((TextView) v).getText().toString();
		String textNow = text.getText().toString();
		if (textNow.equals("0")) {
			textNow = "";
		}
		if (!textButton.equals("OK")) {
			text.setText(textNow+textButton);
		}
		
		switch (v.getId()) {
		case R.id.calc_c:
			text.setText("0");
			break;
		case R.id.calc_ok:
			save();
			break;
		}
		�������������();
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
        
		String textNum = text.getText().toString();
        int number = Integer.parseInt(textNum);
        //Double numDouble = Double.parseDouble(textNum);
        
        //if (number == 0) return;
        
        //Double price1 = Double.parseDouble(price);
        
        //Double amount = numDouble * price1;
        
        Log.d(TAG, "����������� ��������� = " + number);
        
        Intent intent = new Intent();
		intent.putExtra("result", Integer.toString(number));
		intent.putExtra("itemsType", itemsType);
		setResult(RESULT_OK, intent);
		finish();
        
        /*int zakazId = sqdb.saveCalc(itemName, number, price1, amount, parent, check);
        String text = "������ ���������.";
        if (zakazId < 0) {
        	text = "������ ����������!.";
        };
        Toast.makeText(this, text, 1000).show();*/
		
	}

	protected void onDestroy() {
		//sqdb.closeDB();
		super.onDestroy();
	}
	
}
