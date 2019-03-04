package android.support.v7.widget.builder;

import android.support.annotation.DrawableRes;

/**
 * @author vic Zhou
 * @time 2018-3-27 2:04
 * 属性的构造器
 */

public class RecyclerBuilder {

    private String layoutManager = "android.support.v7.widget.LinearLayoutManager";
    private int descendantFocusability = -1;
    private boolean fastScrollEnabled;
    @DrawableRes
    private int fastScrollVerticalThumbDrawable;
    @DrawableRes
    private int fastScrollVerticalTrackDrawable;
    @DrawableRes
    private int fastScrollHorizontalThumbDrawable;
    @DrawableRes
    private int fastScrollHorizontalTrackDrawable;

    private RecyclerBuilder() {
    }

    public RecyclerBuilder layoutManager(String layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public RecyclerBuilder descendantFocusability(int descendantFocusability) {
        this.descendantFocusability = descendantFocusability;
        return this;
    }

    public RecyclerBuilder fastScrollEnabled(boolean fastScrollEnabled) {
        this.fastScrollEnabled = fastScrollEnabled;
        return this;
    }

    public RecyclerBuilder fastScrollVerticalThumbDrawable(@DrawableRes int fastScrollVerticalThumbDrawable) {
        this.fastScrollVerticalThumbDrawable = fastScrollVerticalThumbDrawable;
        return this;
    }

    public RecyclerBuilder fastScrollVerticalTrackDrawable(@DrawableRes int fastScrollVerticalTrackDrawable) {
        this.fastScrollVerticalTrackDrawable = fastScrollVerticalTrackDrawable;
        return this;
    }

    public RecyclerBuilder fastScrollHorizontalThumbDrawable(@DrawableRes int fastScrollHorizontalThumbDrawable) {
        this.fastScrollHorizontalThumbDrawable = fastScrollHorizontalThumbDrawable;
        return this;
    }

    public RecyclerBuilder fastScrollHorizontalTrackDrawable(@DrawableRes int fastScrollHorizontalTrackDrawable) {
        this.fastScrollHorizontalTrackDrawable = fastScrollHorizontalTrackDrawable;
        return this;
    }

    public static RecyclerBuilder newBuild() {
        return new RecyclerBuilder();
    }

    public String getLayoutManager() {
        return layoutManager;
    }

    public int getDescendantFocusability() {
        return descendantFocusability;
    }

    public boolean isFastScrollEnabled() {
        return fastScrollEnabled;
    }

    public int getFastScrollVerticalThumbDrawable() {
        return fastScrollVerticalThumbDrawable;
    }

    public int getFastScrollVerticalTrackDrawable() {
        return fastScrollVerticalTrackDrawable;
    }

    public int getFastScrollHorizontalThumbDrawable() {
        return fastScrollHorizontalThumbDrawable;
    }

    public int getFastScrollHorizontalTrackDrawable() {
        return fastScrollHorizontalTrackDrawable;
    }
}
