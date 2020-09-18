package bizuikit.components.window.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import bizuikit.components.common.IMUIShapeLayout
import bizuikit.components.window.MUIBaseDialog
import bizuikit.components.layout.MUILayout
import com.example.bytedancedemo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIBottomSheet : MUIBaseDialog, IMUIShapeLayout {

    private var behavior: MUIBottomSheetBehavior<out MUILayout> = MUIBottomSheetBehavior()

    private var root: FrameLayout

    private var container: MUILayout

    private var animateToCancel = false

    private var animateToDismiss = false

    private var cancelableVal = true

    constructor(context: Context) : this(context, R.style.MUIBaseDialog)

    @SuppressLint("ClickableViewAccessibility")
    constructor(context: Context, style: Int) : super(context, style) {

        behavior.isHideable = true
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    when {
                        animateToCancel -> {
                            cancel()
                        }
                        animateToDismiss -> {
                            dismiss()
                        }
                        else -> {
                            cancel()
                        }
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        behavior.peekHeight = 0
        behavior.allowDrag = false
        behavior.skipCollapsed = true

        root = layoutInflater.inflate(R.layout.mui_bottom_sheet, null, false) as FrameLayout

        container = root.findViewById(R.id.ml_container)
        val containerLp = container.layoutParams as CoordinatorLayout.LayoutParams
        containerLp.behavior = behavior

        root.findViewById<View>(R.id.touch_outside)
            .setOnClickListener(
                View.OnClickListener {
                    if (behavior.state == BottomSheetBehavior.STATE_SETTLING) {
                        return@OnClickListener
                    }
                    if (cancelableVal && isShowing && shouldWindowCloseOnTouchOutside()) {
                        cancel()
                    }
                })
        root.setOnTouchListener { _, _ -> true }

        super.setContentView(root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun onSetCancelable(cancelable: Boolean) {
        behavior.isHideable = cancelable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        ViewCompat.requestApplyInsets(container)
    }

    override fun onStart() {
        super.onStart()
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun show() {
        super.show()
        if (behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            container.postOnAnimation { behavior.setState(BottomSheetBehavior.STATE_EXPANDED) }
        }
        animateToCancel = false
        animateToDismiss = false
    }

    override fun cancel() {
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            animateToCancel = false
            super.cancel()
        } else {
            animateToCancel = true
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    override fun dismiss() {
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            animateToDismiss = false
            super.dismiss()
        } else {
            animateToDismiss = true
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    fun getRootView(): ViewGroup {
        return container
    }

    fun getBehavior(): MUIBottomSheetBehavior<out MUILayout> {
        return behavior
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