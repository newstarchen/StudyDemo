package com.example.truetest;

public class Mp3 {
    private String songName;

    private String artist;

    private int songId;

   /* public void setSongId(int songId) {
        this.songId = songId;
    }*/

    public int getSongId() {
        return songId;
    }

    /*public void setSongName(String songName) {
        this.songName = songName;
    }*/

    public String getSongName() {
        return songName;
    }

   /* public void setArtist(String artist) {
        this.artist = artist;
    }*/

    public String getArtist() {
        return artist;
    }

    public Mp3(int songId, String songName, String artist) {
        this.songId = songId;
        this.songName = songName;
        this.artist = artist;
    }
}
