package co.bxvip.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Window;

import java.lang.reflect.Field;

/**
 * @author vic Zhou
 * @time 2018-3-31 22:30
 * @des 屏幕工具类 获取
 */

public class DisplayUtils {
    public static int getStatusBarHeight(Context context, int defaultHeightDip) {
        int res = 0;
        if (context instanceof Activity) {
            try {
                Window window = ((Activity) context).getWindow();
                Rect rectangle = new Rect();
                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                res = rectangle.top;
            } catch (Exception e) {

            }
        }
        if (res == 0) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;//默认为38，貌似大部分是这样的
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                res = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (res == 0) {
            res = dip2px(context, defaultHeightDip);
        }
        return res;
    }

    public static int dip2px(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
