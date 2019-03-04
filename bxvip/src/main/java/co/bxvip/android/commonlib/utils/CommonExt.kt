package co.bxvip.android.commonlib.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


/**
 * <pre>
 *     author: vic
 *     time  : 18-1-23
 *     desc  : common 扩展
 * </pre>
 */

object CommonExt {
    fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener { block(it as T) }

    fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }

    fun View.visiable() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    }

    inline fun View.visiableIf(block: () -> Boolean) {
        if (visibility != View.VISIBLE && block()) {
            visibility = View.VISIBLE
        }
    }

    fun View.invisiable() {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
    }

    inline fun View.invisiableIf(block: () -> Boolean) {
        if (visibility != View.INVISIBLE && block()) {
            visibility = View.INVISIBLE
        }
    }

    fun View.gone() {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
    }

    inline fun View.goneIf(block: () -> Boolean) {
        if (visibility != View.GONE && block()) {
            visibility = View.GONE
        }
    }


    fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
    }


    var EditText.value: String
        get() = text.toString()
        set(value) = setText(value)
}