package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;

public class LryTextView extends TextView {
    private float mViewWidth, mViewHeight;
    private Paint mCenterLinePaint;

    public LryTextView(Context context) {
        this(context, null);
    }

    public LryTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LryTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCenterLinePaint = new Paint();
        mCenterLinePaint.setColor(getResources().getColor(R.color.black));
        mCenterLinePaint.setStrokeWidth(20);

        initParams();
        setWillNotDraw(false);
    }

    private void initParams() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,mViewHeight/2-5,mViewWidth,mViewHeight/2+5,mCenterLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("TAGTAG1","get down envent");
                break;
        }
        return super.onTouchEvent(event);
    }
}
