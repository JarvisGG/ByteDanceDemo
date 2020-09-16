package me.drakeet.multitype;

import androidx.annotation.NonNull;

public class OnToManyBuilder<T> implements OneToManyFlow<T>, OneToManyEndpoint<T>{

    private BaseMultiTypeAdapter adapter;

    private Class<T> clazz;

    private ItemViewBinder<T, ?>[] viewBinders;

    OnToManyBuilder(BaseMultiTypeAdapter adapter, Class<T> clazz) {
        this.adapter = adapter;
        this.clazz = clazz;
    }

    @Override
    public OneToManyEndpoint<T> to(@NonNull ItemViewBinder<T, ?>... itemViewBinder) {
        this.viewBinders = itemViewBinder;
        return this;
    }


    @Override
    public void withLinker(@NonNull Linker<T> linker) {
        doRegister(linker);
    }

    @Override
    public void withClassLinker(@NonNull ClassLinker<T> classLinker) {
        doRegister(ClassLinkerWrapper.wrap(classLinker, viewBinders));
    }

    /**
     * 注册
     * @param linker
     */
    private void doRegister(@NonNull Linker<T> linker) {
        for (ItemViewBinder<T, ?> viewBinder : viewBinders) {
            adapter.registerWithLinker(clazz, viewBinder, linker);
        }
    }


}
