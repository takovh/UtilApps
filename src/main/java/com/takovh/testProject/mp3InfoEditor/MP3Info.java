package com.takovh.testProject.mp3InfoEditor;

public class MP3Info {
    private String songName;
    private String artist;
    private String album;
    private String year;
    private String comment;

    public MP3Info() {
    }

    public MP3Info(String songName, String artist, String album, String year, String comment) {
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.year = year;
        this.comment = comment;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "MP3Info{" +
                "songName='" + songName + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", year='" + year + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public String getComment() {
        return comment;
    }
}
