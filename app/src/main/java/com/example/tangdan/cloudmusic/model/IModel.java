package com.example.tangdan.cloudmusic.model;

import com.example.tangdan.cloudmusic.callback.ICallBack;

public interface IModel {
    void getResultInCallBack(final String input, final ICallBack<String> callBack);
}
