package com.example.tangdan.cloudmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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

    private boolean isSongPrepared;

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
        try {
            if (intent != null && intent.getStringExtra(SONG_PATH) != null && !intent.getStringExtra(SONG_PATH).equals(mSongPath)) {
                mSongPath = intent.getStringExtra(SONG_PATH);
                mMediaPlayer.reset();
                Uri uri = Uri.parse(mSongPath);
                mMediaPlayer.setDataSource(mSongPath);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.d("TAGTAG", "开始播放");
                        mp.start();
                        mDuration = mp.getDuration();
                    }
                });
            }
        } catch (IOException e) {
            Log.e("TAGTAG", "error:" + e);
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
            return mDuration;
        }

        public void setPosToPlay(float pos) {
            mMediaPlayer.seekTo((int) (pos * mDuration));
        }

        public int getPlayPos() {
            return currentPos;
        }

        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        public void setPlay(boolean enabled) {
            if (enabled) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }

        public void playNextSong(String songPath) {
            try {
                mSongPath = songPath;
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(songPath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mDuration = mMediaPlayer.getDuration();
            } catch (IOException e) {
                e.printStackTrace();
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
                        break;
                    }
                    if (mDuration != -1 && flag) {
                        currentPos = mMediaPlayer.getCurrentPosition();
                    }
                }
            }
        }
    }

}
