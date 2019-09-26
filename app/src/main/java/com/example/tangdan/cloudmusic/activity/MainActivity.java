package com.example.tangdan.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.present.BasePresenter;
import com.example.tangdan.cloudmusic.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION;
import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private BasePresenter presenter;
    private BaseFragment[] baseFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private TextView mSongText;
    private LinearLayout mBottomPlayingSong , ;
    private SongNameBroadCastReceiver mSongNameBroadCastReceiver;
    private List<View> mIndicatorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new BasePresenter(this);

        initView();
    }

    private void initView() {
        baseFragment = new BaseFragment[3];
        initFragment();
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.ll_mine_container, baseFragment[0], "MINEFRAGMENT");
        mTransaction.commit();

        mSongText = (TextView) findViewById(R.id.tv_song_name);
        mSongText.setText(PreferenceUtil.getInstance(this).getPreferenceString(PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY));
        mBottomPlayingSong = (LinearLayout) findViewById(R.id.ll_bottom_playing_song);
        mBottomPlayingSong.setOnClickListener(this);
        mIndicatorList = new ArrayList<>();
        int[] res = new int[]{R.id.view_find_indicator, R.id.view_mine_indicator, R.id.view_radio_indicator};
        for (int i = 0; i < 3; i++) {
            View view = findViewById(res[i]);
            mIndicatorList.add(view);
        }
        showIndicator(1);
        mSongNameBroadCastReceiver = new SongNameBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        registerReceiver(mSongNameBroadCastReceiver, filter);
    }

    private void initFragment() {
        baseFragment[0] = new MineFragment();
        baseFragment[1] = new FindFragment();
        baseFragment[2] = new RadioFragment();
    }

    private void showIndicator(int endPos) {
        for (int i = 0; i < mIndicatorList.size(); i++) {
            mIndicatorList.get(i).setVisibility(View.INVISIBLE);
        }
        mIndicatorList.get(endPos).setVisibility(View.VISIBLE);
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
            case R.id.ll_bottom_playing_song:
                Intent playMusicIntent = new Intent(this, MusicPlayActivity.class);
                startActivity(playMusicIntent);
                break;
            case R.id.ll_find_indicator:
                showIndicator(0);
                if (!mTransaction.isEmpty()) {
                    mTransaction.replace(R.id.ll_mine_container, baseFragment[0]).commit();
                }
                break;
            case R.id.ll_mine_indicator:
                showIndicator(1);
                if (!mTransaction.isEmpty()) {
                    mTransaction.replace(R.id.ll_mine_container, baseFragment[1]).commit();
                }
                break;
            case R.id.ll_radio_indicator:
                showIndicator(2);
                if (!mTransaction.isEmpty()) {
                    mTransaction.replace(R.id.ll_mine_container, baseFragment[2]).commit();
                }
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
