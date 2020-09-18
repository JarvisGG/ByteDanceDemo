package bizuikit.components.window.bottomsheet

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIBottomSheetBehavior<V : ViewGroup> : BottomSheetBehavior<V>() {

    var allowDrag = true

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        event: MotionEvent
    ): Boolean {
        if (!allowDrag) {
            return false
        }
        return super.onInterceptTouchEvent(parent, child, event)
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        if (!allowDrag) {
            return false
        }
        return super.onTouchEvent(parent, child, event)
    }


}