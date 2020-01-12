package com.example.tangdan.cloudmusic.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.tangdan.cloudmusic.R;

public class ViewActiity extends AppCompatActivity {
    LinearLayout linearLayout;
    CustomView customView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_custom);
        customView = findViewById(R.id.custom_view);
        customView.setWillNotDraw(false);
    }
}
