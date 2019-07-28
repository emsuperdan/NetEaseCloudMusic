package com.example.tangdan.cloudmusic;

public interface IView {
    void showResult(String data);

    void showError(String error);

    void showFailure(String failure);

    void showComplete();

    String getInput();


}
