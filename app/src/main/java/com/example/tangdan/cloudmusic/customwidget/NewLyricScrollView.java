package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.tangdan.cloudmusic.model.LyricObject;

import java.util.ArrayList;

public class NewLyricScrollView extends View {
    private float mCenterX;//歌词中间点 固定值
    private float lyricHeight;
    private float mOffsetY;//用作歌词滚动偏移;
    private float Y = 0;//计算跟手移动Y距离
    private final int INTERVAL = 80;//歌词每行的间隔
    Paint paint = new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL = new Paint();//画笔，用于画高亮的歌词，即当前唱到这句歌词
    private ArrayList<LyricObject> mLyricMap = new ArrayList<>();
    private int hlLineIndex = 0;
    private boolean isLyricReady;

    public NewLyricScrollView(Context context) {
        super(context);
        init();
    }

    public NewLyricScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOffsetY = 600;
        setBackgroundColor(Color.YELLOW);

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(180);
        paint.setTextSize(32);

        paintHL = new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);
        paintHL.setTextSize(35);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isLyricReady) {
            return;
        }
        canvas.drawText(mLyricMap.get(hlLineIndex).getLyric(), mCenterX, mOffsetY + INTERVAL * (hlLineIndex + 1), paintHL);

        for (int i = 0; i < hlLineIndex; i++) {
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, mOffsetY + (INTERVAL) * (i + 1), paint);
        }
        for (int i = hlLineIndex + 1; i < mLyricMap.size(); i++) {
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, mOffsetY + (INTERVAL) * (i + 1), paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = 0.5f * w;
        lyricHeight = 1f * h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Y = event.getRawY();
                Log.d("TAGTAG", "手落下的位置Y：" + Y);
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetY = event.getRawY() - Y;
                Log.d("TAGTAG", "手move到的位置Y：" + event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                Y = event.getRawY();
                Log.d("TAGTAG", "手抬起的位置Y：" + event.getRawY());
                break;
        }
        return true;
    }

    public float getLyricSpeed() {
        float speed = 1f;
        return speed;
    }

    public void setOffsetY(float offsetY) {
        this.mOffsetY = offsetY;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public void setSelectIndex(int currentTime) {
        int index = 0;
        for (int i = 0; i < mLyricMap.size(); i++) {
            LyricObject temp = mLyricMap.get(i);
            if (temp.getBeginTime() < currentTime) {
                hlLineIndex = index++;
            }
        }
    }

    public void setData(ArrayList<LyricObject> map, boolean isLyricReady) {
        this.isLyricReady = isLyricReady;
        mLyricMap = map;
    }
}
