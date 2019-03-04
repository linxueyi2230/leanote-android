package co.bxvip.android.commonlib.rotationview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 带旋转动画的加载图片
 *
 * @Author yyx on 2016/12/12.
 */
public class RotationView extends ImageView {

    public ObjectAnimator getAnimator() {
        return animator;
    }

    public void setAnimator(ObjectAnimator animator) {
        this.animator = animator;
    }

    public ObjectAnimator animator;

    public RotationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RotationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotationView(Context context) {
        this(context, null);
    }

    public void start() {

        Drawable drawable = getDrawable();
        if (drawable != null) {

            if (animator != null) {
                animator.cancel();
            }

            animator = ObjectAnimator.ofFloat(this, "rotation", 0, 360f);
            animator.setDuration(200);
            animator.setRepeatCount(1);
            animator.start();
        }
    }
}
