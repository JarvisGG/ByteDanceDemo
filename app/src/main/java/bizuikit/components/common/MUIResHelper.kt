package bizuikit.components.common

import android.content.Context
import android.util.TypedValue

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
object MUIResHelper {

    private var sTmpValue = TypedValue()


    fun getAttrDimen(context: Context, attrRes: Int): Int {
        return if (!context.theme.resolveAttribute(attrRes, sTmpValue, true)) {
            0
        } else TypedValue.complexToDimensionPixelSize(
            sTmpValue.data,
            context.resources.displayMetrics
        )
    }
}