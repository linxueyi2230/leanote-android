package co.bxvip.adapter.recycler.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <pre>
 *     author: vic
 *     time  : 18-4-3
 *     desc  : ${END}
 * </pre>
 */
public interface OnItemClick {
    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

    boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
}
