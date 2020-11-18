package bizuikit.components.window.bottomsheet

import android.app.Activity
import android.graphics.Color
import android.widget.LinearLayout
import bizuikit.utils.inflate
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
open class MUIBottomSheetBuilder(
    private val activity: Activity
) {

    open lateinit var bottomSheet: MUIBottomSheet

    private var radius: Int = 0
    private var shadowElevation = 0
    private var shadowAlpha = 0f
    private var shadowColor = Color.BLACK

    open var container: LinearLayout? = null
    open var containerId = -1

    /**
     * 弹窗圆角
     */
    fun setRadius(radius: Int): MUIBottomSheetBuilder {
        this.radius = radius
        return this
    }

    /**
     * 阴影延展
     */
    fun setShadowElevation(shadowElevation: Int): MUIBottomSheetBuilder {
        this.shadowElevation = shadowElevation
        return this
    }

    /**
     * 阴影颜色
     */
    fun setShadowColor(shadowColor: Int): MUIBottomSheetBuilder {
        this.shadowColor = shadowColor
        return this
    }

    /**
     * 阴影透明度
     */
    fun setShadowAlpha(shadowAlpha: Float): MUIBottomSheetBuilder {
        this.shadowAlpha = shadowAlpha
        return this
    }

    fun setContainer(
        container: LinearLayout, lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    ): MUIBottomSheetBuilder {
        if (containerId != -1) {
            throw RuntimeException("containerId not -1")
        }
        this.container = container.apply {
            layoutParams = lp
        }
        return this
    }

    fun setContainerId(containerId: Int): MUIBottomSheetBuilder {
        if (container != null) {
            throw RuntimeException("container not null")
        }
        this.containerId = containerId
        return this
    }

    fun build(): MUIBottomSheet {
        return build(R.style.MUIBaseDialog)
    }

    fun build(defStyle: Int): MUIBottomSheet {
        bottomSheet = MUIBottomSheet(activity, defStyle)
        container = bottomSheet.container.inflate(containerId, attachToRoot = true)
        buildContainer()
        bottomSheet.setRadius(radius)
        bottomSheet.setShadowElevation(shadowElevation)
        bottomSheet.setShadowAlpha(shadowAlpha)
        bottomSheet.setShadowColor(shadowColor)
        return bottomSheet
    }

    open fun buildContainer() : LinearLayout {
        addContainer(container)
        return container!!
    }

    open fun addContainer(root: LinearLayout?) {}

}