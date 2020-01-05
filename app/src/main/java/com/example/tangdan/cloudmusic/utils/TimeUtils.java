package com.example.tangdan.cloudmusic.utils;

public class TimeUtils {
    public static String secToTime(int time) {
        String timeStr = null;
        time /= 1000;
        int minute = time / 60 % 60;
        int second = time % 60;
        timeStr = unitFormat(minute) + ":" + unitFormat(second);
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
