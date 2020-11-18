package bizuikit.components.bubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
class BubbleLayout: FrameLayout, OnComputeInternalInsetsListener.UpdateTouchRect {

    companion object {
        const val TAG = "BubbleLayout"
    }

    private var pointerIndexDown = -1

    private val isExpansionAnimating = false

    private val tempRect = Rect()

    private var bubble: Bubble? = null

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
    )

    init {
        invocationHandler.setTouchRegionRect(this)
        viewTreeObserver.addOnGlobalLayoutListener {
            updateTouchRegion()
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
        bubble.bubbleView?.setOnTouchListener(bubbleTouchListener)
        bubble.bubbleView?.setOnClickListener(null)
        addView(bubble.bubbleView)
        bubbleAnimationController.setBubblePosition(bubbleAnimationController.getDefaultStartPosition())
    }

    private val bubbleTouchListener = object : RelativeTouchListener() {
        override fun onDown(v: View, ev: MotionEvent): Boolean {
            if (isExpansionAnimating) {
                return true
            }
            bubbleAnimationController.cancelBubblePositionAnimations()
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
            bubbleAnimationController.moveBubbleFromTouch(viewInitialX + dx, viewInitialY + dy)
        }

        override fun onUp(
            v: View, ev: MotionEvent,
            viewInitialX: Float, viewInitialY: Float,
            dx: Float, dy: Float, velX: Float, velY: Float
        ) {
            bubbleAnimationController.flingBubbleThenSpringToEdge(
                viewInitialX + dx, velX, velY
            )
        }
    }

    private fun getTouchableRegion(outRect: Rect) {
        if (childCount > 0) {
            bubbleAnimationController.getBoundsOnScreen(outRect)
        }
    }

    private fun updateTouchRegion() {
        tempRect.setEmpty()
        getTouchableRegion(tempRect)
    }

    override fun onUpdateTouchRect(): Rect {
        updateTouchRegion()
        return tempRect
    }
}