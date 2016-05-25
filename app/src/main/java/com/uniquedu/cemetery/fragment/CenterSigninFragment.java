package com.uniquedu.cemetery.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.bean.SignIn;
import com.uniquedu.cemetery.utils.SharedPreferencesKey;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhongHang on 2016/4/24.
 */
public class CenterSigninFragment extends BaseFragment {
    private static final String TAG = "CenterSignin";
    private static final int LOAD_SUCCESS = 0x33;
    private EditText mEdittextAccount;
    private EditText mEditTextPassword;
    private EditText mEditTextCodes;
    private Button mButtonSigned;
    private ImageView mImageViewCodes;
    private RequestQueue queue;
    private HashMap<String, String> cookieMap = new HashMap<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_SUCCESS:
                    File file = new File(getContext().getCacheDir(), "pic.jpg");
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        mImageViewCodes.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    //    ：key==>ASP.NET_SessionId   value===>23ekvwbzxltskh1n5smsafzp
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());
        View view = inflater.inflate(R.layout.center_login, null);
        mImageViewCodes = (ImageView) view.findViewById(R.id.imageview_codes);
        mEdittextAccount = (EditText) view.findViewById(R.id.edittext_account);
        mEditTextPassword = (EditText) view.findViewById(R.id.edittext_password);
        mEditTextCodes = (EditText) view.findViewById(R.id.edittext_codes);
        mButtonSigned = (Button) view.findViewById(R.id.button_sign);
        getCodes();
        mImageViewCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCodes();
            }
        });
        mButtonSigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处登录
                String account = mEdittextAccount.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();
                String codes = mEditTextCodes.getText().toString().trim();
                Toast.makeText(CenterSigninFragment.this.getContext(), "点击了登录  参数" + account + "  " + password + "  " + codes, Toast.LENGTH_SHORT).show();
                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("account", account);
                params.put("pwd", password);
                params.put("code", codes);
                params.put("callback", "callback");
                StringRequest request = new StringRequest(Request.Method.POST, Address.LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("login", "登录成功:" + response);
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        response = response.trim();
                        if (response.contains("callback")) {
                            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                        }
                        Gson gson = new Gson();
                        SignIn signIn = gson.fromJson(response, SignIn.class);
                        if (signIn.getExCode().equals("1")) {
                            Toast.makeText(CenterSigninFragment.this.getContext(), signIn.getExMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            //此处登录成功
                            SharedPreferencesUtil.saveData(getActivity().getApplicationContext(), "login", true);
                            SharedPreferencesUtil.saveData(getActivity().getApplicationContext(), "signIn", response);
                            //切换到登录成功的fragment
                            ((CenterFragment) getParentFragment()).replace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("login", "登录失败,请重新登录");
                        System.out.println("登录失败" + error.networkResponse.statusCode);
                        Toast.makeText(CenterSigninFragment.this.getContext(), "登录失败" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //登录时提交的参数
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        //返回验证码的头文件。必须是一个会话，里面存储了seesion。在下载图片时保存了该数据
                        return cookieMap;
                    }
                };

                queue.add(request);
            }
        });
        return view;
    }

    private void getCodes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.whjfs.com/mvcwebmis/login/checkcode");
                    CookieManager cookieManager = new CookieManager();
                    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                    CookieHandler.setDefault(cookieManager);
                    HttpURLConnection httpurlconn = (HttpURLConnection) url.openConnection();
                    httpurlconn.setRequestMethod("GET");
                    //设置编码格式
                    //设置接受的数据类型
                    httpurlconn.setRequestProperty("Accept-Charset", "utf-8");
                    //设置可以序列化的java对象
                    httpurlconn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded");

                    int code = httpurlconn.getResponseCode();
                    if (code == 200) {
                        httpurlconn.getHeaderFields();
                        CookieStore store = cookieManager.getCookieStore();
                        List<HttpCookie> cookies = store.getCookies();

                        for (HttpCookie cookie : cookies) {
                            String key = cookie.getName();
                            String value = cookie.getValue();
                            cookieMap.put(key, value);
//                        Toast.makeText(MainActivity.this, "获得cookie：key==>" + key + "   value===>" + value, Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", "获得cookie：key==>" + key + "   value===>" + value);
                            //        BDebug.e(HTTP_COOKIE, cookie.getName() + "---->" + cookie.getDomain() + "------>" + cookie.getPath());
                        }
                        InputStream is = httpurlconn.getInputStream();
                        //构建一个file对象用于存储图片
                        File file = new File(getContext().getCacheDir(), "pic.jpg");
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        //将输入流写入到我们定义好的文件中
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        //将缓冲刷入文件
                        fos.flush();
                        //告诉handler，图片已经下载成功
                        handler.sendEmptyMessage(LOAD_SUCCESS);
                        Log.d(TAG, "获得的状态码是：" + code);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
