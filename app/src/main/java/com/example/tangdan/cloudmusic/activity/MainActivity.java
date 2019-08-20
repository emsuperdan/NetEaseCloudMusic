package com.example.tangdan.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.present.BasePresenter;

public class MainActivity extends BaseActivity implements View.OnClickListener {
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
}
