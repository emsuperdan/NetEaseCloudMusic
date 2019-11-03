package com.example.tangdan.cloudmusic.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.adapter.MyViewPagerAdapter;
import com.example.tangdan.cloudmusic.component.FragmentIndicatorView;
import com.example.tangdan.cloudmusic.present.BasePresenter;
import com.example.tangdan.cloudmusic.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION;
import static com.example.tangdan.cloudmusic.utils.Constants.BROADCAST_ACTION_KEY;
import static com.example.tangdan.cloudmusic.utils.Constants.PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private BasePresenter presenter;
    private BaseFragment[] baseFragment;
    private FragmentManager mFragmentManager;
    private TextView mSongText;
    private LinearLayout mBottomPlayingSong, mMineLinearLayout, mFindLinearLayout, mRadioLinearLayout;
    private SongNameBroadCastReceiver mSongNameBroadCastReceiver;
    private ViewPager mViewPager;
    private MyViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mFragmentList;
    private FragmentIndicatorView mFragmentIndicatorView;
    private int mCurrentPos;

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

        mSongText = (TextView) findViewById(R.id.tv_song_name);
        mSongText.setText(PreferenceUtil.getInstance(this).getPreferenceString(PREF_PREFERENCE_SONG_NAME_ISPLAYING_KEY));
        mBottomPlayingSong = (LinearLayout) findViewById(R.id.ll_bottom_playing_song);
        mFindLinearLayout = (LinearLayout) findViewById(R.id.ll_find_indicator);
        mMineLinearLayout = (LinearLayout) findViewById(R.id.ll_mine_indicator);
        mRadioLinearLayout = (LinearLayout) findViewById(R.id.ll_radio_indicator);
        mFragmentIndicatorView = (FragmentIndicatorView) findViewById(R.id.fragmentindicator_view);
        mViewPager = (ViewPager) findViewById(R.id.fragment_viewpager);
        mViewPagerAdapter = new MyViewPagerAdapter(mFragmentManager, mFragmentList);
        mViewPager.setAdapter(mViewPagerAdapter);
        switchFragment(1);
        mCurrentPos = 1;

        mViewPager.addOnPageChangeListener(this);
        mFindLinearLayout.setOnClickListener(this);
        mMineLinearLayout.setOnClickListener(this);
        mRadioLinearLayout.setOnClickListener(this);
        mBottomPlayingSong.setOnClickListener(this);
        mSongNameBroadCastReceiver = new SongNameBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        registerReceiver(mSongNameBroadCastReceiver, filter);
    }

    private void initFragment() {
        baseFragment[0] = new FindFragment();
        baseFragment[1] = new MineFragment();
        baseFragment[2] = new RadioFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(baseFragment[0]);
        mFragmentList.add(baseFragment[1]);
        mFragmentList.add(baseFragment[2]);
    }

    @Override
    public void showResult(String data) {
        super.showResult(data);
        if (presenter.isViewAttached()) {
            presenter.loadData();
        }
    }

    public void switchFragment(int pos) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mViewPager.setCurrentItem(pos);
        mCurrentPos = pos;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bottom_playing_song:
                Intent playMusicIntent = new Intent(this, MusicPlayActivity.class);
                startActivity(playMusicIntent);
                break;
            case R.id.ll_find_indicator:
                switchFragment(0);
                break;
            case R.id.ll_mine_indicator:
                switchFragment(1);
                break;
            case R.id.ll_radio_indicator:
                switchFragment(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Log.d("TAGTAG","i:  "+i+"-------"+"v:  "+v+"------"+"i1:  "+i1);

        mFragmentIndicatorView.setIndicatorPos(i,mCurrentPos,v);
        mCurrentPos = i;
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Log.d("TAGTAG","i    "+i);
//        int pos = mViewPager.getCurrentItem();
//        switch (pos) {
//            case 0:
//                switchFragment(0);
//                mFragmentIndicatorView.setIndicatorPos(0, mCurrentPos);
//                break;
//            case 1:
//                switchFragment(1);
//                mFragmentIndicatorView.setIndicatorPos(1, 1);
//                break;
//            case 2:
//                switchFragment(2);
//                mFragmentIndicatorView.setIndicatorPos(2, 1);
//                break;
//            default:
//                break;
//        }
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
