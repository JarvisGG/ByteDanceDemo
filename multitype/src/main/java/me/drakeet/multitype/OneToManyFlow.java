package me.drakeet.multitype;

import androidx.annotation.NonNull;

public interface OneToManyFlow<T> {

    /**
     * 一个class 对应 多个 ItemViewBinder
     * @param itemViewBinder
     * @return
     */
    OneToManyEndpoint<T> to(@NonNull ItemViewBinder<T, ?>... itemViewBinder);
}
