package com.example.tangdan.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.present.BasePresenter;

import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION;
import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView mSongText;
    private BasePresenter presenter;
    private LinearLayout mLocalMusicLayout;
    private ImageView mAlbumImage;
    private LinearLayout mBottomPlayingSong;
    private SongNameBroadCastReceiver mSongNameBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new BasePresenter(this);

        initView();
        mLocalMusicLayout.setOnClickListener(this);
        mAlbumImage.setOnClickListener(this);
        mBottomPlayingSong.setOnClickListener(this);

        mSongNameBroadCastReceiver = new SongNameBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        registerReceiver(mSongNameBroadCastReceiver, filter);
    }

    private void initView() {
        mLocalMusicLayout = (LinearLayout) findViewById(R.id.ll_localmusic);
        mAlbumImage = (ImageView) findViewById(R.id.iv_albumimage);
        mBottomPlayingSong = (LinearLayout) findViewById(R.id.ll_bottom_playing_song);
        mSongText = (TextView) findViewById(R.id.tv_song_name);
    }

    @Override
    public void showResult(String data) {
        super.showResult(data);
        if (presenter.isViewAttached()) {
            presenter.loadData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_localmusic:
                Intent intent = new Intent(this, LocalMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_bottom_playing_song:
                Intent playMusicIntent = new Intent(this, MusicPlayActivity.class);
                startActivity(playMusicIntent);
                break;
            default:
                break;
        }
    }

    public class SongNameBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getAction().equals(BROADCAST_ACTION)) {
                String name = intent.getStringExtra(BROADCAST_ACTION_KEY);
                mSongText.setText(name);
            }
        }
    }
}
