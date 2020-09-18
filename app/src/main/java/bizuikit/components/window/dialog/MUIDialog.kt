package bizuikit.components.window.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import bizuikit.components.common.IMUIShapeLayout
import bizuikit.components.layout.MUILayout
import bizuikit.components.window.MUIBaseDialog
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
class MUIDialog : MUIBaseDialog, IMUIShapeLayout {

    private val root: ViewGroup

    private val container: MUILayout

    constructor(context: Context) : this(context, R.style.MUIBaseDialog_Dialog)

    @SuppressLint("ClickableViewAccessibility")
    constructor(context: Context, style: Int) : super(context, style) {
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        root = layoutInflater.inflate(R.layout.mui_dialog, null, false) as FrameLayout
        container = root.findViewById(R.id.ml_container)

        super.setContentView(root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun addView(view: View?, lp: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )) {
        container.addView(view, lp)
    }

    override fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float) {
        container.setRadiusAndShadow(radius, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowAlpha: Float
    ) {
        container.setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowColor: Int,
        shadowAlpha: Float
    ) {
        container.setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowColor, shadowAlpha)
    }

    override fun setShadowElevation(elevation: Int) {
        container.setShadowElevation(elevation)
    }

    override fun setShadowAlpha(shadowAlpha: Float) {
        container.setShadowAlpha(shadowAlpha)
    }

    override fun setShadowColor(shadowColor: Int) {
        container.setShadowColor(shadowColor)
    }

    override fun setRadius(radius: Int) {
        container.setRadius(radius)
    }

    override fun setRadius(radius: Int, hideRadiusSide: Int) {
        container.setRadius(radius, hideRadiusSide)
    }


}