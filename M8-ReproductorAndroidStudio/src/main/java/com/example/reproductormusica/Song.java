package com.example.reproductormusica;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String coverImagePath;
    private String filePath;

    public Song(String title, String artist, String album, String coverImagePath, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.coverImagePath = coverImagePath;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
