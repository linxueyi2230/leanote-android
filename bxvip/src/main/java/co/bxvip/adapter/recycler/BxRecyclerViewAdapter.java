package co.bxvip.adapter.recycler;

import android.content.Context;

import co.bxvip.adapter.recycler.base.RecyclerItemViewDelegate;
import co.bxvip.adapter.recycler.base.RecyclerViewHolder;

import android.view.LayoutInflater;


import java.util.List;


public abstract class BxRecyclerViewAdapter<T> extends RecyclerMultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public BxRecyclerViewAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mData = datas;

        addItemViewDelegate(new RecyclerItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(RecyclerViewHolder holder, T t, int position) {
                BxRecyclerViewAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(RecyclerViewHolder holder, T t, int position);


}
