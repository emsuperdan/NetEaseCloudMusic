package com.example.tangdan.cloudmusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.tangdan.cloudmusic.IView;

public class BaseActivity extends AppCompatActivity implements IView {
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }

    @Override
    public void showResult(String data) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showFailure(String failure) {

    }

    @Override
    public void showComplete() {

    }

    @Override
    public String getInput() {
        return null;
    }
}
