package co.bxvip.android.commonlib.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * <pre>
 *     author: vic
 *     time  : 18-4-2
 *     desc  : builder 模式的dialog
 * </pre>
 */
public class CustomDialog extends AlertDialog {

    private int height = 0;
    private int width = 0;
    private boolean cancelTouchout = false;
    @LayoutRes
    private int layoutResId = -1;
    private View view = null;
    private int resStyle = -1;


    private SparseArray<View> mViews = null;

    private CustomDialog(Builder builder) {
        this(builder, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    public CustomDialog(Builder builder, int themeResId) {
        super(builder.context, themeResId);
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        layoutResId = builder.layoutResId;
        setView(builder.view);
        resStyle = builder.resStyle;
        mViews = new SparseArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (lp == null) {
            lp = new WindowManager.LayoutParams();
        }
        lp.gravity = Gravity.CENTER;
        lp.height = height;
        lp.width = width;
        getWindow().setAttributes(lp);
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public static final class Builder {
        private int height;
        private int width;
        private boolean cancelTouchout;
        private int layoutResId;
        private View view;
        private int resStyle;

        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setHeight(int val) {
            height = val;
            return this;
        }

        public Builder setWidth(int val) {
            width = val;
            return this;
        }

        public Builder setCancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public Builder setLayoutResId(int val) {
            layoutResId = val;
            return this;
        }

        public Builder setView(View val) {
            view = val;
            return this;
        }

        public Builder setResStyle(int val) {
            resStyle = val;
            return this;
        }

        public CustomDialog build() {
            return new CustomDialog(this);
        }
    }
}
