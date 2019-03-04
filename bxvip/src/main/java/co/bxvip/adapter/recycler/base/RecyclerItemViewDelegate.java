package co.bxvip.adapter.recycler.base;

public interface RecyclerItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(RecyclerViewHolder holder, T t, int position);

}
