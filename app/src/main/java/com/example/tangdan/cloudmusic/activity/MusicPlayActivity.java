package com.example.tangdan.cloudmusic.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;

import java.io.IOException;

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener, MusicPlayProgressBar.ProgressBarListener {
    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton;

    private MediaPlayer mMediaPlayer;
    private MyMusicThread mTread;
    private int mDuration;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0X00:
                    int pos = (int) msg.obj;
                    mMusicPlayProgressBar.setProgress((float) pos / mMediaPlayer.getDuration());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplay);
        mMediaPlayer = new MediaPlayer();
        mMusicPlayProgressBar = (MusicPlayProgressBar) findViewById(R.id.progress_music_bar);
        mPlayButton = (Button) findViewById(R.id.btn_stoporplay);
        mPlayButton.setOnClickListener(this);
        mMusicPlayProgressBar.setProgressBarListener(this);

        try {
            mMediaPlayer.setDataSource("/storage/emulated/0/netease/cloudmusic/Music/Lucky Stroke - Good Day.mp3");
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mTread = new MyMusicThread();
        mTread.start();
    }

    @Override
    public void jumpPosToPlay(float pos) {
        int totalSec= mDuration;
        mMediaPlayer.seekTo((int)(totalSec*pos));
    }

    private class MyMusicThread extends Thread {
        private final Object lock = new Object();
        private volatile boolean pause = false;

        int currentPos;

        @Override
        public void run() {
            super.run();
            mDuration = mMediaPlayer.getDuration();
            while (true) {
                while (pause) {
                    onPause();
                }
                if (mDuration != -1 && mMediaPlayer.isPlaying()) {
                    currentPos = mMediaPlayer.getCurrentPosition();
                    Message msg = Message.obtain();
                    msg.obj = currentPos;
                    msg.what = 0X00;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stoporplay:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mTread.onPauseThread();
                } else {
                    mTread.onResumeThread();
                    mMediaPlayer.start();
                }
                break;
            default:
                break;
        }
    }
}
