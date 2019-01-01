package com.example.convest;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import android.database.Cursor;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.os.Handler;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar
        .OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer = new MediaPlayer();//获得mediaPlayer对象

    // private MediaPlayer mmediaPlayer = new MediaPlayer();
    private TextView gequ;

    private TextView geshou;

    private TextView mTvDef;

    private TextView Dution;

    private SeekBar mSeekBarDef;

    private int dangqianweizhi;

    //private ServiceConnection serviceConnection;

    //private MyServiceMusic.Mybind mybind;

    private Button play;
    private Button stop;
    private Button up;
    private Button down;
    private Button liebiao;
    private Button bofangfashi;
    private int suoying = 0;
    private int fashi = 0;

    private int image[] = {
            R.drawable.play,
            R.drawable.pause
    };
    // private File file[];

    //List<Mp3> mp3s = new ArrayList<Mp3>();

    private int fashianniu[] = {
            R.drawable.shunxu,
            R.drawable.suiji,
            R.drawable.danqu
    };

    private MyDatabaseHelper dbHelper;

    private Handler mhd = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int position = mediaPlayer.getCurrentPosition();
            mSeekBarDef.setProgress(position);
            mTvDef.setText(formatTime(position));
            update();
            return true;
        }
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        up = (Button) findViewById(R.id.upgo);
        down = (Button) findViewById(R.id.downgo);
        liebiao = (Button) findViewById(R.id.liebiao);
        bofangfashi = (Button) findViewById(R.id.bofangfashi);
        dbHelper = new MyDatabaseHelper(this, "MusicStore.db", null, 1);
        Button savedata = (Button) findViewById(R.id.save_data);

        savedata.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        liebiao.setOnClickListener(this);
        bofangfashi.setOnClickListener(this);

        mTvDef = (TextView) findViewById(R.id.tv_def);
        Dution = (TextView) findViewById(R.id.dution);
        mSeekBarDef = (SeekBar) findViewById(R.id.seekbar_def);
        mSeekBarDef.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(this);
        gequ = (TextView) findViewById(R.id.gequ);
        geshou = (TextView) findViewById(R.id.geshou);
        //mediaPlayer.setOnCompletionListener(MediaPlayer.OnCompletionListener);
        //申请运行时权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getMp3s(getApplicationContext());
            initMediaPlayer(); //初始化MediaPlayer类
        }
    }

    public static List<Mp3> getMp3s(Context context) {
        Cursor cursor = context.getContentResolver().query(   //获取内容解析器
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,  //听方面的媒体库，外部内容URI；
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);  //默认排序次序
        List<Mp3> mp3s = new ArrayList<Mp3>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Mp3 mp3 = new Mp3();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));               //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));            //音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));            //艺术家
            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));          //时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));              //文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));          //是否为音乐
            if (isMusic != 0) {     //只把音乐添加到集合当中
                    mp3.setId(id);
                    mp3.setTitle(title);
                    mp3.setArtist(artist);
                    mp3.setDuration(duration);
                    mp3.setSize(size);
                    mp3.setUrl(url);
                    mp3s.add(mp3);
            }
        }
        return mp3s;
    }

    // private void initMediaPlayer() {
    //  Cursor cursor = getContentResolver().query(   //获取内容解析器
    //      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,  //听方面的媒体库，外部内容URI；
    //     MediaStore.Audio.Media.DEFAULT_SORT_ORDER);  //默认排序次序
    //  for (int i = 0; i < cursor.getCount(); i++) {
    //MediaUtil.getMp3s(getApplicationContext()).get(i).getUrl();
    //创建MP3文件
    //     cursor.moveToNext();
    //   String url = cursor.getString(cursor
    //           .getColumnIndex(MediaStore.Audio.Media.DATA));
    //    int isMusic = cursor.getInt(cursor
    //           .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
    // if (isMusic != 0) {     //只把音乐添加到集合当中
    //  }
    //   }
    private void initMediaPlayer() {
        try {
            //int j;
            // for (j = 0; j < mp3s.size(); j++){
            File file = new File(getMp3s(getApplicationContext()).get(suoying).getUrl());
            //指定音频文件路径
            mediaPlayer.reset();
            //    mediaPlayer.setDataSource(url);
            mediaPlayer.setDataSource(file.getPath());
            // }
            //mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();//进入准备状态
            gequ.setText(getMp3s(getApplicationContext()).get(suoying).getTitle());
            geshou.setText(getMp3s(getApplicationContext()).get(suoying).getArtist());
            Dution.setText(formatTime(getMp3s(getApplicationContext()).get(suoying).getDuration()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSeekBarDef.setProgress(0);
        mSeekBarDef.setMax(getMp3s(getApplicationContext()).get(suoying).getDuration());
        update();
    }

    public void onRequestPermissonsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMp3s(getApplicationContext());
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
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
                    play.setBackgroundResource(image[1]);
                    savedata();
                } else {
                    mediaPlayer.pause();
                    play.setBackgroundResource(image[0]);
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                    play.setBackgroundResource(image[0]);
                    initMediaPlayer();
                }
                break;
            case R.id.upgo:
                upgo(getMp3s(getApplicationContext()));
                break;
            case R.id.downgo:
                downgo(getMp3s(getApplicationContext()));
                break;
            case R.id.liebiao: {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.save_data: {
                dbHelper.getWritableDatabase();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.bofangfashi: {
                if (fashi == 0) {
                    bofangfashi.setBackgroundResource(fashianniu[1]);
                    fashi++;
                } else if (fashi == 1) {
                    bofangfashi.setBackgroundResource(fashianniu[2]);
                    fashi++;
                } else if (fashi == 2) {
                    bofangfashi.setBackgroundResource(fashianniu[0]);
                    fashi = 0;
                }
            }
            default:
                break;
        }
    }

    private void upgo(List<Mp3> mp3s) {
        if (suoying <= 0) {
            suoying = mp3s.size() - 1;
            initMediaPlayer();
            mediaPlayer.start();
            play.setBackgroundResource(image[1]);
            savedata();
            //Toast.makeText(MainActivity.this, "已经是第一首了", Toast.LENGTH_SHORT).show();
        } else {
            suoying--;
            initMediaPlayer();
            mediaPlayer.start();
            play.setBackgroundResource(image[1]);
            savedata();
        }
    }

    private void downgo(List<Mp3> mp3s) {
        if (suoying >= mp3s.size() - 1) {
            suoying = 0;
            initMediaPlayer();
            mediaPlayer.start();
            play.setBackgroundResource(image[1]);
            savedata();
            //Toast.makeText(MainActivity.this,"已经是最后一首了", Toast.LENGTH_SHORT).show();
        } else {
            suoying++;
            initMediaPlayer();
            mediaPlayer.start();
            play.setBackgroundResource(image[1]);
            savedata();
        }
    }

    private void savedata() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", getMp3s(getApplicationContext()).get(suoying).getTitle());
        values.put("Artist", getMp3s(getApplicationContext()).get(suoying).getArtist());
        db.insert("Music", null, values);
        values.clear();
    }

    private void update() {
        Message msg = Message.obtain();
        int position = mediaPlayer.getCurrentPosition();
        msg.arg1 = position;
        mhd.sendMessageDelayed(msg, 500);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                        String returnedData = data.getStringExtra("url_return");
                        suoying = data.getIntExtra("index", 0);
                        Log.d("111111111111", returnedData);
                        File file = new File(returnedData);
                        //Toast.makeText(MainActivity.this, "111", Toast.LENGTH_SHORT).show();
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(file.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        gequ.setText(getMp3s(getApplicationContext()).get(suoying).getTitle());
                        geshou.setText(getMp3s(getApplicationContext()).get(suoying).getArtist());
                        Dution.setText(formatTime(getMp3s(getApplicationContext()).get(suoying).getDuration()));
                        play.setBackgroundResource(image[1]);
                        savedata();
                        mSeekBarDef.setProgress(0);
                        mSeekBarDef.setMax(getMp3s(getApplicationContext()).get(suoying).getDuration());
                        update();
                    }
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            switch (seekBar.getId()) {
                case R.id.seekbar_def: {
                    // 设置“与系统默认SeekBar对应的TextView”的值
                    // mTvDef.setText(getResources().getString(R.string.text_def) + " : " + String.valueOf(seekBar.getProgress()));
                    mediaPlayer.seekTo(seekBar.getProgress());
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatTime(int time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (fashi == 0) {
            downgo(getMp3s(getApplicationContext()));
        } else if (fashi == 1) {
            suoying = (int)(0 + Math.random() * (getMp3s(getApplicationContext()).size()));
            initMediaPlayer();
            mediaPlayer.start();
        } else if (fashi == 2) {
            if (suoying == 0) {
                suoying++;
                upgo(getMp3s(getApplicationContext()));
            } else {
                suoying--;
                downgo(getMp3s(getApplicationContext()));
            }
        }
    }

    /*
    Intent intent = new Intent(MainActivity.this,MyServiceMusic.class);

    intent.putExtra("action","play");

    startService(intent);


    if (serviceConnection == null) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mybind = (MyServiceMusic.Mybind) service;
                //设置进度条的最大长度
                long max = getMp3s(getApplicationContext()).get(suoying).getDuration();
                mSeekBarDef.setMax(max);
                mSeekBarDef.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mybind.seekTo(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                //连接之后启动子线程设置当前进度
                new Thread()
                {
                    public void run()
                    {
                        //改变当前进度条的值
                        //设置当前进度
                        while (true) {
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }

                }.start();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        //以绑定方式连接服务
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }
*/
}



