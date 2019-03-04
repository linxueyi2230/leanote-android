package co.bxvip.wedgit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.support.v7.widget.builder.RecycleViewDimen.dip2Px;

/**
 * <pre>
 *     author: vic
 *     time  : 18-3-29
 *     desc  : 默认加载view
 * </pre>
 */
public class DefaultLoadingView extends LinearLayout {
    BxToolIconView loadingView = null;
    BxToolIconView arrowView = null;
    TextView textView = null;

    public DefaultLoadingView(Context context) {
        this(context, null);
    }

    public DefaultLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2Px(getResources(), 40));
        params.gravity = Gravity.RIGHT;
        setLayoutParams(params);
        // loading view
        LinearLayout layout = new LinearLayout(context);
        // 占位 view
        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        layout.addView(new View(context), params);
        // load view
        params = new LayoutParams(dip2Px(getResources(), 35), dip2Px(getResources(), 35));
        params.gravity = Gravity.CENTER_VERTICAL;
        layout.addView(loadingView = new BxToolIconView(context), params);
        loadingView.createCircleProgressBar().startProgressRun();

        layout.addView(arrowView = new BxToolIconView(context), params);
        arrowView.createArrow().setArrowDirection(BxToolIconView.ArrowDirection.UP).invalidate();
        arrowView.setVisibility(GONE);

        params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 6;
        addView(layout, params);
        // wenzi
        params = new LayoutParams(-2, -2);
        params.weight = 5;
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = 20;
        textView = new TextView(context);
        textView.setTextSize(12);
        textView.setText("正在加载...");
        addView(textView, params);
    }

    public BxToolIconView getLoadingView() {
        return loadingView;
    }

    public BxToolIconView getArrowView() {
        return arrowView;
    }

    public TextView getTextView() {
        return textView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minWH = dip2Px(getResources(), 40);
//        // 父容器传过来的宽度方向上的模式
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        // 父容器传过来的高度方向上的模式
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        // 父容器传过来的宽度的值
//        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 父容器传过来的高度的值
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();
        if (height <= minWH) {
            height = minWH;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 设置loadingview的颜色
     *
     * @param color
     */
    public DefaultLoadingView setLoadingViewColor(int color) {
        loadingView.setDrawColor(color).invalidate();
        return this;
    }

    /**
     * 设置加载的文字
     *
     * @param text
     */
    public DefaultLoadingView setLoadText(String text) {
        textView.setText(text);
        return this;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     */
    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
