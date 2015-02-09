package com.yjkim.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.yjkim.dugout.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jehyeok on 2/6/15.
 */
public class AsyncHttpTask extends AsyncTask<String, String, Void> {

    private Context context;
    private OnTaskCompleted listener;
    private String url;
    private InputStream inputStream = null;
    private String result = "";
    private DefaultHttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private String method;
    private ContentType utf8Type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
    private ProgressDialog pd;

    public AsyncHttpTask(DefaultHttpClient httpClient, String method, Context context) {
        this.httpClient = httpClient;
        this.method = method;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        MyApplication.localContext.setAttribute(ClientContext.COOKIE_STORE, MyApplication.cookieStore);
        String cookie = MyApplication.preferences.getString("_anonymous_session", "");
        Log.d("AsyncTask", "_anonymous_session: " + cookie);

        try {
            HttpResponse httpResponse = null;
            if (method.equals("GET")) {
                httpGet = new HttpGet(url);
                httpGet.setHeader("Cookie", "_anonymous_session=" + cookie + ";");
                httpResponse = httpClient.execute(httpGet, MyApplication.localContext);
            } else if (method.equals("POST")) {
                httpPost = new HttpPost(url);
                httpPost.setHeader("Cookie", "_anonymous_session=" + cookie + ";");

                if (params != null) {
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//              왜하는거지 이건 ?
                    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                    for (String param : params) {
                        String[] keyValueSet = param.split(":");
                        String key = keyValueSet[0].trim();
                        String value = keyValueSet[1].trim();

                        builder.addTextBody(key, value, utf8Type);
                        httpPost.setEntity(builder.build());
                    }
                }

                httpResponse = httpClient.execute(httpPost, MyApplication.localContext);
            }
            Log.d("AsynkHttpTask doInBackground", "url: " + url);

            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
        }
        catch (ClientProtocolException e) {
            Log.e("AsyncHttpTask", e.toString());
        }
        catch (IOException e) {
            Log.e("AsyncHttpTask", e.toString());
        }

        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result = sb.toString();
        }
        catch (IOException e) {
            Log.e("AsyncHttpTask", "Error converting result " + e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("HttpAsyncTask", "result from server: " + this.result);
//		Toast toast = Toast.makeText(context, "인증번호가 올바르지 않습니다", Toast.LENGTH_SHORT);
        listener.onTaskCompleted(this.result);
        pd.dismiss();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setListener(OnTaskCompleted listener) {
        this.listener = listener;
    }
}
