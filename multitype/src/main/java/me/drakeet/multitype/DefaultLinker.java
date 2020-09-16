package me.drakeet.multitype;

import androidx.annotation.NonNull;

/**
 * 每个一对一的 item 和 viewBinder，默认的Linker
 * @param <T>
 */
public class DefaultLinker<T> implements Linker<T> {

    @Override
    public int index(@NonNull T t) {
        return 0;
    }

}
