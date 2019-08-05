package com.example.tangdan.cloudmusic.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.adapter.LocalMicAdapter;
import com.example.tangdan.cloudmusic.model.MusicModel;
import com.example.tangdan.cloudmusic.utils.LocalMusicScanUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends BaseActivity {
    private ListView mListView;
    private LocalMicAdapter mAdapter;
    private List<MusicModel> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaclmusic);

        initView();
        initData();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList = LocalMusicScanUtils.getMusicData(this);
        mAdapter = new LocalMicAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_localmusic_list);
    }


}
