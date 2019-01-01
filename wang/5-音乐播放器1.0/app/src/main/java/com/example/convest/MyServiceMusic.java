package com.example.convest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MyServiceMusic extends Service {

    private MediaPlayer mediaPlayer;

    public MyServiceMusic() {
    }

    public class Mybind extends Binder {
        //获取歌曲长度
        public int getMusicDuration()
        {
            int rtn = 0;
            if (mediaPlayer != null)
            {
                rtn = mediaPlayer.getDuration();
            }

            return rtn;
        }

        //获取当前播放进度
        public int getMusicCurrentPosition() {
            int rtn = 0;
            if (mediaPlayer != null) {
                rtn = mediaPlayer.getCurrentPosition();
            }
            return rtn;
        }

        public void seekTo(int position)
        {
            if (mediaPlayer != null)
            {
                mediaPlayer.seekTo(position);
            }
        }

    }
        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            //throw new UnsupportedOperationException("Not yet implemented");
            return new Mybind();
        }

}
