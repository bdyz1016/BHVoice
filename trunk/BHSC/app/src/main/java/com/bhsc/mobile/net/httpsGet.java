package com.bhsc.mobile.net;

import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import android.util.Log;

public class httpsGet {

	public static final String TAG = "httpsGet";
	private final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();

	private X509TrustManager xtm = new X509TrustManager() {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };

	// 增加捕获超时异常
	public String requireClass(String path, Map<String, String> params, String encoding) throws ConnectTimeoutException, SocketTimeoutException, Exception {

		String bakn = "", RetBak = "";

		HttpsURLConnection conn = null;
		InputStream inStream = null;

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

			String httpurl = path + buffer.toString();
			Log.i(TAG, "httspurl_GET" + httpurl);
			URL url = new URL(httpurl);
			conn = (HttpsURLConnection) url.openConnection();

			if (conn instanceof HttpsURLConnection) {
				// Trust all certificates
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0], xtmArray, new SecureRandom());
				SSLSocketFactory socketFactory = context.getSocketFactory();
				conn.setSSLSocketFactory(socketFactory);
				conn.setHostnameVerifier(HOSTNAME_VERIFIER);
			}
			conn.setConnectTimeout(20000);// !!!!
			conn.setReadTimeout(20000);// !!!!
			conn.setRequestMethod("Get");
			conn.setDoInput(true);// 表示从服务器获取数据
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
			Log.i("Return", RetBak);
		} finally {
			try {
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
