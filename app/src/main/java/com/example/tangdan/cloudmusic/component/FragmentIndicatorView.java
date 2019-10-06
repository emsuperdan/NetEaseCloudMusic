package com.example.tangdan.cloudmusic.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.tangdan.cloudmusic.R;

public class FragmentIndicatorView extends View {
    private Context mContext;
    private Paint mPaint;
    private Paint mIndicatorPaint;
    private int viewWidth;
    private int viewHeight;
    private float mIndicatorWidth;
    private int mCurrentPos;
    private int mNextPos;
    private float mPercent;

    public FragmentIndicatorView(Context context) {
        this(context, null);
    }

    public FragmentIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initParams();
    }

    private void initParams() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.red));

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(mContext.getResources().getColor(R.color.white));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
        mIndicatorWidth = viewWidth / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, viewWidth, viewHeight, mPaint);

        float each = (viewWidth - 3 * mIndicatorWidth) / 6;
        float left = 3 * each + mIndicatorWidth + mPercent * mCurrentPos * (2 * each + mIndicatorWidth);
        canvas.drawRect(left, 0, left + mIndicatorWidth, viewHeight, mIndicatorPaint);
    }

    public void setIndicatorPos(int nowPos, float percent) {
        mCurrentPos = nowPos - 1;
        mPercent = percent;
        invalidate();
    }
}
