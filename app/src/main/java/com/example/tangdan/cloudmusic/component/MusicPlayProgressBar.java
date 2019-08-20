package com.example.tangdan.cloudmusic.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.tangdan.cloudmusic.R;


/*
这是一个完全的自定义view，如果需要通过自定义view去实现音乐进度条，
需要去ondraw中绘制

如果是继承了一个Button或者什么seekbar，就基本不需要进行什么绘制了，直接通过这些控件
的什么setClick,setprogress等方法;
 */
public class MusicPlayProgressBar extends View {
    private Context mContext;
    Paint mPaint;
    Paint mThumbPaint;
    float mProgress;

    public MusicPlayProgressBar(Context context) {
        this(context, null);
    }

    public MusicPlayProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicPlayProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initParameters();
    }

    private int getScreenWidth() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenHeight() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void initParameters() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.grey));

        mThumbPaint = new Paint();
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setColor(mContext.getResources().getColor(R.color.white));
    }

    int progressWidth;
    int progressHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        progressWidth = MeasureSpec.getSize(widthMeasureSpec);
        progressHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(progressWidth, progressHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, (float) (progressHeight / 2 - 3.5), progressWidth,
                (float) (progressHeight / 2 + 3.5), mPaint);

        Log.d("AAA","(float) (7.5 + getProgress() * 20"+(float) (7.5 + getProgress() * 200));
        canvas.drawCircle((float) (7.5 + getProgress() * 200), progressHeight / 2,
                (float) 7.5, mThumbPaint);
    }

    private boolean flag = false;
    private float X;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                X=event.getX();
                break;
        }
        return true;
    }

    public float getProgress() {
        return (float) mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}
