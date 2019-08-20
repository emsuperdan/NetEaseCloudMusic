package com.example.tangdan.cloudmusic.utils;

import android.media.MediaPlayer;

public class MediaSingleInstance extends MediaPlayer {
    private volatile static MediaPlayer mMediaPlayer;

    private MediaSingleInstance() {
    }

    public static MediaSingleInstance getInstance() {
        if (mMediaPlayer != null) {
            synchronized (MediaSingleInstance.class) {
                if (mMediaPlayer != null) {
                    mMediaPlayer = new MediaSingleInstance();
                }
            }
        }
        return  null;
    }
}
