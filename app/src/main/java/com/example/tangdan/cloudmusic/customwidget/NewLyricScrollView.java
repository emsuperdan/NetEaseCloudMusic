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
    private final int INTERVAL=80;//歌词每行的间隔
    Paint paint=new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL=new Paint();//画笔，用于画高亮的歌词，即当前唱到这句歌词
    private Map<Integer, LyricObject> mLyricMap = new HashMap<>();
    private int hlLineIndex = 0;

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
        paint.setTextSize(32);

        paintHL=new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);
        paintHL.setTextSize(35);

        setData(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mLyricMap.get(hlLineIndex).getLyric(), mCenterX, mOffsetY + INTERVAL * (hlLineIndex + 1), paintHL);

        for (int i = 0; i< hlLineIndex; i++){
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, mOffsetY+(INTERVAL)*(i+1), paint);
        }
        for (int i = hlLineIndex +1; i<mLyricMap.size(); i++){
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
        float speed = 1f;
        return speed;
    }

    public void setOffsetY(float offsetY){
        this.mOffsetY = offsetY;
    }

    public float getOffsetY(){
        return mOffsetY;
    }

    public void setSelectIndex(int currentTime){
        int index = 0;
        for (int i=0;i<mLyricMap.size();i++){
            LyricObject temp = mLyricMap.get(i);
            if (temp.getBeginTime()<currentTime/1000){
                hlLineIndex = index++;
            }
        }
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

        LyricObject object5 = new LyricObject();
        object5.setLyric("这是第一行歌词");
        object5.setBeginTime(26);
        object5.setEndTime(30);
        object5.setTiming(5);

        LyricObject object6 = new LyricObject();
        object6.setLyric("这是第一行歌词");
        object6.setBeginTime(31);
        object6.setEndTime(35);
        object6.setTiming(5);

        mLyricMap.put(0,object);
        mLyricMap.put(1,object1);
        mLyricMap.put(2,object2);
        mLyricMap.put(3,object3);
        mLyricMap.put(4,object4);
        mLyricMap.put(5,object5);
        mLyricMap.put(6,object6);
    }
}
