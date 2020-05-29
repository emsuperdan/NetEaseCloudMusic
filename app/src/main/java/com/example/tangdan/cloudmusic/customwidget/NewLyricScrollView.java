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

import java.util.HashMap;
import java.util.Map;

public class NewLyricScrollView extends View {
    private float mCenterX;//歌词中间点 固定值
    private float lyricHeight;
    private float mOffsetY;//用作歌词滚动偏移;
    private float Y = 0;//计算跟手移动Y距离
    private final int INTERVAL=45;//歌词每行的间隔
    Paint paint=new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL=new Paint();//画笔，用于画高亮的歌词，即当前唱到这句歌词
    private Map<Integer, LyricObject> mLyricMap = new HashMap<>();
    private int hlLineNumber = 2;

    public NewLyricScrollView(Context context) {
        super(context);
        init();
    }

    public NewLyricScrollView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    private void init() {
        mOffsetY = 200;
        setBackgroundColor(Color.YELLOW);

        paint=new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(180);

        paintHL=new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);

        setData(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mLyricMap.get(hlLineNumber).getLyric(), mCenterX, mOffsetY + INTERVAL * (hlLineNumber + 1), paintHL);

        for (int i = 0;i<hlLineNumber;i++){
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, mOffsetY+(INTERVAL)*(i+1), paint);
        }
        for (int i = hlLineNumber+1;i<mLyricMap.size();i++){
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, mOffsetY+(INTERVAL)*(i+1), paint);
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
                Log.d("TAGTAG","手落下的位置Y："+Y);
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetY = event.getRawY() - Y;
                Log.d("TAGTAG","手move到的位置Y："+event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                Y = event.getRawY();
                Log.d("TAGTAG","手抬起的位置Y："+event.getRawY());
                break;
        }
        return true;
    }

    public float getLyricSpeed(){
        float speed = 0.5f;
        return speed;
    }

    public void setOffsetY(float offsetY){
        this.mOffsetY = offsetY;
    }

    public float getOffsetY(){
        return mOffsetY;
    }

    public void setData(Map<Integer, LyricObject> map){
        LyricObject object = new LyricObject();
        object.setLyric("这是第一行歌词");
        object.setBeginTime(0);
        object.setEndTime(5);
        object.setTiming(5);

        LyricObject object1 = new LyricObject();
        object1.setLyric("这是第一行歌词");
        object1.setBeginTime(6);
        object1.setEndTime(10);
        object1.setTiming(5);

        LyricObject object2 = new LyricObject();
        object2.setLyric("这是第一行歌词");
        object2.setBeginTime(11);
        object2.setEndTime(15);
        object2.setTiming(5);

        LyricObject object3 = new LyricObject();
        object3.setLyric("这是第一行歌词");
        object3.setBeginTime(16);
        object3.setEndTime(20);
        object3.setTiming(5);

        LyricObject object4 = new LyricObject();
        object4.setLyric("这是第一行歌词");
        object4.setBeginTime(21);
        object4.setEndTime(25);
        object4.setTiming(5);

        mLyricMap.put(0,object);
        mLyricMap.put(1,object1);
        mLyricMap.put(2,object2);
        mLyricMap.put(3,object3);
        mLyricMap.put(4,object4);
    }
}
