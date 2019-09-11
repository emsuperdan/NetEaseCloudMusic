package com.example.tangdan.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.present.BasePresenter;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView mSongName;
    private BasePresenter presenter;
    private LinearLayout mLocalMusicLayout;
    private ImageView mAlbumImage;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new BasePresenter(this);

        initView();
        mLocalMusicLayout.setOnClickListener(this);
        mAlbumImage.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);
    }

    private void initView() {
        mLocalMusicLayout = (LinearLayout) findViewById(R.id.ll_localmusic);
        mAlbumImage = (ImageView) findViewById(R.id.iv_albumimage);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_titleandlyric);
        mSongName = (TextView) findViewById(R.id.tv_song_name);
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
                startActivityForResult(intent,CODE_SELECT_SONG);
                break;
            case R.id.iv_albumimage:
                Intent playMusicIntent = new Intent(this, MusicPlayActivity.class);
                startActivity(playMusicIntent);
                break;
            case R.id.ll_titleandlyric:
                Intent playMusicIntent1 = new Intent(this, MusicPlayActivity.class);
                startActivity(playMusicIntent1);
                break;
            default:
                break;
        }
    }

    public class SongNameBroadCastReceiver extends BroadcastReceiver {

        private static final String BROADCAST_ACTION = "broadcast_action_song_path";
        private static final String BROADCAST_ACTION_KEY = "broadcast_action_song_path_key";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(BROADCAST_ACTION)) {
                mSongName.setText(intent.getStringExtra(BROADCAST_ACTION_KEY));
            }
        }
    }

    private static final int CODE_SELECT_SONG = 0x00;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SELECT_SONG:
                break;
        }
    }
}
