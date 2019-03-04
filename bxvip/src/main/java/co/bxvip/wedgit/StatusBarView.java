package co.bxvip.wedgit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import co.bxvip.tools.DisplayUtils;

/**
 * <pre>
 *     author: vic
 *     time  : 18-3-31
 *     desc  : 状态栏
 * </pre>
 */
public class StatusBarView extends FrameLayout {
    private int statusBarHeight;

    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
        setBackgroundColor(Color.LTGRAY);
    }

    private void initialize() {
        this.statusBarHeight = DisplayUtils.getStatusBarHeight(getContext(), 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(DisplayUtils.getScreenWidth(), statusBarHeight);
    }
}
