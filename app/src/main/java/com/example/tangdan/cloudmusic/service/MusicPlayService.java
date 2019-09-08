package com.example.tangdan.cloudmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import com.example.tangdan.cloudmusic.activity.MusicPlayActivity;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;

import java.io.IOException;

public class MusicPlayService extends Service implements MusicPlayActivity.IPlayService {
    private static final String SONG_PATH = "SONG_PATH";
    private static final String TAG = "MusicPlayActivity";

    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton;

    private MediaPlayer mMediaPlayer;
    private Runnable mRunnable;
    private Thread mThread;
    private int mDuration;
    private String mSongPath;
    private int currentPos;
    private MusicPlayActivity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        activity = new MusicPlayActivity();
        activity.setPlayServiceListener(this);
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
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void jumpPosToPlayService(float pos) {
        mMediaPlayer.seekTo((int) pos * mDuration);
    }

    @Override
    public void setSongUri(String path) {
        this.mSongPath = path;
    }

    public class MyBinder extends Binder {
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
        private final Object lock = new Object();
        private volatile boolean pause = false;

        @Override
        public void run() {
            while (true) {
                while (pause) {
                    onPause();
                }
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

        public void onPauseThread() {
            pause = true;
        }

        public void onResumeThread() {
            pause = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        private void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
