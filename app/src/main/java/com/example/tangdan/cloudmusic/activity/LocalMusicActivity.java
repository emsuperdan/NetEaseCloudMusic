package com.example.tangdan.cloudmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.adapter.LocalMicAdapter;
import com.example.tangdan.cloudmusic.model.MusicModel;
import com.example.tangdan.cloudmusic.utils.LocalMusicScanUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String BROADCAST_ACTION = "broadcast_action_song_path";
    private static final String BROADCAST_ACTION_KEY = "broadcast_action_song_path_key";
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

        mListView.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicModel model = mList.get(position);
        String path= model.getmUrl();
        Log.d("TAGTAG","     "+path);

        Intent intent = new Intent(this,MusicPlayActivity.class);
        intent.putExtra(SONG_PATH,path);
        startActivity(intent);
    }
    private static final String SONG_PATH = "SONG_PATH";
}
