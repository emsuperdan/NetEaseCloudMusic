package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.tangdan.cloudmusic.R;

public class RotatingAlbum extends LinearLayout {
    private Context mContext;
    private int mResId;
    private Paint mPaint;
    private float percent;
    private float mWidth;

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
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;

        //Shader.TileMode.CLAMP为拉伸图片铺满
        BitmapShader bitmapShader = new BitmapShader(getBitmap(mResId, w)
                , Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(percent, mWidth / 2, mWidth / 2);

        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
        Log.d("TAGTAG", "percent" + percent);
    }

    private Bitmap getBitmap(int resId, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        options.inJustDecodeBounds = false;
        //设置位图的屏幕密度,即每英寸有多少个像素
        options.inDensity = options.outWidth;
        //设置位图被画出来时的目标像素密度
        //与options.inDensity配合使用,可对图片进行缩放
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), resId, options);
    }

    public void setRotatePer(float per) {
        this.percent = per;
    }
}
