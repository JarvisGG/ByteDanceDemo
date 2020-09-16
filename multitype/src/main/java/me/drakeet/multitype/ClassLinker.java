package me.drakeet.multitype;

import androidx.annotation.NonNull;

public interface ClassLinker<T> {

    @NonNull
    Class<? extends ItemViewBinder<T, ?>> index(@NonNull T t);
}
