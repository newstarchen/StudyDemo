package com.example.truetest;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<Mp3> Mp3List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initMp3s();
        Mp3Adapter adapter = new Mp3Adapter(ListActivity.this, R.layout.mp3_item, Mp3List);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    private void initMp3s() {
        Mp3 music1 = new Mp3(1, "哈哈", "小红");
        Mp3List.add(music1);
        Mp3 music2 = new Mp3(1, "呵呵", "小明");
        Mp3List.add(music2);
        Mp3 music3 = new Mp3(1, "啧啧", "小芳");
        Mp3List.add(music3);
        Mp3 music4 = new Mp3(1, "啦啦", "小何");
        Mp3List.add(music4);
        Mp3 music5 = new Mp3(1, "嘟嘟", "小胖");
        Mp3List.add(music5);
        Mp3 music6 = new Mp3(1, "嘿嘿", "小张");
        Mp3List.add(music6);
    }
}
