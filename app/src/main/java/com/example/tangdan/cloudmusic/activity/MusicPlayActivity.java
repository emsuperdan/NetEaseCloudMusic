package com.example.tangdan.cloudmusic.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;
import com.example.tangdan.cloudmusic.service.MusicPlayService;
import com.example.tangdan.cloudmusic.utils.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_KEY;

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener, MusicPlayProgressBar.ProgressBarListener {
    private static final String SONG_PATH = "SONG_PATH";
    private static final String TAG = "MusicPlayActivity";
    private static final String SP_SONG_PATH = "SP_SONG_PATH";

    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton, mLastButton, mNextButton;

    private String mSongPath;
    private ArrayList<String> mSongList;
    private PreferenceUtil mPreferenceUtil;
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
        mLastButton = (Button) findViewById(R.id.btn_nextsong);
        mNextButton = (Button) findViewById(R.id.btn_lastsong);
        mPlayButton.setOnClickListener(this);
        mLastButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mMusicPlayProgressBar.setProgressBarListener(this);
        mPreferenceUtil = PreferenceUtil.getInstance(this);
        mSongList = getSongListFromsp();
        mSongPath = getIntent().getStringExtra(SONG_PATH);
        mConnection = new MyConnection();
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.putExtra(SONG_PATH, mSongPath);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        MyThread thread = new MyThread();
        thread.start();
    }

    public ArrayList<String> getSongListFromsp() {
        ArrayList<String> list = new ArrayList<>();
        String json = mPreferenceUtil.getPreferenceString(PREF_PREFERENCE_KEY);
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.optString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
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
            case R.id.btn_lastsong:
                int pos = 0;
                if (mSongList.size() == 0 || mSongList.size() == 1) {
                    return;
                }
                for (int i = 0; i < mSongList.size(); i++) {
                    if (mSongList.get(i).equals(mSongPath)) {
                        if (i == 0) {
                            pos = mSongList.size() - 1;
                        } else {
                            pos = i - 1;
                        }
                        break;
                    }
                }
                mSongPath = mSongList.get(pos);
                mPlayService.playNextSong(mSongPath);
                break;
            case R.id.btn_nextsong:
                int pos1 = 0;
                if (mSongList.size() == 0 || mSongList.size() == 1) {
                    return;
                }
                for (int i = 0; i < mSongList.size(); i++) {
                    if (mSongList.get(i).equals(mSongPath)) {
                        if (i == mSongList.size() - 1) {
                            pos1 = 0;
                        } else {
                            pos1 = i + 1;
                        }
                        break;
                    }
                }
                mSongPath = mSongList.get(pos1);
                mPlayService.playNextSong(mSongPath);
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
