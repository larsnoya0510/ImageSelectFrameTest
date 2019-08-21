package com.example.imageselectframetest.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.example.imageselectframetest.R;

public class XfermodeView extends View {

    /**
     * 18種圖形混合模式
     */
    private static final PorterDuff.Mode PorterDuffMode[] = {PorterDuff.Mode.ADD, PorterDuff.Mode.CLEAR, PorterDuff.Mode.DARKEN,
            PorterDuff.Mode.DST, PorterDuff.Mode.DST_ATOP, PorterDuff.Mode.DST_IN, PorterDuff.Mode.DST_OUT, PorterDuff.Mode.DST_OVER,
            PorterDuff.Mode.LIGHTEN, PorterDuff.Mode.MULTIPLY, PorterDuff.Mode.OVERLAY, PorterDuff.Mode.SCREEN, PorterDuff.Mode.SRC,
            PorterDuff.Mode.SRC_ATOP, PorterDuff.Mode.SRC_IN, PorterDuff.Mode.SRC_OUT, PorterDuff.Mode.SRC_OVER, PorterDuff.Mode.XOR};

    private PorterDuffXfermode porterDuffXfermode;

    private int mode;

    private int defaultMode = 0;

    private static final int Layers = Canvas.ALL_SAVE_FLAG;

    /**
     * 屏幕寬高
     */
    private int screenW;
    private int screenH;
    private Bitmap srcBitmap;
    private Bitmap dstBitmap;

    /**
     * 源圖和目標圖寬高
     */
    private int width = 120;
    private int height = 120;

    private Paint mShowpaint;
    public XfermodeView(Context context) {
        super(context);

    }

    public XfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XfermodeView);
        mode = typedArray.getInt(R.styleable.XfermodeView_ModeNum, defaultMode);
        screenW = ScreenUtil.getScreenW(context);
        screenH = ScreenUtil.getScreenH(context);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuffMode[mode]);
        srcBitmap = makeSrc(width, height);
        dstBitmap = makeDst(width, height);
        mShowpaint = new Paint();
        mShowpaint.setStyle(Paint.Style.STROKE);
        mShowpaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        paint.setStyle(Paint.Style.FILL);
        /**
         * 繪制藍色矩形+黃色圓形
         * */
        canvas.drawBitmap(srcBitmap, screenW / 8 - width / 4, screenH / 12 - height / 4, paint);
        canvas.drawBitmap(dstBitmap, screenW / 2, screenH / 12, paint);

        /**
         * 創建一個圖層，在圖層上演示圖形混合後的效果
         * */
        canvas.saveLayer(0, 0, screenW, screenH, null, Layers);

        /**
         * 繪制在設置了XferMode後混合後的圖形
         * */
        canvas.drawBitmap(dstBitmap, screenW / 4, screenH / 3, paint);
        paint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(srcBitmap, screenW / 4, screenH / 3, paint);
        paint.setXfermode(null);
        // 還原畫布
        canvas.restore();
    }
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF66AAFF);
        c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
        return bm;
    }
    private Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF26AAD1);
        c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
        //畫四周的邊框線
        mShowpaint.setColor(0xFF26AAD1);
        c.drawRect(0,0,w,h, mShowpaint);
        return bm;
    }
}
