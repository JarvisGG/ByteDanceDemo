package me.drakeet.multitype;

import androidx.annotation.NonNull;


import me.drakeet.multitype.tool.MultiListImpl;

import java.util.List;

import static me.drakeet.multitype.Preconditions.checkNotNull;


public class MultiTypeAdapter extends BaseMultiTypeAdapter {

    private @NonNull
    MultiListImpl mItemList;

    public MultiTypeAdapter() {
        this(new MultiListImpl());
    }

    public MultiTypeAdapter(@NonNull MultiListImpl multiList) {
        this(multiList, new MultiTypePool());
    }

    public MultiTypeAdapter(@NonNull MultiListImpl multiList, @NonNull TypePool pool) {
        super(multiList, pool);
        this.mItemList = multiList;
    }

    /**
     * 设置 Adapter 的数据List
     * @param items
     */
    public void setItems(@NonNull List<?> items) {
        checkNotNull(items);
        mItemList.setList(items);
    }

}