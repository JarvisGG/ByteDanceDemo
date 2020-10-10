package bizuikit.components.layout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import bizuikit.components.common.IMUIShapeLayout
import bizuikit.components.common.MUIShapeHelper

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
open class MUILayout : LinearLayout, IMUIShapeLayout {

    private val helper by lazy {
        MUIShapeHelper()
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        helper.init(this, context, attrs, defStyleAttr, defStyleRes)
    }

    override fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float) {
        helper.setRadiusAndShadow(radius, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowAlpha: Float
    ) {
        helper.setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowColor: Int,
        shadowAlpha: Float
    ) {
        helper.setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowColor, shadowAlpha)
    }

    override fun setShadowElevation(elevation: Int) {
        helper.setShadowElevation(elevation)
    }

    override fun setShadowAlpha(shadowAlpha: Float) {
        helper.setShadowAlpha(shadowAlpha)
    }

    override fun setShadowColor(shadowColor: Int) {
        helper.setShadowColor(shadowColor)
    }

    override fun setRadius(radius: Int) {
        helper.setRadius(radius)
    }

    override fun setRadius(radius: Int, @IMUIShapeLayout.HideRadiusSide hideRadiusSide: Int) {
        helper.setRadius(radius, hideRadiusSide)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        helper.dispatchRoundBorderDraw(canvas)
    }
}