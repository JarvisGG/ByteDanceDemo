package me.drakeet.multitype;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import me.drakeet.multitype.tool.MultiList;

import java.util.List;

import static me.drakeet.multitype.Preconditions.checkNotNull;


public class BaseMultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "MultiTypeAdapter";

    private @NonNull
    MultiList items;

    private @NonNull
    TypePool typePool;

    public BaseMultiTypeAdapter(@NonNull MultiList items, @NonNull TypePool pool) {
        checkNotNull(items);
        checkNotNull(pool);
        this.items = items;
        this.typePool = pool;
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull ItemViewBinder<T, ?> binder) {
        checkNotNull(clazz);
        checkNotNull(binder);
        checkAndRemoveAllTypesIfNeed(clazz);
        typePool.register(clazz, binder, new DefaultLinker<T>());
        binder.adapter = this;
    }


    @SuppressWarnings("unchecked")
    public <T> OneToManyFlow<T> register(@NonNull Class<? extends T> clazz) {
        checkAndRemoveAllTypesIfNeed(clazz);
        return new OnToManyBuilder(this, clazz);
    }

    /**
     * 内部调用方法，注册 ViewBinder 和 Linker
     * @param clazz
     * @param binder
     * @param linker
     * @param <T>
     */
    <T> void registerWithLinker(@NonNull Class<? extends T> clazz,
                                @NonNull ItemViewBinder<T, ?> binder,
                                @NonNull Linker<T> linker) {
        typePool.register(clazz, binder, linker);
    }

    /**
     * 注册新的ItemViewBinder前，先反注册掉老的
     * @param clazz
     */
    private void checkAndRemoveAllTypesIfNeed(@NonNull Class<?> clazz) {
        if (typePool.unregister(clazz)) {
            Log.w(TAG, "You have registered the " + clazz.getSimpleName() + " type. " +
                    "It will override the original binder(s).");
        }
    }

    /**
     * 类型缓存池
     * @param typePool
     */
    public void setTypePool(@NonNull TypePool typePool) {
        checkNotNull(typePool);
        this.typePool = typePool;
    }


    public @NonNull
    TypePool getTypePool() {
        return typePool;
    }


    @Override
    public final int getItemViewType(int position) {
        Object item = getItemObject(position);
        return indexInTypesOf(item);
    }

    /**
     * 根据 Class 名字计算 ViewType 的index
     * @param item
     * @return
     * @throws BinderNotFoundException
     */
    int indexInTypesOf(@NonNull Object item) throws BinderNotFoundException {
        int index = typePool.firstIndexOf(item.getClass());
        if (index != -1) {
            // 从 typePool 中的 class 映射找到，再匹配 Linker 中 ItemViewBinder 的 index
            @SuppressWarnings("unchecked")
            Linker<Object> linker = (Linker<Object>) typePool.getLinker(index);
            return index + linker.index(item);
        }
        throw new BinderNotFoundException(item.getClass());
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int indexViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemViewBinder<?, ?> itemViewBinder = typePool.getItemViewBinder(indexViewType);
        itemViewBinder.adapter = this;
        return itemViewBinder.onCreateViewHolder(inflater, parent);
    }

    /**
     * 不实现还报错，实现又是空的，留着吧
     * @param holder
     * @param position
     */
    @Override @Deprecated
    public final void onBindViewHolder(ViewHolder holder, int position) {
        throw new IllegalAccessError("You should not call this method. " +
                "Call RecyclerView.Adapter#onBindViewHolder(holder, position, payloads) instead.");
    }

    @Override @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Object item = getItemObject(position);
        int itemViewType = holder.getItemViewType();
        ItemViewBinder itemViewBinder = typePool.getItemViewBinder(itemViewType);
        itemViewBinder.onBindViewHolder(holder, item, payloads, position, items.size());
    }

    /**
     * 独立出来主要为了供子类 override
     * @param position
     * @return
     */
    protected Object getItemObject(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override @SuppressWarnings("unchecked")
    public long getItemId(int position) {
        Object item = items.get(position);
        int itemViewType = getItemViewType(position);
        ItemViewBinder itemViewBinder = typePool.getItemViewBinder(itemViewType);
        return itemViewBinder.getItemId(item);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        getRawBinderByViewHolder(holder).onViewAttachedToWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        getRawBinderByViewHolder(holder).onViewDetachedFromWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        return getRawBinderByViewHolder(holder).onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public void onViewRecycled(@NonNull ViewHolder holder) {
        getRawBinderByViewHolder(holder).onViewRecycled(holder);
    }

    @NonNull
    private ItemViewBinder getRawBinderByViewHolder(@NonNull ViewHolder holder) {
        return typePool.getItemViewBinder(holder.getItemViewType());
    }



}