package bizuikit.components.bubble

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * @author: yyf
 * @date: 2020/11/18
 * @desc: 为了解决 一加 电池优化，会无视 wm 添加的 ViewGroup，强行对 app 降级
 */
class FakeView : View, OnComputeInternalInsetsListener.UpdateTouchRect {
    private val invocationHandler = OnComputeInternalInsetsListener()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        invocationHandler.setTouchRegionRect(this)
    }

    override fun onUpdateTouchRect(): Rect {
        return Rect(0, 0, 0, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ReflectionUtils.addOnComputeInternalInsetsListener(
            viewTreeObserver,
            invocationHandler.listener
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ReflectionUtils.removeOnComputeInternalInsetsListener(viewTreeObserver)
    }
}