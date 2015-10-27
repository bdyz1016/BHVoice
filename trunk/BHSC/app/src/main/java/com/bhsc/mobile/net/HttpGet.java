package com.bhsc.mobile.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.conn.ConnectTimeoutException;

import android.util.Log;

public class HttpGet {
 
	// 增加捕获超时异常
	public String requireClass(String path, Map<String, String> params, String encoding) throws ConnectTimeoutException, SocketTimeoutException ,Exception{

		String bakn = "", RetBak = "";

		HttpURLConnection conn = null;
		InputStream inStream = null;

		try {
			StringBuffer entityBufer = new StringBuffer("");
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					entityBufer.append(entry.getKey()).append('=');
					entityBufer.append(URLEncoder.encode(entry.getValue(), encoding));
					entityBufer.append('&');
				}
				entityBufer.deleteCharAt(entityBufer.length() - 1);
			}

			String httpurl = path + entityBufer.toString();
			Log.i("httpGet", "httpurl_GET" + httpurl);
			URL url = new URL(httpurl);
			conn = (HttpsURLConnection) url.openConnection();

			conn.setConnectTimeout(20000);
			conn.setReadTimeout(20000);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			// conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Accept-Charset", encoding);
			conn.setRequestProperty("contentType", encoding);
			inStream = conn.getInputStream();
			int num = 0;
			byte[] resu = new byte[1024];
			// StringBuilder sb = new StringBuilder(); 异步，多线程线程不安全,略快
			StringBuffer sb = new StringBuffer(); // 同步，线程安全
			while ((num = inStream.read(resu)) != -1) {
				bakn = new String(resu, 0, num);
				sb.append(URLDecoder.decode(bakn, encoding));
			}

			RetBak = sb.toString();
			
		}  finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("CLOUD", "conn类中释放资源出错");
			}
		}

		Log.i("Return", RetBak);
		return RetBak;
	}
}
