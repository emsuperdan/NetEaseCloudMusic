package com.example.tangdan.cloudmusic.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.tangdan.cloudmusic.R;


/*
这是一个完全的自定义view，如果需要通过自定义view去实现音乐进度条，
需要去ondraw中绘制

如果是继承了一个Button或者什么seekbar，就基本不需要进行什么绘制了，直接通过这些控件
的什么setClick,setprogress等方法;
 */
public class MusicPlayProgressBar extends View {
    private ProgressBarListener listener;
    private Context mContext;
    Paint mPaint;
    Paint mThumbPaint;
    private float X;
    float mProgress;
    int progressWidth;
    int progressHeight;

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

    private void initParameters() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.grey));

        mThumbPaint = new Paint();
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setColor(mContext.getResources().getColor(R.color.white));
    }

    public void setProgressBarListener(ProgressBarListener listener) {
        this.listener = listener;
    }

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

        Log.d("TAGTAG","value:+"+getProgress());
        canvas.drawCircle((float) (7.5 + getProgress() * (progressWidth - 7.5)), progressHeight / 2,
                (float) 7.5, mThumbPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                if (moveX > 400) {
                    moveX = 400;
                }
                if (moveX < 0) {
                    moveX = 0;
                }
                float movePos = moveX / progressWidth;
                setProgress(movePos);
                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                if (newX > 400) {
                    newX = 400;
                }
                if (newX < 0) {
                    newX = 0;
                }
                float pos = newX / progressWidth;
                setProgress(pos);
                listener.jumpPosToPlay(pos);
                break;
        }
        return true;
    }

    public interface ProgressBarListener {
        void jumpPosToPlay(float pos);
    }

    public float getProgress() {
        return (float) mProgress;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}
