package me.drakeet.multitype;

import androidx.annotation.NonNull;

import java.util.Arrays;

class ClassLinkerWrapper<T> implements Linker<T>{

    private ClassLinker<T> classLinker;

    private ItemViewBinder<T, ?>[] binders;

    public ClassLinkerWrapper(ClassLinker<T> classLinker,
                              ItemViewBinder<T, ?>[] binders) {
        this.classLinker = classLinker;
        this.binders = binders;

    }

    public static <T> Linker<T> wrap(ClassLinker<T> classLinker,
                                     ItemViewBinder<T, ?>[] viewBinders) {
        return new ClassLinkerWrapper<>(classLinker, viewBinders);
    }


    @Override
    public int index(@NonNull T t) {
        //通过用户自定义的 classLinker 做 ItemViewBinder 的映射
        Class<? extends ItemViewBinder<T, ?>> binderClass = classLinker.index(t);
        for (int i = 0; i < binders.length; i++) {
            if (binders[i].getClass().equals(binderClass)) {
                return i;
            }
        }

        throw new IndexOutOfBoundsException(
                String.format("%s is out of your registered binders'(%s) bounds.",
                        binderClass.getName(), Arrays.toString(binders))
        );
    }
}
