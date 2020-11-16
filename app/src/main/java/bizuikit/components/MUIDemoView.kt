package bizuikit.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @author: yyf
 * @date: 2020/10/16
 * @desc:
 */
class MUIDemoView : View {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e("MUIDemoView", "measure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.e("MUIDemoView", "layout")
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("MUIDemoView", "draw")
        Log.e("MUIDemoView", "after measure -------> measuredWidth : $measuredWidth --- measuredHeight : $measuredHeight")
        Log.e("MUIDemoView", "after layout -------> width : $width --- height : $height")
    }
}