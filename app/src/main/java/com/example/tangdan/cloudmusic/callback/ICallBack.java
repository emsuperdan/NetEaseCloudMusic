package com.example.tangdan.cloudmusic.callback;

public interface ICallBack<T> {
    void onSuccess(T data);

    void onFailure(T failure);

    void onError(T error);

    void onComplete();
}
