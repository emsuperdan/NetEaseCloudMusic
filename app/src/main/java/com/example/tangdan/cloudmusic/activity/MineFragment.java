package com.example.tangdan.cloudmusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tangdan.cloudmusic.R;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mLocalMusicLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrollview_main_mine_layout, container, false);
        mLocalMusicLayout = (LinearLayout) view.findViewById(R.id.ll_localmusic);

        mLocalMusicLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_localmusic:
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), LocalMusicActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


}
