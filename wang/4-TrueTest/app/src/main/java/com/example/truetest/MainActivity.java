package com.example.truetest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer = new MediaPlayer(); //获得mediaPlayer对象


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
       // Button up = (Button) findViewById(R.id.upgo);
       // Button down = (Button) findViewById(R.id.downgo);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
       // up.setOnClickListener(this);
       // down.setOnClickListener(this);
        //申请运行时权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            initMediaPlayer(); //初始化MediaPlayer类
        }
    }

    private void initMediaPlayer() {
        try {
            //创建MP3文件
            File file = new File(Environment.getExternalStorageDirectory(),"music1.mp3");
           // File file2 = new File(Environment.getExternalStorageDirectory(),"music2.mp3");
           // File file3 = new File(Environment.getExternalStorageDirectory(),"music3.mp3");
            //指定音频文件路径
            mediaPlayer.setDataSource(file.getPath());
           // mediaPlayer.setDataSource(file2.getPath());
           // mediaPlayer.setDataSource(file3.getPath());
            mediaPlayer.prepare();  //进入准备状态
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissonsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
           /* case R.id.upgo:
                upgo();
                break;
            case R.id.downgo:
                downgo();
                break;*/
            default:
                break;
        }
    }
/*
    private void upgo() {

    }
*/
   /* private void downgo() {

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
