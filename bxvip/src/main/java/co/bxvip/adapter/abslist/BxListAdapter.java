package co.bxvip.adapter.abslist;

import android.content.Context;
import co.bxvip.adapter.abslist.base.ItemViewDelegate;
import co.bxvip.adapter.abslist.base.ViewHolder;

import java.util.List;

/**
 * <pre>
 *     author: vic
 *     time  : 18-3-26
 *     desc  : https://github.com/hongyangAndroid/baseAdapter // 专门个人使用，感谢作者
 * </pre>
 */
public abstract class BxListAdapter<T> extends MultiItemListTypeAdapter<T> {

    public BxListAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                BxListAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

}