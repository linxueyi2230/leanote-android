package co.bxvip.adapter.recycler;

import android.content.Context;

import co.bxvip.adapter.recycler.base.OnItemClick;
import co.bxvip.adapter.recycler.base.OnItemClickListener;
import co.bxvip.adapter.recycler.base.RecyclerItemViewDelegate;
import co.bxvip.adapter.recycler.base.RecyclerItemViewDelegateManager;
import co.bxvip.adapter.recycler.base.RecyclerViewHolder;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public class RecyclerMultiItemTypeAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected Context mContext;
    protected List<T> mData;

    protected RecyclerItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClick mOnItemClickListener;


    public RecyclerMultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mData = datas;
        mItemViewDelegateManager = new RecyclerItemViewDelegateManager();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mData.get(position), position);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        RecyclerViewHolder holder = RecyclerViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(RecyclerViewHolder holder, View itemView) {

    }

    public void convert(RecyclerViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final RecyclerViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = mData.size();
        return itemCount;
    }


    public List<T> getData() {
        return mData;
    }

    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    public void setNewData(@Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        notifyDataSetChanged();
    }


    /**
     * insert  a item associated with the specified position of adapter
     *
     * @param position
     * @param item
     * @deprecated use {@link #addData(int, Object)} instead
     */
    @Deprecated
    public void add(@IntRange(from = 0) int position, @NonNull T item) {
        addData(position, item);
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    public void addData(@IntRange(from = 0) int position, @NonNull T data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * add one new data
     */
    public void addData(@NonNull T data) {
        mData.add(data);
        notifyItemInserted(mData.size());
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(@IntRange(from = 0) int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * change data
     */
    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        mData.set(index, data);
        notifyItemChanged(index);
    }

    /**
     * add new data in to certain location
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    public void addData(@IntRange(from = 0) int position, @NonNull Collection<? extends T> newData) {
        mData.addAll(position, newData);
        notifyItemRangeInserted(position, newData.size());
    }

    /**
     * add new data to the end of mData
     *
     * @param newData the new data collection
     */
    public void addData(@NonNull Collection<? extends T> newData) {
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size(), newData.size());
    }

    public RecyclerMultiItemTypeAdapter addItemViewDelegate(RecyclerItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public RecyclerMultiItemTypeAdapter addItemViewDelegate(int viewType, RecyclerItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public void setOnItemClickListener(OnItemClick onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
