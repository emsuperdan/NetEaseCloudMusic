package com.example.tangdan.cloudmusic.model;

public interface BaseModel {
    enum TitleState {
        MUSICMODEL,ITEMMODEL;
    }

    TitleState getModelType();
}
