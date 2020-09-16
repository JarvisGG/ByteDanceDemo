package me.drakeet.multitype;

import androidx.annotation.NonNull;

public interface TypePool {

  /**
   * 使用 class 名字和 ItemViewBinder 绑定
   * @param clazz
   * @param binder
   * @param <T>
   */
  <T> void register(
          @NonNull Class<? extends T> clazz,
          @NonNull ItemViewBinder<T, ?> binder,
          @NonNull Linker<T> linker);

  /**
   * 反注册 clazz
   * @param clazz
   * @return
   */
  boolean unregister(@NonNull Class<?> clazz);

  /**
   * 注册的 ItemViewBinder 类型个数
   * @return
   */
  int size();


  /**
   * 通过 Class 的名字，计算出对应的 ViewType index
   * @param clazz
   * @return
   */
  int firstIndexOf(@NonNull Class<?> clazz);

  @NonNull
  Class<?> getClass(int index);

  @NonNull
  ItemViewBinder<?, ?> getItemViewBinder(int index);

  /**
   * 取对应 index 的Linker, 因为会出现 一对多 的ViewBinder，所以需要Linker
   * @param index
   * @return
   */
  Linker<?> getLinker(int index);

}
