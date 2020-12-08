package bizuikit.components.bubble

import android.view.View

/**
 * @author: yyf
 * @date: 2020/12/7
 * @desc:
 */
abstract class BubbleConfig {

    abstract fun getLayoutId(): Int

    abstract fun onBind(view: View?)

    open fun onDown() {}

    open fun onMove(x: Float, y: Float) {}

    open fun onUp() {}

    open fun onAttachToWindow() {}

    open fun onDetachFromWindow() {}
}