package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.example.tangdan.cloudmusic.R;

public class LryView extends ScrollView {
    private String lyricStr;
    private Paint mLyricPaint;

    public LryView(Context context) {
        this(context, null);
    }

    public LryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(getResources().getColor(R.color.cadetblue));
        setAlpha(0.5f);
        initParams();
        setWillNotDraw(false);
    }

    private void initParams(){
        lyricStr = "塔特塔特塔特塔特asdasdasd忐asdsadsa忑忐忑忐忑忐忑\r\n我还哦哦好哦asdasdsadasdasdasdsadasdasdsadsad哦好哦哦好哦哦后";

        mLyricPaint = new Paint();
        mLyricPaint.setColor(getResources().getColor(android.R.color.black));
        mLyricPaint.setTextSize(20);
    }

    public void setLyric(String str){
        this.lyricStr = str;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawText(lyricStr,30,30,mLyricPaint);

        TextPaint textPaint = new TextPaint();
        textPaint.setARGB(0xFF,0,0,0);
        textPaint.setTextSize(20);
        StaticLayout layout = new StaticLayout(lyricStr,textPaint,60, Layout.Alignment.ALIGN_NORMAL,1.0f,0.0f,true);
        canvas.save();
        canvas.translate(20,20);
        layout.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        Log.d("TAGTAG", "x:" + x + "y:" + y);
    }
}
