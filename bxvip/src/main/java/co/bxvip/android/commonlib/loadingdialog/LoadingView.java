package co.bxvip.android.commonlib.loadingdialog;

import android.annotation.SuppressLint;
import android.content.Context;

import co.bxvip.android.commonlib.dialog.BxDialog;

/**
 * <pre>
 *     author: vic
 *     time  : 18-4-3
 *     desc  : loading view
 * </pre>
 */
public class LoadingView {
    @SuppressLint("StaticFieldLeak")
    private static BxDialog loadingView = null;
    @SuppressLint("StaticFieldLeak")
    private static LoadingViewLayout loadingLayoutView = null;

    /**
     * 初始化加载的dialog</br>
     * 在activity的onDestroy()中调用cancel()，在fragment中的onDestroyView()中调用cancel()
     */
    public static void init(Context context) {
//        if (loadingLayoutView == null) {
            loadingLayoutView = new LoadingViewLayout(context);
//        }
//        if (loadingView == null)
            loadingView = new BxDialog(context).loadLayout(loadingLayoutView);
    }

    /**
     * 强制初始化加载的dialog</br>
     * 在activity的onDestroy()中调用cancel()，在fragment中的onDestroyView()中调用cancel()
     */
    public static void initForcibly(Context context) {
//        if (loadingLayoutView == null) {
            loadingLayoutView = new LoadingViewLayout(context);
//        }
        loadingView = new BxDialog(context).loadLayout(loadingLayoutView);
    }

    /**
     * 显示dialog
     */
    public static void show() {
        if (loadingLayoutView != null) loadingLayoutView.loadingBar.startProgressRun();
        if (loadingView != null && !loadingView.isShowing()) loadingView.show();
    }

    /**
     * 消失dialog
     */
    public static void dismiss() {
        if (loadingLayoutView != null) loadingLayoutView.loadingBar.stopProgressRun();
        if (loadingView != null && loadingView.isShowing()) loadingView.dismiss();
    }

    /**
     * 手机返回键按钮监听
     *
     * @param OnclickListener
     */
    public static void setKeyBackListener(BxDialog.OnclickListener OnclickListener) {
        loadingView.setKeyCodeBack(OnclickListener);
    }

    /**
     * LoadingView销毁</br>
     * 在activity的onDestroy()中调用，在fragment中的onDestroyView()中调用
     */
    public static void cancel() {
        if (loadingLayoutView != null) {
            loadingLayoutView.loadingBar.stopProgressRun();
        }
        if (loadingView != null && loadingView.isShowing()) loadingView.dismissCancel();
        loadingLayoutView = null;
        loadingView = null;
    }
}
