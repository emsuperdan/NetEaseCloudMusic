package com.example.tangdan.cloudmusic.present;

import com.example.tangdan.cloudmusic.BaseActivity;
import com.example.tangdan.cloudmusic.callback.ICallBack;
import com.example.tangdan.cloudmusic.model.BaseModel;

public class BasePresenter implements IPresenter {
    private BaseModel model;
    private BaseActivity view;

    public BasePresenter(BaseActivity view) {
        this.view = view;
        model = new BaseModel();
    }

    @Override
    public void loadData() {
        model.getResultInCallBack(view.getInput(), new ICallBack<String>() {
            @Override
            public void onSuccess(String data) {
                view.showResult(data);
            }

            @Override
            public void onFailure(String failure) {
                view.showFailure(failure);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }

            @Override
            public void onComplete() {
            }
        });
        view.showComplete();
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public void dettachView() {
        view = null;
    }
}