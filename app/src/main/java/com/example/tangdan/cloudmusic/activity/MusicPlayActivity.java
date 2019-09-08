package com.example.tangdan.cloudmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;
import com.example.tangdan.cloudmusic.service.MusicPlayService;

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener, MusicPlayProgressBar.ProgressBarListener {
    private static final String SONG_PATH = "SONG_PATH";
    private static final String TAG = "MusicPlayActivity";

    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton;

    private String mSongPath;
    private MyConnection mConnection;
    private MusicPlayService.MyBinder mPlayService;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0X00:
                    mMusicPlayProgressBar.setProgress((float) mPlayService.getPlayPos() / mPlayService.getPlayDuration());
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplay);
        mMusicPlayProgressBar = (MusicPlayProgressBar) findViewById(R.id.progress_music_bar);
        mPlayButton = (Button) findViewById(R.id.btn_stoporplay);
        mPlayButton.setOnClickListener(this);
        mMusicPlayProgressBar.setProgressBarListener(this);
        mSongPath = getIntent().getStringExtra(SONG_PATH);
        mConnection = new MyConnection();
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra(SONG_PATH, mSongPath);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        MyThread thread = new MyThread();
        thread.start();
    }

    private class MyThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                if (mPlayService != null) {
                    mHandler.sendEmptyMessage(0x00);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void jumpPosToPlay(float pos) {
        mPlayService.setPosToPlay(pos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stoporplay:
                boolean flag = mPlayService.isPlaying();
                mPlayService.setPlay(!flag);
                break;
            default:
                break;
        }
    }

    private class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = (MusicPlayService.MyBinder) service;
            mPlayService.setSongUri(mSongPath);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
