@file:JvmName("UICaller")
@file:JvmMultifileClass

package bizuikit.components.utils

import android.os.Handler
import android.os.Looper


/**
 * 是否是UI线程
 * */
fun isUIThread(): Boolean {
    return Thread.currentThread() == Looper.getMainLooper().thread
}

/**
 * 轻量级UI线程执行器，提供给零散的轻度的主线程执行需求  如果你的执行位置强制持有某些复杂对象，有延迟耗时的操作，不建议使用
 * */
fun runOnUIThread(runnable: Runnable) {
    if (isUIThread()) {
        runnable.run()
    } else {
        postToLooper(runnable)
    }
}

private fun postToLooper(runnable: Runnable) {
    runnable.let {
        val handler = Handler(Looper.getMainLooper())
        handler.postAtFrontOfQueue {
            it.run()
        }
    }
}