package com.uniquedu.cemetery;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.uniquedu.cemetery.utils.SharedPreferencesKey;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhongHang on 2016/3/3.
 */
public class AppApplication extends Application {
    private static final int MESSAGE_USER = 4;
    private static final int MESSAGE_TITLE = 5;
    private static final int MESSAGE_CONTENT = 6;
    RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        queue = Volley.newRequestQueue(this);
        getMessage(MESSAGE_USER);
        getMessage(MESSAGE_TITLE);
        getMessage(MESSAGE_CONTENT);
    }

    public void getMessage(final int state) {
        StringRequest request = new StringRequest(Request.Method.POST, Address.GET_DIC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("jiufeng", "得到留言提示：" + response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                response = response.trim();
                if (response.contains("callbackname")) {
                    response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("rows");
                    switch (state) {
                        case MESSAGE_USER:
                            SharedPreferencesUtil.saveData(getApplicationContext(), SharedPreferencesKey.KEY_USER, array.toString());
                            break;
                        case MESSAGE_TITLE:
                            SharedPreferencesUtil.saveData(getApplicationContext(), SharedPreferencesKey.KEY_TITLE, array.toString());
                            break;
                        case MESSAGE_CONTENT:
                            SharedPreferencesUtil.saveData(getApplicationContext(), SharedPreferencesKey.KEY_CONTENT, array.toString());
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("callback", "callbackname");
                params.put("DicTypeName", "" + state);
                return params;
            }
        };
        queue.add(request);
    }
}
