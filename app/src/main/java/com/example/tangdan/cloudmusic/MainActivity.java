package com.example.tangdan.cloudmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.tangdan.cloudmusic.present.BasePresenter;

public class MainActivity extends BaseActivity{
    private BasePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter=new BasePresenter(this);
    }

    @Override
    public void showResult(String data) {
        super.showResult(data);
        if (presenter.isViewAttached()){
            presenter.loadData();
        }
    }

}
