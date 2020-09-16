package me.drakeet.multitype;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView.ViewHolder 的抽象包装类
 * @param <T> 数据泛型
 * @param <VH> ViewHolder
 */
public abstract class ItemViewBinder<T, VH extends RecyclerView.ViewHolder> {

    /* internal */ BaseMultiTypeAdapter adapter;

    @NonNull
    protected abstract VH onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item, int position, int itemSize);

    protected void onBindViewHolder(
            @NonNull VH holder, @NonNull T item, @NonNull List<Object> payloads, int position, int itemSize) {
        onBindViewHolder(holder, item, position, itemSize);
    }



    /**
     * ItemId
     * @param item
     * @return
     */
    public long getItemId(T item) {
        return RecyclerView.NO_ID;
    }

    /**
     * 添加到 window
     * @param holder
     */
    protected void onViewAttachedToWindow(@NonNull VH holder) {}

    /**
     * 从 Window 移除
     * @param holder
     */
    protected void onViewDetachedFromWindow(@NonNull VH holder) {}



    public void onViewRecycled(VH holder) {

    }

    /**
     * 回收失败，返回 false，表示再次检查，返回 true,代表忽略错误依然回收
     * @param holder
     * @return
     */
    public boolean onFailedToRecycleView(VH holder) {
        return false;
    }



}
