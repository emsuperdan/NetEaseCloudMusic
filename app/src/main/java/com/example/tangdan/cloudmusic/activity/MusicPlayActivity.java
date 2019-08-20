package com.example.tangdan.cloudmusic.activity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;
import com.example.tangdan.cloudmusic.utils.MediaSingleInstance;

import java.io.IOException;

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener {

    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton;
    private MyMusicAsyncTask mTask;

    private MediaPlayer mMediaPlayer;
    private MediaSingleInstance mMediaSingleInstance;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mMusicPlayProgressBar.setProgress(0);
                    break;
                case 1:
                    mMusicPlayProgressBar.setProgress(1);
                    break;
                case 2:
                    mMusicPlayProgressBar.setProgress(2);
                    break;
                case 3:
                    mMusicPlayProgressBar.setProgress(3);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mTask.isCancelled()) {
            mTask.cancel(true);
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

        try {
            mMediaPlayer.setDataSource("/storage/emulated/0/netease/cloudmusic/Music/Lucky Stroke - Good Day.mp3");
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTask = new MyMusicAsyncTask();
        mTask.execute();
        mHandler.sendEmptyMessage(1);
    }

    private class MyMusicAsyncTask extends AsyncTask<Integer, Integer, Float> {
        int currentPos;
        int duration;
        float progress;

        @Override
        protected Float doInBackground(Integer... integers) {
            duration = mMediaPlayer.getDuration();
            while (duration != -1 && mMediaPlayer.isPlaying()) {
                currentPos = mMediaPlayer.getCurrentPosition();
                Log.d("AAA", duration+"   "+mMediaPlayer.isPlaying()+"mMediaPlayer.getCurrentPosition()" + mMediaPlayer.getCurrentPosition());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);
            Log.d("AAA",(float)currentPos/duration+"currentPos");
            mMusicPlayProgressBar.setProgress((float) currentPos /duration);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stoporplay:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
                break;
            default:
                break;
        }
    }
}
