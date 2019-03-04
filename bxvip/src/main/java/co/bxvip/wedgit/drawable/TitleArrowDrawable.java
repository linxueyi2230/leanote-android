package co.bxvip.wedgit.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * <pre>
 *     author: vic
 *     time  : 18-4-5
 *     desc  : ${END}
 * </pre>
 */
public class TitleArrowDrawable extends Drawable {


    /**
     * 画符号
     */
    private Paint mSymbolPaint;

    @ColorInt
    private int drawColor = Color.WHITE;


    public TitleArrowDrawable() {
        // 画符号
        if (mSymbolPaint == null)
            mSymbolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSymbolPaint.setColor(drawColor);
        mSymbolPaint.setStyle(Paint.Style.STROKE);
        mSymbolPaint.setStrokeCap(Paint.Cap.ROUND);
        mSymbolPaint.setStrokeJoin(Paint.Join.ROUND);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        int cX = bounds.centerX();
        int cY = bounds.centerY();
        int mRadius = (cX <= cY ? cX : cY) / 5 * 3;
        mSymbolPaint.setStrokeWidth(mRadius * 3 / 18);
        canvas.drawLine(mRadius / 5 * 4 + mRadius / 5, cY, cX + mRadius / 5, cY - mRadius, mSymbolPaint);
        canvas.drawLine(mRadius / 5 * 4 + mRadius / 5, cY, cX + mRadius / 5, cY + mRadius, mSymbolPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
