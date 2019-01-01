package com.example.convest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ListActivity extends AppCompatActivity {

    private ListView mMusiclist;                   //音乐列表

    private SimpleAdapter mAdapter;

    List<Mp3> mp3s = null;

    private List<HashMap<String, Object>> mp3list;

    private HashMap<String, Object> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mMusiclist = (ListView) findViewById(R.id.list_view);
        //为ListView添加数据源
        mp3s = MediaUtil.getMp3s(getApplicationContext()); //获取歌曲对象集合
        setListAdpter(MediaUtil.getMusicMaps(mp3s));    //显示歌曲列表
        mMusiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                     int position, long id) {
                Mp3 mp3 = mp3s.get(position);
               // Toast.makeText(ListActivity.this, mp3.getUrl(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("url_return", mp3.getUrl());
                intent.putExtra("index", position);
                Log.d("url_return",mp3.getUrl());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void setListAdpter(List<HashMap<String, String>> mp3list) {
        mAdapter = new SimpleAdapter(ListActivity.this, mp3list,
                R.layout.mp3_item, new String[]{"number","title",
                "Artist"}, new int[]{R.id.number, R.id.music_songName,
                R.id.music_Artist});
        mMusiclist.setAdapter(mAdapter);
    }

}

