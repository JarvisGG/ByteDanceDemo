package bizuikit.components.dialog.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import bizuikit.components.dialog.MUIBaseDialog
import com.example.bytedancedemo.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIBottomSheet : MUIBaseDialog {

    private var behavior: MUIBottomSheetBehavior<ViewGroup> = MUIBottomSheetBehavior()

    private var rootView: ViewGroup

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

        val container = layoutInflater.inflate(R.layout.mui_bottom_sheet_dialog, null) as ViewGroup

        rootView = container.findViewById(R.id.bottom_sheet)
        val rootViewLp = rootView.layoutParams as CoordinatorLayout.LayoutParams
        rootViewLp.behavior = behavior

        container.findViewById<View>(R.id.touch_outside)
            .setOnClickListener(
                View.OnClickListener {
                    if (behavior.state == BottomSheetBehavior.STATE_SETTLING) {
                        return@OnClickListener
                    }
                    if (cancelableVal && isShowing && shouldWindowCloseOnTouchOutside()) {
                        cancel()
                    }
                })
        rootView.setOnTouchListener { _, _ -> true }

        super.setContentView(
            container, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
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
        ViewCompat.requestApplyInsets(rootView)
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
            rootView.postOnAnimation { behavior.setState(BottomSheetBehavior.STATE_EXPANDED) }
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
        return rootView
    }

    fun getBehavior(): MUIBottomSheetBehavior<ViewGroup> {
        return behavior
    }

    fun addContainer(view: View?, lp: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )) {
        rootView.addView(view, lp)
    }

    fun addContainer(layoutResId: Int) {
        LayoutInflater.from(rootView.context).inflate(layoutResId, rootView, true)
    }


}