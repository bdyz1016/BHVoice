package com.bhsc.mobile.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei on 16/4/11.
 */
public class UploadFile {
    private static final String TAG = UploadFile.class.getSimpleName();
    public static String uploadMultiFileSync(String actionUrl,Map<String,String> headers,  List<File> fileList, Map<String, String> params) throws IOException {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        URL url = new URL(actionUrl);
        connection = (HttpURLConnection) url.openConnection();

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

//        StringBuffer stringBuffer = new StringBuffer();
//        if (params != null && !params.isEmpty()) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                stringBuffer.append(entry.getKey()).append('=');
//                stringBuffer.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//                stringBuffer.append('&');
//            }
//            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
//        }

        // Allow Inputs &amp; Outputs.
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        // Set HTTP method to POST.
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = new DataOutputStream(connection.getOutputStream());

        for(int i = 0;i < fileList.size();i++) {
            FileInputStream fileInputStream = new FileInputStream(fileList.get(i));
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file" + i +"\";filename=\"" + fileList.get(i).getName() + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            fileInputStream.close();
        }
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        // Responses from the server (code and message)
        int serverResponseCode = connection.getResponseCode();
        String serverResponseMessage = connection.getResponseMessage();
        StringBuilder builder = new StringBuilder();
        if (serverResponseCode == 200) {
            InputStream in = connection.getInputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                builder.append((char) ch);
            }
        }
        outputStream.flush();
        outputStream.close();

        return builder.toString();
    }
}
