package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.PopupWindowCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.adapter.PhotoAdapter;
import com.uniquedu.cemetery.bean.PhotoBean;
import com.uniquedu.cemetery.bean.SignIn;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshBase;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshListView;
import com.uniquedu.cemetery.utils.BitmapUtils;
import com.uniquedu.cemetery.utils.PostFileRequest;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by ZhongHang on 2016/4/26.
 */
public class ManagerPhotoActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE = 0x23;
    private ListView mListView;
    private PullToRefreshListView mPullRefreshView;
    private LayoutInflater mInflater;
    private String deadId;
    private ArrayList<PhotoBean> mList;
    private int page = 1;
    private PhotoAdapter mAdapter;
    private static final int STATE_LOAD_MORE = 0;
    private static final int STATE_LOAD_REFRESH = 1;
    private TextView mTextViewNull;
    private TextView mTextViewBack;
    private ImageView mImageViewEdit;
    private ImageView mImageViewCamera;
    private TextView mTextViewHead;
    private TextView mTextViewCancel;
    private ImageView mImageViewDelete;
    private SignIn signIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_photo);
        mTextViewBack = (TextView) findViewById(R.id.grid_back);
        mTextViewHead = (TextView) findViewById(R.id.grid_head);
        mTextViewCancel = (TextView) findViewById(R.id.textview_cancel);
        mImageViewDelete = (ImageView) findViewById(R.id.imageview_delete);
        mTextViewCancel.setOnClickListener(this);
        mImageViewDelete.setOnClickListener(this);
        mTextViewBack.setOnClickListener(this);
        mImageViewEdit = (ImageView) findViewById(R.id.imageview_edit);
        mImageViewEdit.setOnClickListener(this);
        mImageViewCamera = (ImageView) findViewById(R.id.imageview_camera);
        mImageViewCamera.setOnClickListener(this);
        Gson gson = new Gson();
        signIn = gson.fromJson(SharedPreferencesUtil.getData(getApplicationContext(), "signIn", "").toString(), SignIn.class);

        deadId = getIntent().getStringExtra("deadId");
        mInflater = getLayoutInflater();
        mPullRefreshView = (PullToRefreshListView) findViewById(R.id.photo_listview);
        mListView = mPullRefreshView.getRefreshableView();
        mTextViewNull = (TextView) findViewById(R.id.textview_null);
        mList = new ArrayList<>();
        mAdapter = new PhotoAdapter(mInflater, mList);
        mListView.setAdapter(mAdapter);
        mPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(STATE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(STATE_LOAD_MORE);
            }
        });
        loadData(STATE_LOAD_REFRESH);
        mAdapter.setOnItemListener(new PhotoAdapter.OnItemClick() {
            @Override
            public void onItemClick(PhotoBean bean) {
                Intent intent = new Intent(getApplicationContext(), ShowPhotoActivity.class);
                intent.putExtra("photo", bean);
                startActivity(intent);
            }
        });
    }

    private void loadData(int state) {
        if (state == STATE_LOAD_MORE) {
            page++;
        } else {
            page = 1;
        }
        HashMap<String, String> loadmore = new HashMap<>();
        loadmore.put("callback", "callbakename");
        loadmore.put("page", "" + page);
        loadmore.put("rows", "12");
        loadmore.put("id", deadId);
        getPhotos(loadmore, state);
    }

    private void getPhotos(final HashMap<String, String> params, final int state) {
        if (deadId != null) {
            StringRequest request = new StringRequest(Request.Method.POST, Address.WORSHIP_PHOTO, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("返回照片信息", response);
                    List<PhotoBean> list = resloveData(response);
                    if (state == STATE_LOAD_MORE) {
                        if (list.size() != 0) {
                            mList.addAll(list);
                            mAdapter.setPhotos(mList);
                        } else {
                            Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_LONG).show();
                        }
                        mPullRefreshView.onRefreshComplete();
                    } else {
                        if (list.size() != 0) {
                            mList.clear();
                            mList.addAll(list);
                            mAdapter.setPhotos(mList);
                            mPullRefreshView.setVisibility(View.VISIBLE);
                            mTextViewNull.setVisibility(View.INVISIBLE);
                        } else {
                            mTextViewNull.setVisibility(View.VISIBLE);
                            mPullRefreshView.setVisibility(View.INVISIBLE);
                            //显示无数据
                        }
                        mPullRefreshView.onRefreshComplete();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "获取详细信息失败", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        } else {
            Toast.makeText(getApplicationContext(), "获取详细信息失败", Toast.LENGTH_LONG).show();
        }

    }

    private void photoEdit(boolean b) {
        if (b) {
            mTextViewHead.setVisibility(View.INVISIBLE);
            mTextViewBack.setVisibility(View.INVISIBLE);
            mImageViewCamera.setVisibility(View.INVISIBLE);
            mImageViewEdit.setVisibility(View.INVISIBLE);
            mImageViewDelete.setVisibility(View.VISIBLE);
            mTextViewCancel.setVisibility(View.VISIBLE);
        } else {
            mTextViewHead.setVisibility(View.VISIBLE);
            mTextViewBack.setVisibility(View.VISIBLE);
            mImageViewCamera.setVisibility(View.VISIBLE);
            mImageViewEdit.setVisibility(View.VISIBLE);
            mImageViewDelete.setVisibility(View.INVISIBLE);
            mTextViewCancel.setVisibility(View.INVISIBLE);
        }
    }

    private List<PhotoBean> resloveData(String response) {
        List<PhotoBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(response)) {
            return list;
        }
        response = response.trim();
        if (response.indexOf("callbakename") == 0) {
            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
        }
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("rows");
            Gson gson = new Gson();
            Type type = new TypeToken<List<PhotoBean>>() {
            }.getType();
            Object fromJson2 = gson.fromJson(array.toString(), type);
            list = (List<PhotoBean>) fromJson2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private PopupWindow popupWindow;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_back:
                finish();
                break;
            case R.id.imageview_edit:
                photoEdit(true);
                mAdapter.setEditor(true);

                break;
            case R.id.imageview_camera:
                //此处弹出popupwindow
//                createPopupWindow();

                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
// 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
// 默认选择图片,回填选项(支持String ArrayList)
//                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.textview_cancel:
                photoEdit(false);
                mAdapter.setEditor(false);
                break;
            case R.id.imageview_delete:
                photoEdit(false);
                mAdapter.setEditor(false);
                deletePhotos(mAdapter.getListCheckedPhoto());
                Toast.makeText(ManagerPhotoActivity.this, "选中照片张数" + mAdapter.getListCheckedPhoto().size(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void deletePhotos(List<PhotoBean> photos) {
        if (photos.size() == 0) {
            return;
        }
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("callback", "callback");
        builder.add("account", signIn.getAccount());
        builder.add("token", signIn.getToken());
        String ids = "(";
        for (PhotoBean photo : photos) {
            ids += photo.getId() + ",";
        }
        ids += ")";
        builder.add("photoIDs", ids);
        RequestBody body = builder.build();
        okhttp3.Request request = new okhttp3.Request.Builder().post(body).url("http://www.whjfs.com/mvcwebmis/nologin/AppDeleteMemorialPhotos").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络连接错误
                Log.e("slj", "managerPhoto:" + "网络连接返回数据错误");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                //网络连接成功
                Log.e("slj", "managerPhoto:" + "网络连接返回数据正常" + response.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                for (String path : paths) {
                    Log.d("MainActivity", "图片的地址：" + path);
                }
                String picturePath = paths.get(0);
                File file = new File(Environment.getExternalStorageDirectory() + "/upload.jpg");
                Toast.makeText(ManagerPhotoActivity.this, getCacheDir() + "/upload.jpg", Toast.LENGTH_SHORT).show();
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                BitmapUtils.zipImage(picturePath, file.getAbsolutePath());
                postFileOk("http://www.whjfs.com/mvcwebmis/nologin/AppNewMemorialPhoto", file.getAbsolutePath());
            }
        }
    }

    private void postFileHttpClient(final String url, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                {
                    //创建HttpClient客户端
                    HttpClient httpclient = HttpClients.createDefault();
                    try {
                        HttpPost httppost = new HttpPost(url);//创建提交的方法为post
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        builder.addBinaryBody("fileData", new File(filePath));
                        builder.addTextBody("callback", "callback");
                        builder.addTextBody("photoTitle", "20160512");
                        builder.addTextBody("account", signIn.getAccount());
                        builder.addTextBody("token", signIn.getToken());
                        httppost.setEntity(builder.build());
                        HttpResponse response = httpclient.execute(httppost);
                        int statusCode = response.getStatusLine().getStatusCode();
                        if (statusCode == HttpStatus.SC_OK) {
                            System.out.println("服务器正常响应.....");
                            HttpEntity resEntity = response.getEntity();
                            System.out.println(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据
                            System.out.println(resEntity.getContent());
                            EntityUtils.consume(resEntity);
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            httpclient.getConnectionManager().shutdown();
                        } catch (Exception ignore) {

                        }
                    }
                }
            }
        }).start();
    }

    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    private void postFileOk(String url, String filePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        byte[] bytes = getBytes(filePath);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("callback", "callback")
                .addFormDataPart("account", signIn.getAccount())
                .addFormDataPart("token", signIn.getToken())
                .addFormDataPart("photoTitle", format.format(new Date())).addFormDataPart("fileData", new File(filePath).getName(), RequestBody.create(MediaType.parse("image/jpg"), bytes)).build();
        okhttp3.Request request = new okhttp3.Request.Builder().post(body).url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("", "网络连接返回数据错误");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                Log.d("ManagerPhotoActivity", "网络连接返回数据" + result);
//              Toast.makeText(ManagerPhotoActivity.this, "上传返回的信息" + response.body().string(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
