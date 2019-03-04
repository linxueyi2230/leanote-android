package co.bxvip.wedgit;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.LinkedHashMap;
import java.util.Map;

import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.DOWN;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.LEFT;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.LEFTDOWN;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.LEFTUP;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.RIGHT;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.RIGHTDOWN;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.RIGHTUP;
import static co.bxvip.wedgit.BxToolIconView.ArrowDirection.UP;

/**
 * <pre>
 *     author: vic
 *     time  : 18-3-28
 *     desc  : 箭头view  这个是继承 ImageView  的自定义view ，如果设置了背景  则是不做绘制
 *                                r                    2r
 *     ---------------------------------------------------→ y
 *    ∣
 *    ∣
 *    ∣
 *    ∣         0                6                     7
 *    ∣
 *    ∣
 *    ∣
 *    ∣         2                                     3
 *    ∣
 *    ∣
 *    ∣
 *    ∣
 *    ∣         4               7                     5
 *    ∣
 *    ∣
 *    ∣
 *    ∣-----------------------------------------------------
 *    ↓
 *     x
 *
 *
 * </pre>
 */
public class BxToolIconView extends ImageView implements Runnable {

    // 箭头颜色 圆形背景颜色
    @ColorInt
    private int drawColor = Color.GRAY, drawColor2 = Color.WHITE, circleBackgroundColor = Color.TRANSPARENT;

    // 箭头到对勾对应的四个点

    private Map<ArrowDirection, PointF> pointFMap = new LinkedHashMap<>();

    // view的宽高
    private int mViewWidth;
    private int mViewHeight;

    // view的中心
    private int mViewCenterX;
    private int mViewCenterY;

    // 半径
    private int mRadius;

    /**
     * 画实心圆
     */
    private Paint mCirclePaint;

    /**
     * 画符号
     */
    private Paint mSymbolPaint;

    /**
     * 箭头方向  默认 向下
     */
    private ArrowDirection arrowDirection = DOWN; // 方向
    /**
     * 绘制view 的类型
     */
    private ViewType viewType = ViewType.ARROW;

    /**
     * view  的类型 fork 叉 ; hook 勾  arrow 箭头; progressbar 圆形loadingview ,TITLE_ARROW title bar arrow ,CIRCLE_PLUS 原型加号
     */
    public enum ViewType {
        FORK, HOOK, ARROW, PROGRESS, FLOWER_PROGRESS, TITLE_ARROW, CIRCLE_PLUS
    }

    /**
     * 箭头使用
     */
    public enum ArrowDirection {
        LEFT, LEFTUP, LEFTDOWN, UP, RIGHT, RIGHTUP, RIGHTDOWN, DOWN,
    }

    /**
     * progress bar use
     *
     * @param context
     */
    private static final int PEROID = 16;// 绘制周期
    private boolean isOnDraw = false;
    private boolean isRunning = false;
    private int startAngle = 0;
    private int swipeAngle;
    private int speed = 6;


    public BxToolIconView(Context context) {
        this(context, null);
    }

