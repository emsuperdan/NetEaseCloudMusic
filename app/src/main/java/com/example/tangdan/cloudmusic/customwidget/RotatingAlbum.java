package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;

public class RotatingAlbum extends LinearLayout {
    private Context mContext;
    private int mResId;
    private ImageView mImage;
    private Paint mPaint;
    private float mRadius;

    public RotatingAlbum(Context context) {
        this(context, null);
    }

    public RotatingAlbum(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatingAlbum(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        this.mResId = R.drawable.login_icon;
        mRadius = mContext.getResources().getDimension(R.dimen.circle_radius);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.yellow));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        initParams();
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }

    private void initParams(){

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mImage = (ImageView) findViewById(R.id.rotate_album);
        mImage.setImageResource(mResId);
    }
}
