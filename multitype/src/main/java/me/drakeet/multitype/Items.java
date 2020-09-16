package me.drakeet.multitype;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 方便多类型使用的ArrayList, 一般情况一个 Items 就够用
 *
 */
public class Items extends ArrayList<Object> {

    public Items() {
        super();
    }

    public Items(int initialCapacity) {
        super(initialCapacity);
    }

    public Items(@NonNull Collection<?> c) {
        super(c);
    }
}
