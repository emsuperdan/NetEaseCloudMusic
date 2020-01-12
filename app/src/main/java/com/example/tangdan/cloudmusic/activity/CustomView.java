package com.example.tangdan.cloudmusic.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.tangdan.cloudmusic.R;

public class CustomView extends LinearLayout {
    RelativeLayout linearLayout;
    int percent;
    int totalHeight,totalWidth;
    private Paint paint;
    private Paint progressPaint;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet set) {
        this(context, set,0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        LayoutInflater.from(context).inflate(R.layout.activity_view_custom_item,this);

//        setBackgroundColor(context.getResources().getColor(R.color.yellow));
//        linearLayout = this.findViewById(R.id.custom_root_view);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(context.getResources().getColor(R.color.black));
//
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(context.getResources().getColor(R.color.yellow));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

        private void updateUI() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawRoundRect(new RectF(50,50,450,150),20,20,paint);
//        canvas.clipRect(0,0,50,50);
        canvas.drawRect(10,10,200,150, progressPaint);

        canvas.restoreToCount(layerId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension( MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        totalHeight = this.getHeight();
        totalWidth = this.getWidth();
    }

    public void setPercent(int n){
        this.percent = n;
        updateUI();
    }
}
