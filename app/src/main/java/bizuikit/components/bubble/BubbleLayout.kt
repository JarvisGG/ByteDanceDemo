package bizuikit.components.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import bizuikit.components.magnetictarget.MagnetizedObject
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
class BubbleLayout: FrameLayout, OnComputeInternalInsetsListener.UpdateTouchRect {

    companion object {
        const val TAG = "BubbleLayout"
        private const val DISMISS_TRANSITION_DURATION_MS = 200
    }

    private var pointerIndexDown = -1

    private val touchRect = Rect()

    private lateinit var bubble: Bubble

    private val bubbleView by lazy {
        bubble.bubbleView
    }

    private val dismissContainer by lazy {
        FrameLayout(context)
    }

    private val dismissCircle by lazy {
        BubbleDismissView(context)
    }

    private var showingDismissCircle = false

    private var showingDismissBubble = false

    private var circleAnimator: SpringAnimation? = null

    private var bubbleAnimator: SpringAnimation? = null

    private var magnetizedObject: MagnetizedObject<*>? = null

    private var magneticTarget: MagnetizedObject.MagneticTarget? = null

    private val bubbleAnimationController by lazy {
        BubbleAnimationController(this)
    }

    private val invocationHandler = OnComputeInternalInsetsListener()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews()
    }

    init {
        invocationHandler.setTouchRegionRect(this)
        viewTreeObserver.addOnGlobalLayoutListener {
            updateTouchRegion()
        }
    }

    private fun initViews() {
        clipChildren = false
        clipToPadding = false
        initDismissView()
    }

    private fun initDismissView() {
        val circleSize: Int = resources.getDimensionPixelSize(R.dimen.dismiss_circle_size)
        dismissCircle.layoutParams = LayoutParams(
            circleSize, circleSize, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        )
        dismissCircle.translationY = resources.getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height).toFloat()

        dismissContainer.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.floating_dismiss_gradient_height),
            Gravity.BOTTOM
        )
        dismissContainer.setBackgroundResource(R.drawable.mui_floating_dismiss_gradient_transition)
        dismissContainer.visibility = View.VISIBLE
        dismissContainer.clipChildren = false
        dismissContainer.clipToPadding = false
        val bottomPadding = resources.getDimensionPixelSize(R.dimen.floating_dismiss_bottom_margin)
        dismissContainer.setPadding(0, 0, 0, bottomPadding)
        dismissContainer.addView(dismissCircle)

        addView(dismissContainer)

        // 磁场范围
        val dismissRadius = resources.getDimensionPixelSize(R.dimen.dismiss_circle_radius)
        // dismissCircle 作为磁场中心
        magneticTarget = MagnetizedObject.MagneticTarget(dismissCircle, dismissRadius)
    }

    private fun showDismissCircle() {
        if (showingDismissCircle) {
            return
        }
        showingDismissCircle = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dismissContainer.z = Short.MAX_VALUE - 1.toFloat()
        }
        dismissContainer.visibility = VISIBLE
        (dismissContainer.background as TransitionDrawable).startTransition(
            DISMISS_TRANSITION_DURATION_MS
        )
        circleAnimator?.cancel()
        circleAnimator = generateDismissAnimator(dismissCircle)
        circleAnimator?.animateToFinalPosition(0f)
    }

    private fun hideDismissCircle() {
        if (!showingDismissCircle) {
            return
        }
        showingDismissCircle = false
        (dismissContainer.background as TransitionDrawable).reverseTransition(
            DISMISS_TRANSITION_DURATION_MS
        )
        circleAnimator?.cancel()
        circleAnimator = generateDismissAnimator(dismissCircle)
        circleAnimator?.animateToFinalPosition(dismissContainer.height.toFloat())
    }

    private fun hideDismissBubble() {
        if (showingDismissBubble || bubbleView == null) {
            return
        }
        showingDismissBubble = true
        bubbleAnimator?.cancel()
        bubbleAnimator = generateDismissAnimator(bubbleView!!)
        bubbleAnimator?.addEndListener { _, _, _, _ ->
            bubbleCallback?.onDismiss()
        }
        bubbleAnimator?.animateToFinalPosition(bubble.bubbleView!!.translationY + dismissContainer.height)
    }

    private fun generateDismissAnimator(target: View): SpringAnimation {
        return SpringAnimation(target, DynamicAnimation.TRANSLATION_Y).apply {
            spring = SpringForce().apply {
                stiffness = SpringForce.STIFFNESS_LOW
                dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
            }
        }
    }

    private fun passEventToMagnetizedObject(event: MotionEvent): Boolean {
        return magnetizedObject != null && magnetizedObject?.maybeConsumeMotionEvent(event) ?: false
    }

    private val stackMagnetListener = object : MagnetizedObject.MagnetListener {
        override fun onStuckToTarget(target: MagnetizedObject.MagneticTarget) {
        }

        override fun onUnstuckFromTarget(
            target: MagnetizedObject.MagneticTarget,
            velX: Float,
            velY: Float,
            wasFlungOut: Boolean
        ) {
        }

        override fun onReleasedInTarget(target: MagnetizedObject.MagneticTarget) {
            hideDismissBubble()
            hideDismissCircle()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ReflectionUtils.addOnComputeInternalInsetsListener(
            viewTreeObserver,
            invocationHandler.listener
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ReflectionUtils.removeOnComputeInternalInsetsListener(viewTreeObserver)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action != MotionEvent.ACTION_DOWN && ev.actionIndex != pointerIndexDown) {
            return false
        }
        if (ev.action == MotionEvent.ACTION_DOWN) {
            pointerIndexDown = ev.actionIndex
        } else if (ev.action == MotionEvent.ACTION_UP
            || ev.action == MotionEvent.ACTION_CANCEL
        ) {
            pointerIndexDown = -1
        }
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun addBubble(bubble: Bubble) {
        this.bubble = bubble
        bindBubbleView()
        bubbleAnimationController.bindBubble(bubble)
        bubbleAnimationController.setBubblePosition(bubbleAnimationController.getDefaultStartPosition())
    }

    private fun bindBubbleView() {
        bubbleView?.setOnTouchListener(bubbleTouchListener)
        bubbleView?.setOnClickListener {
            bubble.config.onClick()
        }
        addView(bubbleView)
        dismissContainer.bringToFront()
    }

    private val bubbleTouchListener = object : RelativeTouchListener() {
        override fun onDown(v: View, ev: MotionEvent): Boolean {
            bubble.config.onDown(v)
            showingDismissBubble = false
            bubbleAnimationController.cancelBubblePositionAnimations()

            if (magneticTarget != null) {
                magnetizedObject = bubbleAnimationController.getMagnetizedStack(magneticTarget!!)
                magnetizedObject?.magnetListener = stackMagnetListener
            }
            passEventToMagnetizedObject(ev)
            return true
        }

        override fun onMove(
            v: View,
            ev: MotionEvent,
            viewInitialX: Float,
            viewInitialY: Float,
            dx: Float,
            dy: Float
        ) {
            showDismissCircle()
            if (passEventToMagnetizedObject(ev)) {
                return
            }
            bubble.config.onMove(v, viewInitialX + dx, viewInitialY + dy)
            bubbleAnimationController.moveBubbleFromTouch(viewInitialX + dx, viewInitialY + dy)
        }

        override fun onUp(
            v: View, ev: MotionEvent,
            viewInitialX: Float, viewInitialY: Float,
            dx: Float, dy: Float, velX: Float, velY: Float
        ) {
            if (passEventToMagnetizedObject(ev)) {
                return
            }
            bubble.config.onUp(v)
            bubbleAnimationController.flingBubbleThenSpringToEdge(
                viewInitialX + dx, velX, velY
            )
            hideDismissCircle()
        }
    }

    private fun getTouchableRegion(outRect: Rect) {
        if (childCount > 0) {
            bubbleAnimationController.getBoundsOnScreen(outRect)
        }
    }

    private fun updateTouchRegion() {
        touchRect.setEmpty()
        getTouchableRegion(touchRect)
    }

    override fun onUpdateTouchRect(): Rect {
        updateTouchRegion()
        return touchRect
    }

    private var bubbleCallback: BubbleCallback? = null

    interface BubbleCallback {
        fun onDismiss()
    }

    fun registerBubbleCallback(callback: BubbleCallback) {
        bubbleCallback = callback
    }
}