    public BxToolIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setBackgroundColor(Color.TRANSPARENT);
    }

    private void init() {
        // 画背景圆
        if (mCirclePaint == null)
            mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleBackgroundColor);

        // 画符号
        if (mSymbolPaint == null)
            mSymbolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSymbolPaint.setColor(drawColor);
        mSymbolPaint.setStyle(Paint.Style.STROKE);
        mSymbolPaint.setStrokeCap(Paint.Cap.ROUND);
        mSymbolPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        mViewCenterX = mViewWidth / 2;
        mViewCenterY = mViewHeight / 2;

        int tempX = mViewCenterX - (getPaddingRight() + getPaddingLeft()) / 2;
        int tempY = mViewCenterX - (getPaddingTop() + getPaddingBottom()) / 2;
        mRadius = tempX <= tempY ? tempX : tempY;
        mSymbolPaint.setStrokeWidth(mRadius * 2 / 18);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mViewWidth != 0) {
            pointFMap.put(LEFT, new PointF(mRadius / 2, mRadius / 2 * 2));
            pointFMap.put(LEFTUP, new PointF(mRadius / 2, mRadius / 2));
            pointFMap.put(LEFTDOWN, new PointF(mRadius / 2, mRadius / 2 * 3));
            pointFMap.put(UP, new PointF(mRadius / 2 * 2, mRadius / 2));
            pointFMap.put(RIGHT, new PointF(mRadius / 2 * 3, mRadius / 2 * 2));
            pointFMap.put(RIGHTUP, new PointF(mRadius / 2 * 3, mRadius / 2));
            pointFMap.put(RIGHTDOWN, new PointF(mRadius / 2 * 3, mRadius / 2 * 3));
            pointFMap.put(DOWN, new PointF(mRadius / 2 * 2, mRadius / 2 * 3));
        }
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawable == null || resid <= 0)
            return;
        // 先绘制背景
        mCirclePaint.setColor(circleBackgroundColor);
        mSymbolPaint.setColor(drawColor);
        canvas.drawCircle(mViewCenterX, mViewCenterY, mRadius, mCirclePaint);
        switch (viewType) {
            case TITLE_ARROW:// 箭头
            case ARROW:
                // DOWN
                if (viewType == ViewType.ARROW) {
                    drawLine(canvas, pointFMap.get(UP), pointFMap.get(LEFT));
                    drawLine(canvas, pointFMap.get(UP), pointFMap.get(DOWN));
                    drawLine(canvas, pointFMap.get(UP), pointFMap.get(RIGHT));
                } else {
                    mSymbolPaint.setStrokeWidth(mRadius * 3 / 18);
                    canvas.drawLine(mRadius / 4 + getPaddingLeft(), mRadius / 4 * 5 + getPaddingTop(),
                            mRadius + getPaddingLeft(), mRadius / 4 * 3 + getPaddingTop(), mSymbolPaint);
                    canvas.drawLine(mRadius / 4 * 7 + getPaddingLeft(), mRadius / 4 * 5 + getPaddingTop(),
                            mRadius + getPaddingLeft(), mRadius / 4 * 3 + getPaddingTop(), mSymbolPaint);
                }
                //Rotation
                int rotation = arrowDirection == UP ? 0 : arrowDirection == RIGHTUP ? 45 : arrowDirection == RIGHT ? 90
                        : arrowDirection == RIGHTDOWN ? 135 : arrowDirection == DOWN ? 180 : arrowDirection == LEFTDOWN ? 225
                        : arrowDirection == LEFT ? 270 : 315;
                setRotation(rotation);
                break;
            case CIRCLE_PLUS:
                // 画圆
                mSymbolPaint.setColor(drawColor2);
                int d = mRadius * 2 / 18;
                mSymbolPaint.setStrokeWidth(2);
                canvas.drawArc(new RectF(d, d, mViewWidth - d, mViewHeight - d), 0, 360, false, mSymbolPaint);
                mSymbolPaint.setStrokeWidth(mRadius / 7);
                mSymbolPaint.setColor(drawColor);
                drawLine(canvas, pointFMap.get(LEFT), pointFMap.get(RIGHT));
                drawLine(canvas, pointFMap.get(DOWN), pointFMap.get(UP));
                break;
            case FORK: // 叉
                drawLine(canvas, pointFMap.get(LEFTUP), pointFMap.get(RIGHTDOWN));
                drawLine(canvas, pointFMap.get(LEFTDOWN), pointFMap.get(RIGHTUP));
                break;
            case HOOK: // 勾
                drawLine(canvas, pointFMap.get(LEFT), pointFMap.get(DOWN));
                drawLine(canvas, pointFMap.get(DOWN), pointFMap.get(RIGHTUP));
                break;
            case FLOWER_PROGRESS: // 菊花进度条
            case PROGRESS: // 进度条
                if (viewType == ViewType.PROGRESS) {
                    int index = startAngle / 360;
                    if (index % 2 == 0) {
                        swipeAngle = (startAngle % 720) / 2;
                    } else {
                        swipeAngle = 360 - (startAngle % 720) / 2;
                    }
                    canvas.drawArc(getOvalRect(), startAngle, swipeAngle, false, mSymbolPaint);
                } else {
                    for (int i = 0; i < 12; i++) {
                        mSymbolPaint.setAlpha(((i + 1 + control) % 12 * 255) / 12);
                        canvas.drawLine(mViewCenterX, mViewCenterY - mRadius / 5 * 4, mViewCenterX, mViewCenterY - mRadius / 5 * 2, mSymbolPaint);
                        canvas.rotate(30, mViewCenterX, mViewCenterY);
                    }
                }

                break;
        }
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 40;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 40;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    private RectF ovalRect = null;

    private RectF getOvalRect() {
        if (ovalRect == null) {
            int d = mViewWidth / 5;
            ovalRect = new RectF(d, d, mViewWidth - d, mViewHeight - d);
        }
        return ovalRect;
    }

    // 绘制线段
    private void drawLine(Canvas canvas, PointF start, PointF end) {
        if (start.x != 0 && end.x != 0) {
            canvas.drawLine(start.x + getPaddingLeft(), start.y + getPaddingTop(),
                    end.x + getPaddingLeft(), end.y + getPaddingTop(), mSymbolPaint);
        }
    }

    private int control = 1;

    @Override
    public void run() {
        if (viewType == ViewType.PROGRESS) {
//            ValueAnimator.ofInt()
            while (isOnDraw) {
                isRunning = true;
                long startTime = System.currentTimeMillis();
                startAngle += speed;
                postInvalidate();
                long time = System.currentTimeMillis() - startTime;
                if (time < PEROID) {
                    try {
                        Thread.sleep(PEROID - time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 开始进度条的绘制速度  默认6
     *
     * @param speed
     */
    public void setProgressSpeed(int speed) {
        this.speed = speed;
    }

    private Thread mProgessThread = null;

    private ValueAnimator valueAnimator;

    /**
     * 开始进度条的动画
     */
    public void startProgressRun() {
        isOnDraw = true;
        switch (viewType) {
            case PROGRESS:
                if (mProgessThread == null) {
                    mProgessThread = new Thread(this);
                }
                if (!mProgessThread.isAlive())
                    mProgessThread.start();
                break;

            case FLOWER_PROGRESS:
                if (valueAnimator == null) {
                    valueAnimator = ValueAnimator.ofInt(12, 1);
                    valueAnimator.setDuration(1000);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                    valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            control = (int) animation.getAnimatedValue();
                            invalidate();
                        }
                    });
                }
                valueAnimator.start();
                break;
        }
    }

    /**
     * 停止进度条的动画
     */
    public void stopProgressRun() {
        isOnDraw = false;
        switch (viewType) {
            case FLOWER_PROGRESS:
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    valueAnimator.end();
                }
                break;
            case PROGRESS:
                if (mProgessThread != null && !mProgessThread.isInterrupted()) {
                    mProgessThread.interrupt();
                }
                break;
        }
    }

    /**
     * 进度条是否在运行
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }


    /**
     * default create  titlebar 返回键箭头 默认颜色是 Color.WHITE  默认箭头向左
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setArrowDirection(ArrowDirection) 设置箭头的方向
     * @see #setDrawColor(int) 设置箭头颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * <p>
     * --------------------------最后调用-----------------------
     * @see #draw() 最后调用 刷新UI
     */
    public BxToolIconView createTitleArrow() {
        this.viewType = ViewType.TITLE_ARROW;
        this.drawColor = Color.WHITE;
        this.arrowDirection = LEFT;
        this.circleBackgroundColor = Color.TRANSPARENT;
        init();
        return this;
    }

    /**
     * default create  下拉箭头  默认颜色是 Color.GRAY
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setArrowDirection(ArrowDirection) 设置箭头的方向
     * @see #setDrawColor(int) 设置箭头颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * <p>
     * --------------------------最后调用-----------------------
     * @see #draw() 最后调用 刷新UI
     */
    public BxToolIconView createArrow() {
        this.viewType = ViewType.ARROW;
        this.drawColor = Color.GRAY;
        this.circleBackgroundColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * default create  叉  默认颜色是 Color.WHITE
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setDrawColor(int) 设置箭头颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * <p>
     * --------------------------最后调用-----------------------
     * @see #draw() 最后调用 刷新UI
     */
    public BxToolIconView createFork() {
        this.viewType = ViewType.FORK;
        this.drawColor = Color.WHITE;
        this.circleBackgroundColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * default create  勾  默认颜色是 Color.WHITE
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setDrawColor(int) 设置勾颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * <p>
     * --------------------------最后调用-----------------------
     * @see #draw() 最后调用 刷新UI
     */
    public BxToolIconView createHook() {
        this.viewType = ViewType.HOOK;
        this.drawColor = Color.WHITE;
        this.circleBackgroundColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * default create  圆外形 里面是十字  圆线默认颜色是 Color.WHITE
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setDrawColor(int) 设置里面十字颜色
     * @see #setDrawColor2(int) 设置外圈颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * <p>
     * --------------------------最后调用-----------------------
     * @see #draw() 最后调用 刷新UI
     */
    public BxToolIconView createCirclePlus() {
        this.viewType = ViewType.CIRCLE_PLUS;
        this.drawColor = Color.GRAY;
        this.drawColor2 = Color.LTGRAY;
        this.circleBackgroundColor = Color.TRANSPARENT;
        this.draw();
        return this;
    }

    /**
     * default create  加载动画进度条 默认颜色： Color.GRAY
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setDrawColor(int) 设置里面十字颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * @see #setProgressSpeed(int) 加载速度 默认 6
     * <p>
     * --------------------------最后调用-----------------------
     * @see #startProgressRun() 开始动画绘制
     * @see #stopProgressRun() 结束动画的绘制
     */
    public BxToolIconView createCircleProgressBar() {
        this.viewType = ViewType.PROGRESS;
        this.drawColor = Color.GRAY;
        this.circleBackgroundColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * default create  加载动画进度条 默认颜色： Color.GRAY
     * <p>
     * -------------------------颜色修改------------------------
     *
     * @see #setDrawColor(int) 设置里面十字颜色
     * @see #setCircleBackgroundColor(int) 圆形背景的颜色
     * @see #setProgressSpeed(int) 加载速度 默认 6
     * <p>
     * --------------------------最后调用-----------------------
     * @see #startProgressRun() 开始动画绘制
     * @see #stopProgressRun() 结束动画的绘制
     */
    public BxToolIconView createFlowerProgressBar() {
        this.viewType = ViewType.FLOWER_PROGRESS;
        this.drawColor = Color.BLACK;
        this.circleBackgroundColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * 设置 绘制的颜色
     *
     * @param drawColor
     * @return
     */
    public BxToolIconView setDrawColor(@ColorInt int drawColor) {
        this.drawColor = drawColor;
        return this;
    }

    /**
     * 设置 绘制的颜色
     *
     * @param drawColor
     * @return
     */
    public BxToolIconView setDrawColorRes(@ColorRes int drawColor) {
        this.drawColor = ContextCompat.getColor(getContext(), drawColor);
        return this;
    }

    /**
     * 设置 绘制的颜色
     *
     * @param drawColor2
     * @return
     */
    public BxToolIconView setDrawColor2(@ColorInt int drawColor2) {
        this.drawColor2 = drawColor2;
        return this;
    }

    /**
     * 设置 绘制的颜色
     *
     * @param drawColor2
     * @return
     */
    public BxToolIconView setDrawColor2Res(@ColorRes int drawColor2) {
        this.drawColor2 = drawColor2;
        return this;
    }

    /**
     * 设置view的圆形背景
     *
     * @param circleBackgroundColor
     * @return
     */
    public BxToolIconView setCircleBackgroundColor(int circleBackgroundColor) {
        this.circleBackgroundColor = circleBackgroundColor;
        return this;
    }

    /**
     * 箭头使用 title arrow 同样可以使用
     *
     * @param arrowDirection
     * @return
     */
    public BxToolIconView setArrowDirection(ArrowDirection arrowDirection) {
        this.arrowDirection = arrowDirection;
        return this;
    }

    /**
     * 除了 加载进度条可以不用调用  或者是 直接调用
     *
     * @see #invalidate()
     */
    public void draw() {
        invalidate();
    }

    private Drawable drawable = null;
    @IdRes
    private int resid;

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        this.drawable = background;
        this.resid = 1;
        draw();
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        this.resid = resid;
        draw();
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        this.drawable = background;
        draw();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        this.drawable = drawable;
        draw();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        this.resid = resid;
        draw();
    }
}
