package com.example.tangdan.cloudmusic.activity;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.component.MusicPlayProgressBar;
import com.example.tangdan.cloudmusic.customwidget.NewLyricScrollView;
import com.example.tangdan.cloudmusic.model.LyricObject;
import com.example.tangdan.cloudmusic.model.MusicModel;
import com.example.tangdan.cloudmusic.service.MusicPlayService;
import com.example.tangdan.cloudmusic.utils.JsonUtils;
import com.example.tangdan.cloudmusic.utils.PreferenceUtil;
import com.example.tangdan.cloudmusic.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION;
import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_NAME_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_PATH_ISPLAYING_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_PATH_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.live_mic_url0;
import static com.example.tangdan.cloudmusic.utils.Constants.live_mic_url1;

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener, MusicPlayProgressBar.ProgressBarListener {
    private static final String SONG_PATH = "SONG_PATH";

    private MusicPlayProgressBar mMusicPlayProgressBar;
    private Button mPlayButton, mLastButton, mNextButton;
    private TextView mCurPlayTime, mTotalPlayTime;
    private NewLyricScrollView mAlbumImage;

    private MusicModel mModel;
    private Bundle mBundle;
    private String mSongPath;
    private String mSongName;
    private ArrayList<String> mSongPathList;
    private ArrayList<String> mSongNameList;
    private PreferenceUtil mPreferenceUtil;
    private MyConnection mConnection;
    private MusicPlayService.MyBinder mPlayService;
    private Intent songIntent;
    private ValueAnimator mRotateAnimator;
    private int indexTime = 0;
    private HashMap<Integer, LyricObject> lyricMap = new HashMap<>();
    private boolean isLyricReady;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Handler mUiHandler;

    private Runnable mPlayProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayService != null && mPlayService.getPlayDuration() != 0) {
                mMusicPlayProgressBar.setProgress((float) mPlayService.getPlayPos() / mPlayService.getPlayDuration());
                mCurPlayTime.setText(TimeUtils.secToTime(mPlayService.getPlayPos()));
                mTotalPlayTime.setText(TimeUtils.secToTime(mPlayService.getPlayDuration()));
            }
        }
    };

    private Runnable mLyricRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayService != null && mPlayService.getPlayDuration() != 0) {
                mAlbumImage.setSelectIndex(indexTime);
                mAlbumImage.setOffsetY(mAlbumImage.getOffsetY() - mAlbumImage.getLyricSpeed());
                mAlbumImage.invalidate();
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
        mUiHandler = new Handler(Looper.getMainLooper());
        mHandlerThread = new HandlerThread(MusicPlayActivity.class.getSimpleName());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mMusicPlayProgressBar = (MusicPlayProgressBar) findViewById(R.id.progress_music_bar);
        mPlayButton = (Button) findViewById(R.id.btn_stoporplay);
        mLastButton = (Button) findViewById(R.id.btn_nextsong);
        mNextButton = (Button) findViewById(R.id.btn_lastsong);
        mAlbumImage = (NewLyricScrollView) findViewById(R.id.iv_albumimage);
        mCurPlayTime = findViewById(R.id.tv_play_curtime);
        mTotalPlayTime = findViewById(R.id.tv_play_totaltime);

        mPlayButton.setOnClickListener(this);
        mLastButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mMusicPlayProgressBar.setProgressBarListener(this);
        mPreferenceUtil = PreferenceUtil.getInstance(this);
        songIntent = new Intent();
        songIntent.setAction(BROADCAST_ACTION);
        mConnection = new MyConnection();

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mModel = (MusicModel) mBundle.getSerializable("musicmodel");
            mSongName = TextUtils.isEmpty(mModel.getmTitle()) ? "哈哈哈为空" : mModel.getmTitle();
            okhttpGetWithLivemic();
//            if (!mModel.getAlbumPicUrl().equals("")) {
//                Glide.with(this).load(album_pic_url0 + mModel.getAlbumPicUrl() + album_pic_url1).into(mAlbumImage);
//            }
        } else {
            mSongPathList = getSongPathListFromSp();
            mSongNameList = getSongNameListFromSp();
            mSongName = mPreferenceUtil.getPreferenceString(PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY);
            mSongPath = mPreferenceUtil.getPreferenceString(PREF_PREFERENCE_SONG_PATH_ISPLAYING_KEY);
            Intent intent = new Intent(this, MusicPlayService.class);
            intent.putExtra(SONG_PATH, mSongPath);
            startService(intent);
            bindService(intent, mConnection, BIND_AUTO_CREATE);
            MyPlayProgressThread thread = new MyPlayProgressThread();
            thread.start();

            MyLyricThread thread1 = new MyLyricThread();
            thread1.start();
        }
    }

    private MusicModel okhttpGetWithLivemic() {
        if (mModel == null && mModel.getmSongId().equals("")) {
            return mModel;
        }
        //需要及时更新
        final Request request = new Request.Builder().url(live_mic_url0 + mModel.getmSongId() + live_mic_url1).get().build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAGTAG", "error" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    byte[] B = new byte[4096];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int length;
                    while ((length = inputStream.read(B)) > 0) {
                        baos.write(B, 0, length);
                    }

                    String url = JsonUtils.parsePlayUrl(baos.toString());
                    mModel.setmLiveMicUrl(url);
                    mSongPath = url;
                    Intent intent = new Intent(MusicPlayActivity.this, MusicPlayService.class);
                    intent.putExtra(SONG_PATH, mSongPath);
                    startService(intent);
                    bindService(intent, mConnection, BIND_AUTO_CREATE);
                    MyPlayProgressThread thread = new MyPlayProgressThread();
                    thread.start();

                    MyLyricThread thread1 = new MyLyricThread();
                    thread1.start();
                }
            }
        });
        return mModel;
    }

    public ArrayList<String> getSongPathListFromSp() {
        ArrayList<String> list = new ArrayList<>();
        String json = mPreferenceUtil.getPreferenceString(PREF_PREFERENCE_SONG_PATH_KEY);
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

    public ArrayList<String> getSongNameListFromSp() {
        ArrayList<String> list = new ArrayList<>();
        String json = mPreferenceUtil.getPreferenceString(PREF_PREFERENCE_SONG_NAME_KEY);
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

    private class MyPlayProgressThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                if (mPlayService != null) {
                    mUiHandler.post(mPlayProgressRunnable);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class MyLyricThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                mUiHandler.post(mLyricRunnable);
                try {
                    Thread.sleep(100);
                    indexTime+=100;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void jumpPosToPlay(float pos) {
        mPlayService.setPosToPlay(pos);
        mUiHandler.post(mPlayProgressRunnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stoporplay:
                boolean flag = mPlayService.isPlaying();
                mPlayService.setPlay(!flag);

//                if (!flag){
//                    mRotateAnimator.resume();
//                }else {
//                    if (mRotateAnimator!=null){
//                        mRotateAnimator.pause();
//                    }
//                }

                break;
            case R.id.btn_lastsong:
                int pos = 0;
                if (mSongPathList.size() == 0 || mSongPathList.size() == 1) {
                    return;
                }
                for (int i = 0; i < mSongPathList.size(); i++) {
                    if (mSongPathList.get(i).equals(mSongPath)) {
                        if (i == 0) {
                            pos = mSongPathList.size() - 1;
                        } else {
                            pos = i - 1;
                        }
                        break;
                    }
                }
                mSongPath = mSongPathList.get(pos);
                mSongName = mSongNameList.get(pos);
                mPlayService.playNextSong(mSongPath);
                break;
            case R.id.btn_nextsong:
                int pos1 = 0;
                if (mSongPathList.size() == 0 || mSongPathList.size() == 1) {
                    return;
                }
                for (int i = 0; i < mSongPathList.size(); i++) {
                    if (mSongPathList.get(i).equals(mSongPath)) {
                        if (i != mSongPathList.size() - 1) {
                            pos1 = i + 1;
                        }
                        break;
                    }
                }
                mSongPath = mSongPathList.get(pos1);
                mSongName = mSongNameList.get(pos1);
                mPlayService.playNextSong(mSongPath);
                break;
            default:
                break;
        }
        songIntent.putExtra(BROADCAST_ACTION_KEY, mSongName);
        sendBroadcast(songIntent);

        PreferenceUtil.getInstance(this).putPreferenceString(PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY, mSongName);
        PreferenceUtil.getInstance(this).putPreferenceString(PREF_PREFERENCE_SONG_PATH_ISPLAYING_KEY, mSongPath);
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

    class LyricLoadTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String data = "";
            try {
                File file = new File("lyric.txt");
                if (!file.isFile()) {
                    return null;
                }

                FileInputStream stream = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(stream,"GB2312"));
                Pattern pattern = Pattern.compile("\\d{2}");

                while ((data = br.readLine()) != null) {
                    Log.d("TAGTAG","--------"+data+"/n");
                    data = data.replace("[","");
                    data = data.replace("]","@");
                    String splitdata[] = data.split("@");
                    //歌词中不算时间的部分
                    if (data.endsWith("@")){
                        for (int i = 0;i<splitdata.length;i++){
                            String str = splitdata[i];
                            str = str.replace()
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isLyricReady = true;
        }
    }
}
