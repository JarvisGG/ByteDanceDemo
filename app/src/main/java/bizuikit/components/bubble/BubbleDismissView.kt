package bizuikit.components.bubble

import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import bizuikit.utils.dp2px
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/12/8
 * @desc:
 */
class BubbleDismissView(context: Context?) : FrameLayout(context) {
    private val mIconView = ImageView(getContext())
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setViewSizes()
    }

    private fun setViewSizes() {
        val res = resources
        val iconSize = 24.dp2px.toInt()
        mIconView.layoutParams = LayoutParams(iconSize, iconSize, Gravity.CENTER)
    }

    init {
        val res = resources
        background = res.getDrawable(R.drawable.mui_bubble_dismiss_circle)
        mIconView.setImageDrawable(res.getDrawable(R.drawable.mui_bubble_dismiss_icon))
        addView(mIconView)
        setViewSizes()
    }
}