package co.bxvip.adapter.recycler.base;

import android.support.v4.util.SparseArrayCompat;


/**
 * Created by zhy on 16/6/22.
 */
public class RecyclerItemViewDelegateManager<T>
{
    SparseArrayCompat<RecyclerItemViewDelegate<T>> delegates = new SparseArrayCompat();

    public int getItemViewDelegateCount()
    {
        return delegates.size();
    }

    public RecyclerItemViewDelegateManager<T> addDelegate(RecyclerItemViewDelegate<T> delegate)
    {
        int viewType = delegates.size();
        if (delegate != null)
        {
            delegates.put(viewType, delegate);
            viewType++;
        }
        return this;
    }

    public RecyclerItemViewDelegateManager<T> addDelegate(int viewType, RecyclerItemViewDelegate<T> delegate)
    {
        if (delegates.get(viewType) != null)
        {
            throw new IllegalArgumentException(
                    "An RecyclerItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered RecyclerItemViewDelegate is "
                            + delegates.get(viewType));
        }
        delegates.put(viewType, delegate);
        return this;
    }

    public RecyclerItemViewDelegateManager<T> removeDelegate(RecyclerItemViewDelegate<T> delegate)
    {
        if (delegate == null)
        {
            throw new NullPointerException("RecyclerItemViewDelegate is null");
        }
        int indexToRemove = delegates.indexOfValue(delegate);

        if (indexToRemove >= 0)
        {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public RecyclerItemViewDelegateManager<T> removeDelegate(int itemType)
    {
        int indexToRemove = delegates.indexOfKey(itemType);

        if (indexToRemove >= 0)
        {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public int getItemViewType(T item, int position)
    {
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--)
        {
            RecyclerItemViewDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType( item, position))
            {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No RecyclerItemViewDelegate added that matches position=" + position + " in data source");
    }

    public void convert(RecyclerViewHolder holder, T item, int position)
    {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++)
        {
            RecyclerItemViewDelegate<T> delegate = delegates.valueAt(i);

            if (delegate.isForViewType( item, position))
            {
                delegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No RecyclerItemViewDelegateManager added that matches position=" + position + " in data source");
    }


    public RecyclerItemViewDelegate getItemViewDelegate(int viewType)
    {
        return delegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType)
    {
        return getItemViewDelegate(viewType).getItemViewLayoutId();
    }

    public int getItemViewType(RecyclerItemViewDelegate itemViewDelegate)
    {
        return delegates.indexOfValue(itemViewDelegate);
    }
}
