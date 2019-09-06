package com.example.tangdan.cloudmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;
import com.example.tangdan.cloudmusic.service.MusicPlayService;

import java.io.IOException;

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener, MusicPlayProgressBar.ProgressBarListener {
    private static final String SONG_PATH = "SONG_PATH";
    private static final String TAG = "MusicPlayActivity";

    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton;

    private MediaPlayer mMediaPlayer;
    private int mDuration;
    private String mSongPath;
    private MyConnection mConnection;
    private MusicPlayService.MyBinder mPlayService;



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0X00:
                    mMusicPlayProgressBar.setProgress((float) mPlayService.getPlayPos() / mMediaPlayer.getDuration());
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
        mMusicPlayProgressBar = (MusicPlayProgressBar) findViewById(R.id.progress_music_bar);
        mPlayButton = (Button) findViewById(R.id.btn_stoporplay);
        mPlayButton.setOnClickListener(this);
        mMusicPlayProgressBar.setProgressBarListener(this);
        mSongPath = getIntent().getStringExtra(SONG_PATH);
        mConnection = new MyConnection();
        Intent intent= new Intent(this, MusicPlayService.class);
        startService(intent);
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    @Override
    public void jumpPosToPlay(float pos) {
        int totalSec = mDuration;
        mMediaPlayer.seekTo((int) (totalSec * pos));
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

    private class MyConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = (MusicPlayService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
