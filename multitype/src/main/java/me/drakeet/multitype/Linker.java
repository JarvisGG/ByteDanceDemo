package me.drakeet.multitype;

import androidx.annotation.NonNull;

/**
 * 连接 items 和 itemViewBinders
 * @param <T>
 * @throw
 */
public interface Linker<T> {

    int index(@NonNull T t);
}
