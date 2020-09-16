package bizuikit.components.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import bizuikit.components.common.IMUIShapeLayout
import bizuikit.components.common.MUIShapeHelper

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
open class MUIButton : AppCompatButton, IMUIButtonLoading {

    private val buttonHelper by lazy { MUIButtonHelper() }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        buttonHelper.init(this, context, attrs, defStyleAttr)
    }

    override fun setBackgroundColor(color: Int) {
        buttonHelper.setBgData(ColorStateList.valueOf(color))
    }

    fun setBgData(colors: ColorStateList?) {
        buttonHelper.setBgData(colors)
    }

    fun setStrokeData(width: Int, colors: ColorStateList?) {
        buttonHelper.setBgStrokeData(width, colors)
    }

    fun getStrokeWidth(): Int {
        return buttonHelper.getBgStrokeWidth()
    }

    fun setStrokeColors(colors: ColorStateList?) {
        buttonHelper.setBgStrokeColors(colors)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        buttonHelper.setBounds(0, 0, width, height)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        buttonHelper.draw(canvas)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || buttonHelper.verifyDrawable(who)
    }

    override fun showLoading() {
        buttonHelper.showLoading()
    }

    override fun stopLoading() {
        buttonHelper.stopLoading()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            setLayerType(LAYER_TYPE_NONE, null)
        } else {
            setLayerType(LAYER_TYPE_HARDWARE, null)
        }
        invalidate()
    }

}