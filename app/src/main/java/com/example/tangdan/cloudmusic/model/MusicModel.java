package com.example.tangdan.cloudmusic.model;

public class MusicModel {
    private String mTitle;
    private String mArtist;
    private long mId;
    private long mSize;
    private long mDuration;
    private String mUrl;
    private String mAlbum;
    private long mAlbumId;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public long getmAlbumId() {
        return mAlbumId;
    }

    public void setmAlbumId(long mAlbumId) {
        this.mAlbumId = mAlbumId;
    }
}