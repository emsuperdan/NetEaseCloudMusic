package com.example.tangdan.cloudmusic.customwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
    private Drawable mDraw;
    private float percent;

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
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.red));
        mDraw = getResources().getDrawable(R.drawable.login_icon);

        initParams();
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = ((BitmapDrawable) (mDraw)).getBitmap();
        Bitmap b = getCircleBitmap(bitmap);

        Rect rectDest = new Rect(0,0,getWidth(),getHeight());
        canvas.drawBitmap(b,null,rectDest,null);
        mPaint.reset();
    }

    private Bitmap getCircleBitmap(Bitmap bitmap){
        Bitmap outPut = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outPut);

        Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        canvas.drawARGB(0,0,0,0);
        int x = bitmap.getWidth();

        canvas.drawCircle(x/2,x/2,x/2,mPaint);
        canvas.rotate(percent, x / 2, x / 2);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect,rect,mPaint);
        return outPut;
    }

    public void setRotatePer(float per){
        this.percent = per;
    }

    private void initParams(){

    }
}
