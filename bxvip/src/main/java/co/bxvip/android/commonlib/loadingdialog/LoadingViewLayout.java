package co.bxvip.android.commonlib.loadingdialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.bxvip.refresh.utils.IDs;
import co.bxvip.tools.SelectorFactory;
import co.bxvip.wedgit.BxToolIconView;

import static android.view.Gravity.CENTER;
import static co.bxvip.tools.DisplayUtils.dip2px;

/**
 * <pre>
 *     author: vic
 *     time  : 18-4-3
 *     desc  : 加载进度布局
 * </pre>
 */
public class LoadingViewLayout extends RelativeLayout {

    private LinearLayout rootLinearLayout;

    private TextView textView;

    public BxToolIconView loadingBar;

    public LoadingViewLayout(Context context) {
        this(context, null);
    }

    public LoadingViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setId(IDs.refresh_ex_header - 1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        rootLinearLayout = new LinearLayout(context);
        rootLinearLayout.setLayoutParams(layoutParams);
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        rootLinearLayout.setBackgroundDrawable(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(Color.BLACK)
                .setCornerRadius(10)
                .create());
        textView = new TextView(context);
        textView.setText("加载中");
        textView.setTextColor(Color.LTGRAY);
        LinearLayout.LayoutParams loadingBarParams = new LinearLayout.LayoutParams(dip2px(context, 60), dip2px(context, 60));
        loadingBarParams.gravity = CENTER;
        loadingBarParams.topMargin = dip2px(context, 15);
        loadingBar = new BxToolIconView(context);
        loadingBar.setPadding(10, 10, 10, 10);
        loadingBar.createFlowerProgressBar().setDrawColor(Color.LTGRAY).draw();
        rootLinearLayout.addView(loadingBar, loadingBarParams);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = CENTER;
        textParams.topMargin = dip2px(context, 15);
        rootLinearLayout.addView(textView, textParams);
        LayoutParams rootParams = new LayoutParams(dip2px(context, 120), dip2px(context, 120));
        rootParams.addRule(CENTER_IN_PARENT);
        this.addView(rootLinearLayout, rootParams);
    }
}
