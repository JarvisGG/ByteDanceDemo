package bizuikit.components.bubble

import android.graphics.PointF
import android.os.Handler
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.hypot

abstract class RelativeTouchListener : View.OnTouchListener {

    abstract fun onDown(v: View, ev: MotionEvent): Boolean

    abstract fun onMove(
        v: View,
        ev: MotionEvent,
        viewInitialX: Float,
        viewInitialY: Float,
        dx: Float,
        dy: Float
    )

    abstract fun onUp(
        v: View,
        ev: MotionEvent,
        viewInitialX: Float,
        viewInitialY: Float,
        dx: Float,
        dy: Float,
        velX: Float,
        velY: Float
    )

    private val touchDown = PointF()

    private val viewPositionOnTouchDown = PointF()

    private val velocityTracker = VelocityTracker.obtain()

    private var touchSlop: Int = -1
    private var movedEnough = false

    private val handler = Handler()
    private var performedLongClick = false

    @Suppress("UNCHECKED_CAST")
    override fun onTouch(v: View, ev: MotionEvent): Boolean {
        addMovement(ev)

        val dx = ev.rawX - touchDown.x
        val dy = ev.rawY - touchDown.y

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!onDown(v, ev)) {
                    return false
                }

                touchSlop = ViewConfiguration.get(v.context).scaledTouchSlop

                touchDown.set(ev.rawX, ev.rawY)
                viewPositionOnTouchDown.set(v.translationX, v.translationY)

                performedLongClick = false
                handler.postDelayed({
                    if (v.isLongClickable) {
                        performedLongClick = v.performLongClick()
                    }
                }, ViewConfiguration.getLongPressTimeout().toLong())
            }

            MotionEvent.ACTION_MOVE -> {
                if (!movedEnough && hypot(dx, dy) > touchSlop && !performedLongClick) {
                    movedEnough = true
                    handler.removeCallbacksAndMessages(null)
                }

                if (movedEnough) {
                    onMove(v, ev, viewPositionOnTouchDown.x, viewPositionOnTouchDown.y, dx, dy)
                }
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000 /* units */)
                onUp(v, ev, viewPositionOnTouchDown.x, viewPositionOnTouchDown.y, dx, dy,
                    velocityTracker.xVelocity, velocityTracker.yVelocity)
                if (movedEnough) {

                } else if (!performedLongClick) {
                    v.performClick()
                } else {
                    handler.removeCallbacksAndMessages(null)
                }

                velocityTracker.clear()
                movedEnough = false
            }
        }

        return true
    }

    private fun addMovement(event: MotionEvent) {
        val deltaX = event.rawX - event.x
        val deltaY = event.rawY - event.y
        event.offsetLocation(deltaX, deltaY)
        velocityTracker.addMovement(event)
        event.offsetLocation(-deltaX, -deltaY)
    }
}