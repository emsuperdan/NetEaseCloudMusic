package com.example.tangdan.cloudmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.activity.BaseActivity;
import com.example.tangdan.cloudmusic.activity.LocalMusicActivity;
import com.example.tangdan.cloudmusic.present.BasePresenter;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private BasePresenter presenter;
    private LinearLayout mLocalMusicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new BasePresenter(this);

        initView();
        mLocalMusicLayout.setOnClickListener(this);
    }

    private void initView() {
        mLocalMusicLayout = (LinearLayout) findViewById(R.id.ll_localmusic);
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
            default:
                break;
        }
    }
}
