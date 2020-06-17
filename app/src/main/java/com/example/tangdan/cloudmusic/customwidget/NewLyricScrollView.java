package com.example.tangdan.cloudmusic.customwidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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
    private float mOffsetY;//记录歌词滚动偏移
    private final int INTERVAL = 100;//歌词每行的间隔
    Paint paint = new Paint();//画笔，用于画不是高亮的歌词
    Paint paintHL = new Paint();//画笔，用于画高亮的歌词，即当前唱到这句歌词
    Paint paintDash = new Paint();
    Paint paintPlay = new Paint();
    private float mLyricTotalHeight;
    private int mLineNumber;
    private ArrayList<LyricObject> mLyricMap = new ArrayList<>();
    private int hlLineIndex = 0;
    private boolean isLyricReady;
    private boolean isReachTop = true, isReachBottom;
    private boolean mAllowOffset = true;
    private LyricOnClickListener listener;
    private ValueAnimator mRollbackAnimator;
    private ValueAnimator mShowDashAnimator;
    private int mIndexCache = 1;
    private int mOldTime;

    private Scroller mScroller;
    private GestureDetector mGestureDetector;
    private boolean isFling;
    private boolean showDashLine;
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
        paintHL.setTextSize(50);

        paintDash.setAntiAlias(true);
        paintDash.setStyle(Paint.Style.STROKE);
        paintDash.setStrokeWidth(4);
        paintDash.setColor(Color.BLACK);
        paintDash.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));

        paintPlay.setAntiAlias(true);
        paintPlay.setStyle(Paint.Style.STROKE);
        paintPlay.setStrokeWidth(4);
        paintPlay.setColor(Color.BLACK);

        mScroller = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
    }

    private void createRollbackAnimator(float startY, float endY) {
        cancelRollbackAnimator();
        if (startY == endY) {
            return;
        }
        mRollbackAnimator = ValueAnimator.ofFloat(startY, endY);
        mRollbackAnimator.setStartDelay(3000);
        mRollbackAnimator.setDuration(500);
        mRollbackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetY = (float) animation.getAnimatedValue();
                scrollTo(0, (int) mOffsetY);
                invalidate();
            }
        });
        mRollbackAnimator.start();
    }

    private void cancelRollbackAnimator() {
        if (mRollbackAnimator != null) {
            mRollbackAnimator.removeAllListeners();
            mRollbackAnimator.cancel();
            mRollbackAnimator = null;
        }
    }

    private void createShowDashAnimator() {
        cancelShowDashAnimator();
        mShowDashAnimator = ValueAnimator.ofFloat(0, 1);
        mShowDashAnimator.setStartDelay(3000);
        mShowDashAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("TAGTAG","hidedashLine");
                showDashLine = false;
                invalidate();
            }
        });
        mShowDashAnimator.start();
    }

    private void cancelShowDashAnimator() {
        if (mShowDashAnimator != null) {
            mShowDashAnimator.removeAllListeners();
            mShowDashAnimator.cancel();
            mShowDashAnimator = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isLyricReady) {
            return;
        }
        canvas.drawText(mLyricMap.get(hlLineIndex).getLyric(), mCenterX, 650 + INTERVAL * (hlLineIndex + 1), paintHL);

        for (int i = 0; i < hlLineIndex; i++) {
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, 650 + (INTERVAL) * (i + 1), paint);
        }
        for (int i = hlLineIndex + 1; i < mLyricMap.size(); i++) {
            canvas.drawText(mLyricMap.get(i).getLyric(), mCenterX, 650 + (INTERVAL) * (i + 1), paint);
        }
        if (showDashLine) {
            canvas.drawOval(0, lyricHeight / 2 + mOffsetY - 20, 40, lyricHeight / 2 + mOffsetY + 20, paintPlay);
            canvas.drawLine(40, lyricHeight / 2 + mOffsetY, mCenterX * 2, lyricHeight / 2 + mOffsetY, paintDash);
        }
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
                showDashLine = true;
                Log.d("TAGTAG","showdashLine");
                break;
            case MotionEvent.ACTION_UP:
                mAllowOffset = true;
                if (showDashLine) {
                    createShowDashAnimator();
                } else {
                    cancelShowDashAnimator();
                }
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
        if (mOffsetY > (mLyricTotalHeight)) {
            mOffsetY = mLyricTotalHeight;
            isReachBottom = true;
        } else if (mOffsetY < 0) {
            mOffsetY = 0;
            isReachTop = true;
        }
        scrollTo(0, (int) mOffsetY);
        invalidate();
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public void setSelectIndex(int currentTime) {
        if (mOldTime > currentTime) {//如果用户把pos指针往当前位置的前面调整，那么重置index
            int index = 1;
            for (int i = index; i < mLyricMap.size(); i++) {//拿到往前调整到的位置的index
                LyricObject temp = mLyricMap.get(i);
                if (temp.getBeginTime() < currentTime) {
                    mOldTime = currentTime;
                    hlLineIndex = index++;
                }
            }

            for (int i = 0; i < (mIndexCache - index); i++) {//相减得到相差的offsetY
                setOffsetY(getOffsetY() - 100);
            }

            mIndexCache = index;
            return;
        }

        for (int i = mIndexCache; i < mLyricMap.size(); i++) {
            LyricObject temp = mLyricMap.get(i);
            if (temp.getBeginTime() < currentTime) {
                mOldTime = currentTime;
                hlLineIndex = mIndexCache++;
                setOffsetY(getOffsetY() + 100);
            }
        }
    }

    public void setData(ArrayList<LyricObject> map, boolean isLyricReady, int lineNumber) {
        this.isLyricReady = isLyricReady;
        mLyricMap = map;
        mLyricTotalHeight = 93 * lineNumber;
        mLineNumber = lineNumber;
        Log.d("TAGTAG", "mLyricTotalHeight" + mLyricTotalHeight);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d("TAGTAG", "滑动中" + mScroller.getCurrY());
            mAllowOffset = false;
            if (mScroller.getCurrY() < 0) {
                scrollTo(mScroller.getCurrX(), 0);
                mOffsetY = 0;
            } else if (mScroller.getCurrY() > (mLyricTotalHeight)) {
                scrollTo(mScroller.getCurrX(), (int) (mLyricTotalHeight));
                mOffsetY = mLyricTotalHeight;
            } else {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                mOffsetY = mScroller.getCurrY();
            }

//            if (isScrolled) {
//                isScrolled = false;
//                createRollbackAnimator(mOffsetY, hlLineIndex * 100);
//            } else {
//                cancelRollbackAnimator();
//            }
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
            if (mOffsetY < 0) {
                mOffsetY = 0;
            }
        } else if (velocityY < 0 && !isReachBottom) {
            Log.d("TAGTAG >0", "起点Y:" + scrollY + "fling 过程中的mOffsetY" + mOffsetY);
            mScroller.startScroll(0, scrollY, 0, 50 * elementA * elementB, duration);
            if (mOffsetY > (mLyricTotalHeight)) {
                mOffsetY = mLyricTotalHeight;
            }
            invalidate();
        }
    }

    public void fullScrollYto(float distance, float velocityY) {
        int scrollY = getScrollY();
        int deltaY;
        if (velocityY > 0) {
            Log.d("TAGTAG", "手指从上往下猛滑");
            deltaY = -(int) (mOffsetY);
        } else {
            Log.d("TAGTAG", "手指从下往上猛滑");
            deltaY = (int) (mLyricTotalHeight - mOffsetY);
        }
        mScroller.startScroll(0, scrollY, 0, deltaY, 800);
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
            if (mOffsetY < 0) {
                mOffsetY = 0;
                isReachTop = true;
            } else {
                isReachTop = false;
            }

            if (mOffsetY > (mLyricTotalHeight)) {
                mOffsetY = mLyricTotalHeight;
                isReachBottom = true;
            } else {
                isReachBottom = false;
            }
            isScrolled = true;
            scrollTo(0, (int) mOffsetY);

            if (isScrolled) {
                isScrolled = false;
                createRollbackAnimator(mOffsetY, hlLineIndex * 100);
            } else {
                cancelRollbackAnimator();
            }

            invalidate();
            Log.d("TAGTAG", "scroll 过程中的mOffsetY" + mOffsetY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > 400 && Math.abs(velocityY) > 4000) {
                fullScrollYto(e1.getY() - e2.getY(), velocityY);
            } else {
                smoothScrollYTo(e1.getY() - e2.getY(), velocityY);
            }
            isFling = true;
            isScrolled = true;
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
