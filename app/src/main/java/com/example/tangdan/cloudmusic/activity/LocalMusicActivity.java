package com.example.tangdan.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.adapter.LocalMicAdapter;
import com.example.tangdan.cloudmusic.model.MusicModel;
import com.example.tangdan.cloudmusic.utils.LocalMusicScanUtils;
import com.example.tangdan.cloudmusic.utils.PreferenceUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION;
import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_NAME_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_PATH_ISPLAYING_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_PATH_KEY;

public class LocalMusicActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView mListView;
    private LocalMicAdapter mAdapter;
    private List<MusicModel> mList;
    private TextView mSongName;
    private List<String> mSongpathList = new ArrayList<>();
    private List<String> mSongNameList = new ArrayList<>();
    private int[] mIndicatorViewIds = new int[]{R.id.view_localmusic_singlesong,
            R.id.view_localmusic_singer, R.id.view_localmusic_album, R.id.view_localmusic_file};
    private List<View> mIndicatorViewList = new ArrayList<>();
    private PreferenceUtil mPreferenceUtil;
    private SongNameBroadCastReceiver mSongNameBroadCastReceiver;
    private LinearLayout mBottomLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaclmusic);

        initView();
        initData();

        mListView.setOnItemClickListener(this);
        mBottomLinearLayout.setOnClickListener(this);
        mSongName.setText(PreferenceUtil.getInstance(this).getPreferenceString(PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY));
        mSongNameBroadCastReceiver = new SongNameBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        registerReceiver(mSongNameBroadCastReceiver, filter);
    }

    private void initData() {
        mList = new ArrayList<>();
        mList = LocalMusicScanUtils.getMusicData(this);
        mAdapter = new LocalMicAdapter(this, mList);
        mListView.setAdapter(mAdapter);

        storeSongpathTosp();
    }

    public void storeSongpathTosp() {
        mSongpathList.clear();
        mSongNameList.clear();
        for (int i = 0; i < mList.size(); i++) {
            mSongpathList.add(mList.get(i).getmUrl());
            mSongNameList.add(mList.get(i).getmTitle());
        }

        JSONArray array = new JSONArray(mSongpathList);
        mPreferenceUtil.putPreferenceString(PREF_PREFERENCE_SONG_PATH_KEY, array.toString());
        array = new JSONArray(mSongNameList);
        mPreferenceUtil.putPreferenceString(PREF_PREFERENCE_SONG_NAME_KEY, array.toString());
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_localmusic_list);
        mSongName = (TextView) findViewById(R.id.tv_song_name);
        mBottomLinearLayout = (LinearLayout) findViewById(R.id.ll_bottom_playing_song);
        View v;
        for (int i = 0; i < mIndicatorViewIds.length; i++) {
            v = findViewById(mIndicatorViewIds[i]);
            v.setVisibility(View.INVISIBLE);
            mIndicatorViewList.add(v);
        }
        mIndicatorViewList.get(0).setVisibility(View.VISIBLE);
        mPreferenceUtil = PreferenceUtil.getInstance(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicModel model = mList.get(position);
        if (model == null) {
            return;
        }
        String path = model.getmUrl();
        String name = model.getmTitle();

        Intent intent = new Intent(this, MusicPlayActivity.class);
        startActivity(intent);

        PreferenceUtil.getInstance(this).putPreferenceString(PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY, name);
        PreferenceUtil.getInstance(this).putPreferenceString(PREF_PREFERENCE_SONG_PATH_ISPLAYING_KEY, path);

        mSongName.setText(model.getmTitle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                mSongName.setText(intent.getStringExtra(BROADCAST_ACTION_KEY));
            }
        }
    }
}
