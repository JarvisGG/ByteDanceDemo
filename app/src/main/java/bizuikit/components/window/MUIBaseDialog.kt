package bizuikit.components.window

import android.R
import android.content.Context
import android.view.Window
import androidx.appcompat.app.AppCompatDialog

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
open class MUIBaseDialog(
    context: Context,
    themeResId: Int
) : AppCompatDialog(context, themeResId) {

    private var canceledOnTouchOutsideSet = false
    private var canceledOnTouchOutsideVal = true


    init {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    protected open fun shouldWindowCloseOnTouchOutside(): Boolean {
        if (!canceledOnTouchOutsideSet) {
            val a = context.obtainStyledAttributes(intArrayOf(R.attr.windowCloseOnTouchOutside))
            canceledOnTouchOutsideVal = a.getBoolean(0, true)
            a.recycle()
            canceledOnTouchOutsideSet = true
        }
        return canceledOnTouchOutsideVal
    }
}