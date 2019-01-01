package com.example.convest;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class MediaUtil{

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

    public static List<HashMap<String, String>> getMusicMaps(
            List<Mp3> mp3s) {
        List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
        // String music_menu = String.valueOf(R.drawable.music_menu);
        // String check_music = String.valueOf(R.drawable.check);
        int i = 0;//<span style="white-space:pre">		//</span>//定义歌曲的序号
        for (Iterator iterator = mp3s.iterator(); iterator.hasNext();) {   //迭代器
            i++;
            Mp3 mp3 = (Mp3) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("number",String.valueOf(i));
            map.put("id",String.valueOf(mp3.getId()));
            map.put("title", mp3.getTitle());
            map.put("Artist", mp3.getArtist());
            map.put("duration", formatTime(mp3.getDuration()));
            map.put("size", String.valueOf(mp3.getSize()));
            map.put("url", mp3.getUrl());
            // map.put("music_menu",music_menu);
            // map.put("check_music",check_music);
            mp3list.add(map);
        }
        return mp3list;
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

}

