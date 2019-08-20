package com.example.tangdan.cloudmusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private int[] mIndicatorViewIds = new int[]{R.id.view_localmusic_singlesong,
            R.id.view_localmusic_singer, R.id.view_localmusic_album, R.id.view_localmusic_file};
    private List<View> mIndicatorViewList = new ArrayList<>();

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
        View v;
        for (int i = 0; i < mIndicatorViewIds.length; i++) {
            v = findViewById(mIndicatorViewIds[i]);
            v.setVisibility(View.INVISIBLE);
            mIndicatorViewList.add(v);
        }
        mIndicatorViewList.get(0).setVisibility(View.VISIBLE);
    }

}
