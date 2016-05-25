package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.service.MusicService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MPlayerActivity extends BaseActivity {
    private String[] musicNames = {"生死不离", "父亲", "母亲", "父亲母亲"};
    private String[] musicTime = {"04:11", "04:33", "04:40",
            "04:41"};
    private int[] musicrul = {R.raw.life_and_death, R.raw.father, R.raw.mother, R.raw.father_mother};
    private ListView musicList;
    private List<Map<String, String>> listems;
    private ImageView music_image;
    private TextView play_back;
    private RequestQueue mQueue;
    private String id;//纪念馆id
    private String musicId;
    private boolean isPlay = true;
    private LayoutInflater mInflater;
    private MusicAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mInflater = getLayoutInflater();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        musicId = intent.getStringExtra("musicId");
        mQueue = Volley.newRequestQueue(getApplicationContext());
        initialize();
        getmusicdata();
        lvclick();
    }

    private void initialize() {
        play_back = (TextView) findViewById(R.id.play_back);
        musicList = (ListView) findViewById(R.id.music_list);
        play_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    private void getmusicdata() {
        listems = new ArrayList<Map<String, String>>();
        for (int i = 0; i < musicNames.length; i++) {
            Map<String, String> listem = new HashMap<String, String>();
            listem.put("name", musicNames[i]);
            listem.put("time", musicTime[i]);
            listems.add(listem);
        }
        mAdapter = new MusicAdapter();
        musicList.setAdapter(mAdapter);
        mAdapter.select(Integer.parseInt(musicId));
    }

    private void lvclick() {
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(MPlayerActivity.this, MusicService.class);
                int select = position;
                if (musicId.equals(position + "") && isPlay) {
                    it.putExtra("isPlay", false);
                    isPlay = false;
                    select = -1;
                }
                it.putExtra("musicid", select);
                startService(it);
                mAdapter.select(select);
                sendMusicId(select);
            }
        });
    }

    private void sendMusicId(final int i) {
        StringRequest srReq = new StringRequest(Request.Method.POST, Address.MUSIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("tag", "发送成功" + s);
                    }
                }, null) {
//                     callback=callbakename&id=3079&state=" + i

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("callback", "callbakename");
                params.put("id", id);
                params.put("state", "" + i);
                return params;
            }
        };
        mQueue.add(srReq);
    }

    class MusicAdapter extends BaseAdapter {
        //        SimpleAdapter simplead = new SimpleAdapter(this, listems,
//                R.layout.list_music, new String[]{"name", "time"},
//                new int[]{R.id.text_music_name, R.id.text_music_time});
        private int selectPosition;

        @Override
        public int getCount() {
            return listems.size();
        }

        public void select(int select) {
            selectPosition = select;
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.list_music, null);
            TextView textViewName = (TextView) convertView.findViewById(R.id.text_music_name);
            TextView textViewTime = (TextView) convertView.findViewById(R.id.text_music_time);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.music_image_play);
            textViewName.setText(listems.get(position).get("name"));
            textViewTime.setText(listems.get(position).get("time"));
            if (selectPosition == position) {
                imageView.setSelected(true);
                textViewName.setSelected(true);
                textViewTime.setSelected(true);
            }
            return convertView;
        }
    }
}
