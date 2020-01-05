package com.example.tangdan.cloudmusic.model;

import java.io.Serializable;

public class MusicModel implements BaseModel,Serializable {
    private static final long serialVersionUID = 1L;

    private String mTitle;
    private String mArtist;
    private long mId;
    private long mSize;
    private long mDuration;
    private String mLocalMicUrl;
    private String mLiveMicUrl;
    private String mAlbum;
    private long mAlbumId;
    private String mSongId;
    private boolean mIsLocalMic;

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

    public String getmLocalMicUrl() {
        return mLocalMicUrl;
    }

    public void setmLocalMicUrl(String mLocalMicUrl) {
        this.mLocalMicUrl = mLocalMicUrl;
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

    @Override
    public TitleState getModelType() {
        return TitleState.MUSICMODEL;
    }

    public String getmSongId() {
        return mSongId;
    }

    public void setmSongId(String mSongId) {
        this.mSongId = mSongId;
    }

    public boolean ismIsLocalMic() {
        return mIsLocalMic;
    }

    public void setmIsLocalMic(boolean mIsLocalMic) {
        this.mIsLocalMic = mIsLocalMic;
    }

    public String getmLiveMicUrl() {
        return mLiveMicUrl;
    }

    public void setmLiveMicUrl(String mLiveMicUrl) {
        this.mLiveMicUrl = mLiveMicUrl;
    }
}
