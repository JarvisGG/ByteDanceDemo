package me.drakeet.multitype;

import androidx.annotation.NonNull;

/**
 * ItemViewBinder 没找到异常，必须先注册后操作
 */
class BinderNotFoundException extends RuntimeException {

  BinderNotFoundException(@NonNull Class<?> clazz) {
    super("Do you have registered {className}.class to the binder in the adapter/pool?"
        .replace("{className}", clazz.getSimpleName()));
  }
}