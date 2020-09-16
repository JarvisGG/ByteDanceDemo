package me.drakeet.multitype;

import androidx.annotation.NonNull;
import me.drakeet.multitype.tool.MultiListImpl;
import java.util.List;

public class MultiListTypeAdapter extends BaseMultiTypeAdapter {

    private @NonNull
    MultiListImpl mDataList;

    public MultiListTypeAdapter() {
        this(new MultiListImpl());
    }

    public MultiListTypeAdapter(@NonNull MultiListImpl items) {
        this(items, new MultiTypePool());
    }

    public MultiListTypeAdapter(@NonNull MultiListImpl items, @NonNull TypePool pool) {
        super(items, pool);
        this.mDataList = items;
    }

    public void addList(List<?> list) {
        mDataList.addList(list);
    }

    public void addList(List<?> list, MultiListImpl.IListDisplayStrategy strategy) {
        mDataList.addList(list, strategy);
    }

}
