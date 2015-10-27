package com.bhsc.mobile.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
import java.util.Map;

//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.KeyManager;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
//import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import android.util.Log;

public class httpPost {

	public static final String TAG = "httpPost";

	private HttpURLConnection conn = null;

	// 增加捕获超时异常
	public String requireClass(String path, Map<String, String> params, String encoding) throws ConnectTimeoutException, SocketTimeoutException, Exception {

		String bakn = "", RetBak = "";

		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			StringBuffer buffer = new StringBuffer();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					buffer.append(entry.getKey()).append('=');
					buffer.append(URLEncoder.encode(entry.getValue(), encoding));
					buffer.append('&');
				}
				buffer.deleteCharAt(buffer.length() - 1);
			}

			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(60000);// !!!!
			conn.setReadTimeout(60000);// !!!!
			conn.setRequestMethod("POST");
			conn.setDoInput(true);// 表示从服务器获取数据
			conn.setDoOutput(true);// 表示向服务器写数据

			String bSt = buffer.toString();
			Log.i(TAG, "发送参数" + "   " + path + " :" + buffer.toString());
			byte[] entity = bSt.getBytes();

			// 表示设置请求体的类型是文本类型
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 获得上传信息的字节大小以及长度
			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			conn.connect();

			// 获得输出流,向服务器输出数据
			outStream = conn.getOutputStream();
			outStream.write(entity);
			outStream.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Log.e(TAG, "conn.getResponseCode() = " + conn.getResponseCode() + "  网络异常");
//				return "";
			}

			inStream = conn.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] resu = new byte[1024];
			while ((len = inStream.read(resu)) != -1 ) {
				baos.write(resu, 0, len);
			}
			baos.close();//内存输入流不需要CLOSE
			byte[] result = baos.toByteArray();//转化为byte数组
			RetBak = URLDecoder.decode(new String(result).replaceAll("%(?![0-9a-fA-F]{2})", "%25"), encoding);

		} finally {
			try {
				if (outStream != null)
					outStream.close();
				if (inStream != null)
					inStream.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			if (null != conn) {
				conn.disconnect();
			}
		}
		Log.i(TAG, RetBak);
		return RetBak;
	}
}
