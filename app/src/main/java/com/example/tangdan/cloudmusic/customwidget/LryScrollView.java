package com.example.tangdan.cloudmusic.customwidget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.example.tangdan.cloudmusic.R;

public class LryScrollView extends ScrollView {
    private LyricProgressListener listener;
    private float mViewWidth, mViewHeight;
    private Paint mCenterLinePaint;
    private int centerLineOffset;
    private ValueAnimator mRotateAnimator;
    private boolean isMeasured;
    private float pos;

    public LryScrollView(Context context) {
        this(context, null);
    }

    public LryScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LryScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
    }

    private void initParams() {
        mCenterLinePaint = new Paint();
        mCenterLinePaint.setColor(getResources().getColor(R.color.black));
        mCenterLinePaint.setStrokeWidth(20);
    }

    public void setLyricProgressListener(LyricProgressListener listener){
        this.listener =  listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isMeasured) {
            return;
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        startScroll();
        isMeasured = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, (mViewHeight + 2 * centerLineOffset) / 2 - 123, mViewWidth - 50,
                (mViewHeight + 2 * centerLineOffset) / 2 - 117, mCenterLinePaint);
        canvas.drawRect(mViewWidth - 45, (mViewHeight + 2 * centerLineOffset) / 2 - 140, mViewWidth,
                (mViewHeight + 2 * centerLineOffset) / 2 - 100, mCenterLinePaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        centerLineOffset = t;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void startScroll() {
        mRotateAnimator = ValueAnimator.ofInt(0, (int) (mViewHeight / 2));
        mRotateAnimator.setDuration(5000);
        mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int valueY = (int) animation.getAnimatedValue();
                scrollTo(0, valueY);
            }
        });
        mRotateAnimator.start();
    }


    public void setLyricPos(float pos){
        this.pos = pos;
        scrollTo(0, (int) (mViewHeight * pos / 2));
    }

    public float getLyricPos(){
        return pos;
    }

    public interface LyricProgressListener{
        void jumpLyricPos(float pos);
    }
}
