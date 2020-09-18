package bizuikit.components.window.bottomsheet

import android.app.Activity
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import bizuikit.components.bar.MUITitleBar
import bizuikit.utils.getToolBarHeight
import bizuikit.utils.inflate
import bizuikit.utils.sp2px
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
open class MUIBottomSheetBuilder(
    private val activity: Activity
) {

    private lateinit var bottomSheet: MUIBottomSheet

    private var title: String = ""

    private var radius: Int = 0
    private var shadowElevation = 0
    private var shadowAlpha = 0f
    private var shadowColor = Color.BLACK

    private var container: View? = null
    private var containerId = -1

    /**
     * 标题
     */
    fun setTitle(title: String): MUIBottomSheetBuilder {
        this.title = title
        return this
    }

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
        container: View, lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
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
        bottomSheet.addView(buildContainer())
        bottomSheet.setRadius(radius)
        bottomSheet.setShadowElevation(shadowElevation)
        bottomSheet.setShadowAlpha(shadowAlpha)
        bottomSheet.setShadowColor(shadowColor)
        return bottomSheet
    }

    open fun buildContainer() : View {
        val root = LinearLayout(activity)
        root.orientation = LinearLayout.VERTICAL
        addTitle(root)
        addContainer(root)
        return root
    }

    /**
     * 添加标题
     */
    open fun addTitle(root: LinearLayout) {
        if (!TextUtils.isEmpty(title)) {
            val titleBar = MUITitleBar(activity)
            titleBar.title = title
            titleBar.setBackgroundColor(Color.WHITE)
            root.addView(titleBar, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                activity.getToolBarHeight())
            )
            titleBar.textSize = 15.sp2px

        }
    }

    /**
     * 添加 Container
     */
    open fun addContainer(root: LinearLayout) {
        if (container != null) {
            root.addView(container)
        } else if (containerId != -1) {
            container = root.inflate(containerId)
            root.addView(container)
        }
    }

}