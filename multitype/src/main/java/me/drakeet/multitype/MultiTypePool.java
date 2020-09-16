package me.drakeet.multitype;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static me.drakeet.multitype.Preconditions.checkNotNull;

public class MultiTypePool implements TypePool {

  /**
   * class 类型集合
   */
  private final @NonNull
  List<Class<?>> classes;

  /**
   * ItemViewBinder 集合
   */
  private final @NonNull
  List<ItemViewBinder<?, ?>> binders;

  /**
   * 用于处理 一对多
   */
  private final @NonNull
  List<Linker<?>> linkers;


  public MultiTypePool() {
    this.classes = new ArrayList<>();
    this.binders = new ArrayList<>();
    this.linkers = new ArrayList<>();
  }


  /**
   * 初始化 List 的容量
   * @param initialCapacity
   */
  public MultiTypePool(int initialCapacity) {
    this.classes = new ArrayList<>(initialCapacity);
    this.binders = new ArrayList<>(initialCapacity);
    this.linkers = new ArrayList<>(initialCapacity);
  }


  public MultiTypePool(
      @NonNull List<Class<?>> classes,
      @NonNull List<ItemViewBinder<?, ?>> binders,
      @NonNull List<Linker<?>> linkers) {
    checkNotNull(classes);
    checkNotNull(binders);
    this.classes = classes;
    this.binders = binders;
    this.linkers = linkers;
  }


  @Override
  public <T> void register(
      @NonNull Class<? extends T> clazz,
      @NonNull ItemViewBinder<T, ?> binder,
      @NonNull Linker<T> linker) {
    checkNotNull(clazz);
    checkNotNull(binder);
    classes.add(clazz);
    binders.add(binder);
    linkers.add(linker);
  }


  @Override
  public boolean unregister(@NonNull Class<?> clazz) {
    checkNotNull(clazz);
    boolean removed = false;
    while (true) {
      int index = classes.indexOf(clazz);
      if (index != -1) {
        classes.remove(index);
        binders.remove(index);
        linkers.remove(index);
        removed = true;
      } else {
        break;
      }
    }
    return removed;
  }


  @Override
  public int size() {
    // classes、binders、linkers size相等，所以返回哪一个都一样？
    return classes.size();
  }


  @Override
  public int firstIndexOf(@NonNull final Class<?> clazz) {
    checkNotNull(clazz);
    int index = classes.indexOf(clazz);
    if (index != -1) {
      //在 classes 集合中找到此类，直接返回
      return index;
    }

    for (int i = 0; i < classes.size(); i++) {
      if (classes.get(i).isAssignableFrom(clazz)) {
        // clazz 是 classes.get(i) 的子类或者子接口， clazz 是子类才返回true
        return i;
      }
    }
    return -1;
  }


  @Override
  public @NonNull
  Class<?> getClass(int index) {
    return classes.get(index);
  }


  @Override
  public @NonNull
  ItemViewBinder<?, ?> getItemViewBinder(int index) {
    return binders.get(index);
  }

  @Override
  public Linker<?> getLinker(int index) {
    return linkers.get(index);
  }



}
