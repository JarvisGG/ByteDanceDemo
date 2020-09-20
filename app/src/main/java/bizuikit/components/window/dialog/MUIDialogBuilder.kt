package bizuikit.components.window.dialog

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import bizuikit.utils.inflate
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
open class MUIDialogBuilder(
    private val activity: Activity
) {

    private lateinit var dialog: MUIDialog

    private var radius: Int = 0
    private var shadowElevation = 0
    private var shadowAlpha = 0f
    private var shadowColor = Color.BLACK

    var container: LinearLayout? = null
    var containerId = -1


    /**
     * 弹窗圆角
     */
    fun setRadius(radius: Int): MUIDialogBuilder {
        this.radius = radius
        return this
    }

    /**
     * 阴影延展
     */
    fun setShadowElevation(shadowElevation: Int): MUIDialogBuilder {
        this.shadowElevation = shadowElevation
        return this
    }

    /**
     * 阴影颜色
     */
    fun setShadowColor(shadowColor: Int): MUIDialogBuilder {
        this.shadowColor = shadowColor
        return this
    }

    /**
     * 阴影透明度
     */
    fun setShadowAlpha(shadowAlpha: Float): MUIDialogBuilder {
        this.shadowAlpha = shadowAlpha
        return this
    }

    fun setContainer(
        container: LinearLayout, lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    ): MUIDialogBuilder {
        if (containerId != -1) {
            throw RuntimeException("containerId not -1")
        }
        this.container = container.apply {
            layoutParams = lp
        }
        return this
    }

    fun setContainerId(containerId: Int): MUIDialogBuilder {
        if (container != null) {
            throw RuntimeException("container not null")
        }
        this.containerId = containerId
        return this
    }

    open fun build(): MUIDialog {
        return build(R.style.MUIBaseDialog)
    }

    open fun build(defStyle: Int): MUIDialog {
        dialog = MUIDialog(activity, defStyle)
        container = dialog.container.inflate(containerId, attachToRoot = true)
        buildContainer()
        dialog.setRadius(radius)
        dialog.setShadowElevation(shadowElevation)
        dialog.setShadowAlpha(shadowAlpha)
        dialog.setShadowColor(shadowColor)
        return dialog
    }

    open fun buildContainer() : LinearLayout {
        addContainer(container)
        return container!!
    }

    open fun addContainer(root: LinearLayout?) {}



}