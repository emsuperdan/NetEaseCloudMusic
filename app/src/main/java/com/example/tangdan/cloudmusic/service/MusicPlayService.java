package com.example.tangdan.cloudmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.tangdan.cloudmusic.activity.MusicPlayActivity;

import java.io.IOException;

public class MusicPlayService extends Service {
    private static final String SONG_PATH = "SONG_PATH";
    private static final String TAG = "MusicPlayService";

    private MediaPlayer mMediaPlayer;
    private Runnable mRunnable;
    private Thread mThread;
    private int mDuration;
    private String mSongPath;
    private int currentPos;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mRunnable = new MyRunnable();
        mThread = new Thread(mRunnable);
        mThread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (intent != null) {
            mSongPath = intent.getStringExtra(SONG_PATH);
        }
        try {
            mMediaPlayer.setDataSource(mSongPath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mDuration = mMediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public class MyBinder extends Binder {
        public void setSongUri(String uri) {
            mSongPath = uri;
        }

        public int getPlayDuration() {
            return mMediaPlayer.getDuration();
        }

        public void setPosToPlay(float pos) {
            mMediaPlayer.seekTo((int) pos * mDuration);
        }

        public int getPlayPos() {
            return currentPos;
        }

        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        public void setPlay(boolean enabled) {
            if (enabled) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        }
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (mMediaPlayer != null) {
                    boolean flag = false;
                    try {
                        flag = mMediaPlayer.isPlaying();
                    } catch (Exception e) {
                        Log.d(TAG, "Error since mediaplay is released" + e);
                    }
                    if (mDuration != -1 && flag) {
                        currentPos = mMediaPlayer.getCurrentPosition();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
        }
    }

}