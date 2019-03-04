package co.bxvip.adapter.abslist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * @author vic Zhou
 * @time 2018-3-27 0:59
 * @des 折叠 listview
 */

public abstract class BxExpandableAdapter<T> extends BaseExpandableListAdapter {
    protected Context context;
    protected List<T> list;

    public BxExpandableAdapter(Context context, List<T> list) {
        this.list = list;
        this.context = context;
    }
    public void setDatas(List<T> mDatas) {
        this.list = mDatas;
    }

    public List<T> getDatas() {
        return list;
    }
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getGroupType(int groupPosition) {
        return super.getGroupType(groupPosition);
    }

    public abstract int getChildrenCount(int groupPosition);

    public T getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    public abstract Object getChild(int groupPosition, int childPosition);

    public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    public abstract boolean isChildSelectable(int groupPosition, int childPosition);
}
