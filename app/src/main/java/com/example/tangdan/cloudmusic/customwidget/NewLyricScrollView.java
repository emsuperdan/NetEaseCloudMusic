package com.example.tangdan.cloudmusic.customwidget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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
    private final int INTERVAL = 80;//歌词每行的间隔
    Paint paint = new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL = new Paint();//画笔，用于画高亮的歌词，即当前唱到这句歌词
    Paint paintDash = new Paint();
    private float mLyricTotalHeight;
    private ArrayList<LyricObject> mLyricMap = new ArrayList<>();
    private int hlLineIndex = 0;
    private boolean isLyricReady;
    private boolean isReachTop = true, isReachBottom;
    private boolean mAllowOffset = true;
    private LyricOnClickListener listener;
    private ValueAnimator mRollbackAnimator;

    private Scroller mScroller;
    private GestureDetector mGestureDetector;
    private boolean isFling;
    private boolean isScrolled;

    public NewLyricScrollView(Context context) {
        super(context);
        init();
    }

    public NewLyricScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOffsetY = -600;
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

        paintDash.setAntiAlias(true);
        paintDash.setStyle(Paint.Style.STROKE);
        paintDash.setStrokeWidth(4);
        paintDash.setColor(Color.BLACK);
        paintDash.setPathEffect(new DashPathEffect(new float[]{4,4},0));

        mScroller = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
    }

    private void createRollbackAnimator(float startY, float endY) {
        cancelRollbackAnimator();
        mRollbackAnimator = ValueAnimator.ofFloat(startY, endY);
        mRollbackAnimator.setStartDelay(5000);
        mRollbackAnimator.setDuration(500);
        mRollbackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mRollbackAnimator.start();
    }

    private void cancelRollbackAnimator() {
        if (mRollbackAnimator != null) {
            mRollbackAnimator.cancel();
            mRollbackAnimator = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isLyricReady) {
            return;
        }
        canvas.drawText(mLyricMap.get(hlLineIndex).getLyric(), mCenterX, INTERVAL * (hlLineIndex + 1), paintHL);

        for (int i = 0; i < hlLineIndex; i++) {
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, (INTERVAL) * (i + 1), paint);
        }
        for (int i = hlLineIndex + 1; i < mLyricMap.size(); i++) {
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, (INTERVAL) * (i + 1), paint);
        }
        canvas.drawLine(0, lyricHeight/2+mOffsetY, mCenterX * 2, lyricHeight/2+mOffsetY, paintDash);
    }

    public void setLyricOnClickListener(LyricOnClickListener listener) {
        this.listener = listener;
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
                mAllowOffset = false;
                break;
            case MotionEvent.ACTION_UP:
                mAllowOffset = true;
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    public float getLyricSpeed() {
        float speed = 1f;
        return speed;
    }

    public void setOffsetY(float offsetY) {
        if (!mAllowOffset) {
            return;
        }
        this.mOffsetY = offsetY;
        if (mOffsetY > (-600 + mLyricTotalHeight)) {
            mOffsetY = -600 + mLyricTotalHeight;
            isReachBottom = true;
        }
        scrollTo(0, (int) mOffsetY);
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

    public void setData(ArrayList<LyricObject> map, boolean isLyricReady, int lineNumber) {
        this.isLyricReady = isLyricReady;
        mLyricMap = map;
        mLyricTotalHeight = 75 * lineNumber;
        Log.d("TAGTAG", "mLyricTotalHeight" + mLyricTotalHeight);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d("TAGTAG", "滑动中"+mScroller.getCurrY());
            mAllowOffset = false;
            if (mScroller.getCurrY() < -600) {
                scrollTo(mScroller.getCurrX(), -600);
                mOffsetY = -600;
            } else if (mScroller.getCurrY() > (-600 + mLyricTotalHeight)) {
                scrollTo(mScroller.getCurrX(), (int) (-600 + mLyricTotalHeight));
                mOffsetY = mLyricTotalHeight - 600;
            } else {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                mOffsetY = mScroller.getCurrY();
            }
            postInvalidate();
        } else {
            mAllowOffset = true;
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
            Log.d("TAGTAG >0", "起点Y:" + scrollY + "fling 过程中的mOffsetY" + mOffsetY);
            mScroller.startScroll(0, scrollY, 0, -50 * elementA * elementB, duration);
            if (mOffsetY < -600) {
                mOffsetY = -600;
            }
        } else if (velocityY < 0 && !isReachBottom) {
            Log.d("TAGTAG >0", "起点Y:" + scrollY + "fling 过程中的mOffsetY" + mOffsetY);
            mScroller.startScroll(0, scrollY, 0, 50 * elementA * elementB, duration);
            if (mOffsetY > (-600 + mLyricTotalHeight)) {
                mOffsetY = -600 + mLyricTotalHeight;
            }
            invalidate();
        }

        if (isScrolled) {
            isScrolled = false;
            Log.d("TAGTAG", "起始点mOffsetY" + mOffsetY + "    终点mOffsetYCache" + (INTERVAL * (hlLineIndex + 1) + mOffsetY));
            createRollbackAnimator(mOffsetY,INTERVAL * (hlLineIndex + 1)+mOffsetY);
        }else {
            cancelRollbackAnimator();
        }
    }

    public void smoothScrollYToEdge(float distance, float velocityY) {
        int scrollY = getScrollY();
        int deltaY;
        if (velocityY > 0) {
            Log.d("TAGTAG", "手指从上往下猛滑");
            deltaY = - (int)(mOffsetY + 600);
        } else {
            Log.d("TAGTAG","手指从下往上猛滑");
            deltaY = (int) (mLyricTotalHeight - mOffsetY);
        }
        mScroller.startScroll(0, scrollY, 0, deltaY, 800);

        if (isScrolled) {
            isScrolled = false;
            Log.d("TAGTAG", "起始点mOffsetY" + mOffsetY + "    终点mOffsetYCache" + (INTERVAL * (hlLineIndex + 1) + mOffsetY));
            createRollbackAnimator(mOffsetY,INTERVAL * (hlLineIndex + 1)+mOffsetY);
        }else {
            cancelRollbackAnimator();
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
        public boolean onSingleTapUp(MotionEvent e) {
            listener.performClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mOffsetY += distanceY;
            if (mOffsetY < -600) {
                mOffsetY = -600;
                isReachTop = true;
            } else {
                isReachTop = false;
            }

            if (mOffsetY > (-600 + mLyricTotalHeight)) {
                mOffsetY = -600 + mLyricTotalHeight;
                isReachBottom = true;
            } else {
                isReachBottom = false;
            }
            isScrolled = true;
            scrollTo(0, (int) mOffsetY);
            invalidate();
            Log.d("TAGTAG", "scroll 过程中的mOffsetY" + mOffsetY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > 400 && Math.abs(velocityY) > 4000) {
                smoothScrollYToEdge(e1.getY() - e2.getY(), velocityY);
            } else {
                smoothScrollYTo(e1.getY() - e2.getY(), velocityY);
            }
            isFling = true;
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    public interface LyricOnClickListener {
        void performClick();

        void performLongClick();
    }

//    @Override
//    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
//        Log.d("TAGTAG", "x" + x + "   y" + y);
//    }
}
