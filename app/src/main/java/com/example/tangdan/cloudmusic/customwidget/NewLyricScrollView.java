package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.example.tangdan.cloudmusic.model.LyricObject;

import java.util.ArrayList;

public class NewLyricScrollView extends View {
    private float mCenterX;//歌词中间点 固定值
    private float lyricHeight;
    private float mOffsetY;//用作歌词滚动偏移;(相对于canvas来说)
    private float mScrollY;//记录控件scroll偏移;()
    private float Y = 0;//计算跟手移动Y距离
    private final int INTERVAL = 80;//歌词每行的间隔
    Paint paint = new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL = new Paint();//画笔，用于画高亮的歌词，即当前唱到这句歌词
    private ArrayList<LyricObject> mLyricMap = new ArrayList<>();
    private int hlLineIndex = 0;
    private boolean isLyricReady;
    private boolean isReachTop = true, isReachBottom;

    private Scroller mScroller;
    private GestureDetector mGestureDetector;
    private boolean isFling;

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
        paint.setTextSize(42);

        paintHL = new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);
        paintHL.setTextSize(45);

        mScroller = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
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
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return mGestureDetector.onTouchEvent(event);
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

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            mScrollY = mScroller.getCurrY();
            postInvalidate();
        }
    }

    public void smoothScrollYTo(float distance, float velocityY) {
        int scrollY = getScrollY();
        int elementA = 0;
        int elementB = 0;
        float absVelocity = Math.abs(velocityY);
        float absDistance = Math.abs(distance);
        int duration = 800;
        if (absVelocity <= 2000) {
            elementA = 1;
        } else if (absVelocity > 2000 && 3000 > absVelocity) {
            elementA = 2;
        } else if (absVelocity > 3000 && 5000 > absVelocity) {
            elementA = 3;
        } else {
            elementA = 4;
        }

        if (absDistance <= 200) {
            elementB = 1;
        } else if (absDistance > 200 && 300 > absDistance) {
            elementB = 2;
        } else if (absDistance > 300 && 500 > absDistance) {
            elementB = 3;
            duration *= 1.2;
        } else {
            elementB = 4;
            duration *= 1.5;
        }

        if (velocityY > 0 && !isReachTop) {
            mScroller.startScroll(0, scrollY, 0, -50 * elementA * elementB, duration);
            mOffsetY += 50 * elementA * elementB;
        } else if (velocityY < 0 && !isReachBottom){
            mScroller.startScroll(0, scrollY, 0, 50 * elementA * elementB, duration);
            mOffsetY -= 50 * elementA * elementB;
        }
        invalidate();
    }

    public void smoothScrollYToEdge(float velocityY) {
        int scrollY = getScrollY();
        int deltaY = -(int) mScrollY;
        if (velocityY < 0) {
            deltaY = 2000;
        }
        mScroller.startScroll(0, scrollY, 0, deltaY, 500);
        if (velocityY > 0) {
            mOffsetY = 600;
        }
        invalidate();
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            if (isFling) {
                mScroller.forceFinished(true);
                isFling = false;
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("TAGTAG", "mOffsetY" + mOffsetY);
            if (mOffsetY >= 600) {
                isReachTop = true;
                return super.onScroll(e1, e2, distanceX, distanceY);
            } else {
                isReachTop = false;
            }
            mOffsetY -= distanceY;
            invalidate();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("TAGTAG", "y速度:" + velocityY + "  e1 Y:" + e1.getY() + "  e2 Y:" + e2.getY());
            if (Math.abs(e1.getY() - e2.getY()) > 400 && Math.abs(velocityY) > 4000) {
                Log.d("TAGTAG", "scroll to edge");
                smoothScrollYToEdge(velocityY);
            } else {
                smoothScrollYTo(e1.getY() - e2.getY(), velocityY);
            }
            isFling = true;
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };
}
