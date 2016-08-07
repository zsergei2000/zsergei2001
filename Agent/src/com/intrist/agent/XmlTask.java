package com.intrist.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intrist.agent.DataOptions.downloadCallback;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;



public class XmlTask extends AsyncTask<String, Integer, String> {

	private static final String TAG = "myLogs";
	private static final int counter = 100;
	private DataOptions.downloadCallback downloadCallback;
	
	Context ctx;
	DataBase sqdb;
	ProgressDialog pd;
	
	String title, tableName, deviceId, serverUrl = "http://176.111.63.76:43440", baseName;
	int userRole;
	
	XmlTask(DataBase sqdb, ProgressDialog pd, String deviceId, String baseName, int userRole, downloadCallback downloadCallback) {
		
		this.sqdb = sqdb;
		this.pd = pd;
		this.deviceId = deviceId;
		this.baseName = baseName;
		this.userRole = userRole;
		this.downloadCallback = downloadCallback;
		
		//this.dirDevice = dirDevice;
		//this.dirFTP = dirFTP;
	}
	
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		pd.setTitle(title);
		pd.setMessage(tableName);
		pd.incrementProgressBy(values[0]);
	}
	
	protected String doInBackground(String... params) {
		
		String result = "0";
		
		/*boolean доступРазрешен = получитьРазрешениеДоступа();
		
		if (!доступРазрешен) {
			pd.dismiss();
			return result;
		}*/
		
		title = "Загрузка данных";
		
		try {
			tableName = "Агенты:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/agent/"+deviceId);
			Log.d(TAG, "resultJson = " + result);
		    if (result.equals("0")) {
		    	pd.dismiss();
				return result;
		    }
		    result = writeAgents(result);
		    if (result.equals("1")&userRole!=User.ROLE_ADMIN) {
		    	pd.dismiss();
				return result;
		    }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Торговые точки:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/TRT/"+deviceId);
			writeTRT(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Номенклатура:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/items/"+deviceId);
			writeItems(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Агенты - Торговые точки:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/agentTRT/"+deviceId);
			writeAgentsTRT(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Типы цен:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/priceType/"+deviceId);
			writePriceType(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Типы цен - ТРТ:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/priceTypeTRT/"+deviceId);
			writePriceTypeTRT(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Цены номенклатуры:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/itemsPrice/"+deviceId);
			writeItemsPrice(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Маршруты:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/route/"+deviceId);
			writeRoute(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Контрагент:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/kontragent/"+deviceId);
			writeKontragent(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Контрагент - ТРТ:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/kontragentTRT");
			writeKontragentTRT(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Текущие заказы:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/currentOrders/"+deviceId);
			writeСurrentOrders(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Склады:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/stock/"+deviceId);
			writeStock(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Склады агентов:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/stockAgent/"+deviceId);
			writeStockAgent(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Остатки:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/balance/"+deviceId);
			writeBalance(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Дата сервера:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/dateServer");
			writeDateServer(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Типы цен каналов сбыта:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/channel_price");
			writeChannelPrice(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Продажи:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/salesData/"+deviceId);
			writeSalesData(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tableName = "Отчеты:";
			result = downloadJSON(serverUrl+"/"+baseName+"/hs/reports");
			writeReports(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pd.dismiss();
		
		result = "2";
		
		return result;
	}

	private String downloadJSON(String url) throws ClientProtocolException, IOException {
		
		HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
		
        pd.setMax(0);
		pd.setProgress(0);
		publishProgress(0);
		
		// получаем данные с внешнего ресурса
        try {
            URL url1 = new URL(url);

            Log.d(TAG, "Подключение к "+url);
            
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            Log.d(TAG, "Чтение JSON");
            
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка http.");
        }
        return resultJson;
	}
	
	@Override
	protected void onPostExecute(String result) {
		//super.onPostExecute(result);
		
		downloadCallback.onResult(result);
	}
	
	private boolean получитьРазрешениеДоступа() {
		
		boolean доступРазрешен = false;
	                
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String resultJson = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		//HttpResponse response;
		
		try {
		    URL url1 = new URL(serverUrl+"/"+baseName+"/hs/id/"+deviceId);
		
		    Log.d(TAG, serverUrl+"/"+baseName+"/hs/id/"+deviceId);
		    
		    urlConnection = (HttpURLConnection) url1.openConnection();
		    urlConnection.setRequestMethod("GET");
		    urlConnection.connect();
		    
		    InputStream inputStream = urlConnection.getInputStream();
		    StringBuffer buffer = new StringBuffer();
		
		    reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		
		    Log.d(TAG, "Чтение доступа");
		    
		    String line;
		    while ((line = reader.readLine()) != null) {
		        buffer.append(line);
		    }
		
		    resultJson = buffer.toString();
		
		    Log.d(TAG, "resultJson = " + resultJson);
		    
		    if (resultJson.equals("1")) {
		    	доступРазрешен = true;
		    }
		    
		} catch(Exception e) {
		    e.printStackTrace();
		    Log.d(TAG, "Не получилось");
		}
    
		return доступРазрешен;
	
	}
	
	private String writeAgents(String result) {
		
		String role = "0";
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("agent");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod = description.getString("kod");
                String name = description.getString("name");
                String merch_id = description.getString("merch_id");
                String hierarchy = description.getString("hierarchy");
                role = description.getString("role");
                
				m = new HashMap<String, String>();
				m.put("1", kod);
				m.put("2", name);
				m.put("3", merch_id);
				m.put("4", hierarchy);
				m.put("5", role);
                
                data.add(m);
                
                publishProgress(1);
                
            }
            
            sqdb.clearTable("agents");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("agents", data);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
        
        return role;
	}
	
	private void writeTRT(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("trt");
            
    		pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod = description.getString("kod");
                String name = description.getString("name");
                String olId = description.getString("ol_id");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", name);
                m.put("3", olId);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("TRT");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("TRT", data);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeItems(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
				m = new HashMap<String, String>();
				m.put("1", description.getString("kod"));
				m.put("2", description.getString("name"));
				m.put("3", description.getString("thisGroup"));
				m.put("4", description.getString("parentKod"));
				m.put("5", description.getString("prodcode"));
				m.put("6", description.getString("kodManuf"));
				m.put("7", description.getString("k1"));
				m.put("8", description.getString("k2"));
				m.put("9", description.getString("k3"));
				   
				data.add(m);
				    
				k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("items");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("items", data);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeAgentsTRT(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("agentTRT");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kodAgent = description.getString("kodAgent");
                String kodTRT = description.getString("kodTRT");
                
                m = new HashMap<String, String>();
                m.put("1", kodAgent);
                m.put("2", kodTRT);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("AgentTRT");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("AgentTRT", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writePriceType(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod = description.getString("kod");
                String name = description.getString("name");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", name);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("priceType");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("priceType", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writePriceTypeTRT(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kodTRT	= description.getString("kodTRT");
                String kodManuf	= description.getString("kodManuf");
                String kodPrice	= description.getString("kodPrice");
                
                m = new HashMap<String, String>();
                m.put("1", kodTRT);
                m.put("2", kodManuf);
                m.put("3", kodPrice);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("priceTypeTRT");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("priceTypeTRT", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeItemsPrice(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kodItem	= description.getString("kodItem");
                String kodPrice	= description.getString("kodPrice");
                String price	= description.getString("price");
                
                m = new HashMap<String, String>();
                m.put("1", kodItem);
                m.put("2", kodPrice);
                m.put("3", price);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("itemsPrice");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("itemsPrice", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeRoute(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod		= description.getString("kod");
                String day		= description.getString("day");
                String kodTRT	= description.getString("kodTRT");
                String kodAgent	= description.getString("kodAgent");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", day);
                m.put("3", kodTRT);
                m.put("4", kodAgent);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("route");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("route", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeKontragent(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod		= description.getString("kod");
                String name		= description.getString("name");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", name);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("kontragent");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("kontragent", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeKontragentTRT(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kodTRT		= description.getString("kodTRT");
                String kodKontr		= description.getString("kodKontr");
                
                m = new HashMap<String, String>();
                m.put("1", kodTRT);
                m.put("2", kodKontr);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("kontragentTRT");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("kontragentTRT", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeСurrentOrders(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod		= description.getString("kod");
                String order	= description.getString("zakaz");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", order);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("currentOrders");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("currentOrders", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeStock(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod		= description.getString("kod");
                String name		= description.getString("name");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", name);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("stock");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("stock", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeStockAgent(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kodStock		= description.getString("kodStock");
                String kodAgent		= description.getString("kodAgent");
                
                m = new HashMap<String, String>();
                m.put("1", kodStock);
                m.put("2", kodAgent);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("stockAgent");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("stockAgent", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeBalance(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
                String kod		= description.getString("kod");
                String balance	= description.getString("balance");
                String stock	= description.getString("stock");
                
                m = new HashMap<String, String>();
                m.put("1", kod);
                m.put("2", balance);
                m.put("3", stock);
                data.add(m);
                
                k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("balance");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("balance", data);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeChannelPrice(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
				m = new HashMap<String, String>();
				m.put("1", description.getString("kodItem"));
				m.put("2", description.getString("channel"));
				m.put("3", description.getString("kodPrice"));
				   
				data.add(m);
				    
				k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("channelPrice");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("channelPrice", data);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeDateServer(String result) {
		
		try {
			sqdb.clearTable("dateServer");
			sqdb.writeDateServer(result);
			Log.d(TAG, "writing to data base successful");
		} catch (IOException e) {
			Log.e(TAG, "writing to data base failed!!!");
			e.printStackTrace();
		}
	}
	
	private void writeSalesData(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
				m = new HashMap<String, String>();
				m.put("1", description.getString("object"));
				m.put("2", description.getString("lastMF"));
				m.put("3", description.getString("lastMP"));
				m.put("4", description.getString("diferent"));
				m.put("5", description.getString("today"));
				   
				data.add(m);
				    
				k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("salesData");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("salesData", data);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
	private void writeReports(String result) {
		
		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> m;
		
		int rows = 0;
		int k = 0;
		
        try {
        	JSONObject dataJsonObj = new JSONObject(result);
            JSONArray trt = dataJsonObj.getJSONArray("items");
            
            pd.setMax(trt.length());
    		pd.setProgress(0);
            
            for (int i = 0; i < trt.length(); i++, rows++) {
            	
                JSONObject description = trt.getJSONObject(i);
                
				m = new HashMap<String, String>();
				m.put("1", description.getString("report"));
				m.put("2", description.getString("column"));
				m.put("3", description.getString("move"));
				m.put("4", description.getString("value"));
				   
				data.add(m);
				    
				k++;
                if (k == counter) {
                	publishProgress(k);
                	k = 0;
                }
            }
            
            sqdb.clearTable("reports");
            Log.d(TAG, "rows = " + rows);
            sqdb.addData("reports", data);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Ошибка чтения JSON");
        }
	}
	
}
