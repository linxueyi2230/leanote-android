package android.support.v7.widget.builder;

import android.content.res.Resources;

/**
 * @author vic Zhou
 * @time 2018-3-27 3:08
 */

public class RecycleViewDimen {
    // dimen 资源
    public static int fastscroll_default_thickness = 8;
    public static int fastscroll_margin = 0;
    public static int fastscroll_minimum_range = 50;
    public static int item_touch_helper_max_drag_scroll_per_frame = 20;
    public static int item_touch_helper_swipe_escape_max_velocity = 800;
    public static int item_touch_helper_swipe_escape_velocity = 120;
    // id
    public static int item_touch_helper_previous_elevation = 0x7f06000d;

    public static int dip2Px(Resources resources, int dp) {
        return (int) (dp * resources.getDisplayMetrics().density + 0.5);
    }
}
