package bizuikit.components.bubble

import android.graphics.PointF
import android.view.View

/**
 * @author: yyf
 * @date: 2020/12/7
 * @desc:
 */
abstract class BubbleConfig {

    abstract fun getLayoutId(): Int

    open fun onBind(view: View?) {}

    open fun onDown(v: View) {}

    open fun onMove(v: View, x: Float, y: Float) {}

    open fun onUp(v: View) {}

    open fun onAttachToWindow(v: View) {}

    open fun onDetachFromWindow(v: View) {}

    open fun getStartMargin() = 0f

    open fun getEndMargin() = 0f

    open fun getTopMargin() = 0f

    open fun getBottomMargin() = 0f

    open fun getDefaultPosition(): PointF {
        return PointF(0f, 0f)
    }
}