package bizuikit.components.bubble

import android.graphics.PointF
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.dynamicanimation.animation.*
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import bizuikit.utils.dp2px
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
open class BubbleAnimationController(
    private val layout: BubbleLayout
) {

    companion object {
        const val DEFAULT_STIFFNESS = 12000
        const val SPRING_AFTER_FLING_STIFFNESS = 750
        const val SPRING_AFTER_FLING_DAMPING_RATIO = 0.85f
        const val FLING_FRICTION = 2.2f
        const val ESCAPE_VELOCITY = 750f
        const val TAG = "BubbleAnimationController"
    }
    private val bubbleBitmapSize = 0

    private val bubbleSize = 50.dp2px

    private val stackStartingVerticalOffset = 0

    private var springToTouchOnNextMotionEvent = true

    private val bubblePosition = PointF((-1).toFloat(), (-1).toFloat())

    private val bubblePositionAnimations = HashMap<ViewProperty, DynamicAnimation<*>>()

    fun moveBubbleFromTouch(x: Float, y: Float, vararg update: Runnable?) {
        if (springToTouchOnNextMotionEvent) {
            springBubble(x, y, DEFAULT_STIFFNESS.toFloat(), *update)
            springToTouchOnNextMotionEvent = false
        } else {
            val springToTouchX =
                bubblePositionAnimations[DynamicAnimation.TRANSLATION_X] as SpringAnimation
            val springToTouchY =
                bubblePositionAnimations[DynamicAnimation.TRANSLATION_Y] as SpringAnimation

            springToTouchX.animateToFinalPosition(x)
            springToTouchY.animateToFinalPosition(y)
        }
    }

    open fun springBubble(
        destinationX: Float, destinationY: Float, stiffness: Float, vararg update: Runnable?
    ) {
        springBubbleWithStackFollowing(
            DynamicAnimation.TRANSLATION_X,
            SpringForce()
                .setStiffness(stiffness)
                .setDampingRatio(SPRING_AFTER_FLING_DAMPING_RATIO),
            0f,
            destinationX,
            *update
        )
        springBubbleWithStackFollowing(
            DynamicAnimation.TRANSLATION_Y,
            SpringForce()
                .setStiffness(stiffness)
                .setDampingRatio(SPRING_AFTER_FLING_DAMPING_RATIO),
            0f,
            destinationY,
            *update
        )
    }

    protected open fun springBubbleWithStackFollowing(
        property: ViewProperty, spring: SpringForce,
        vel: Float, finalPosition: Float, vararg update: Runnable?
    ) {
        if (layout.childCount == 0) {
            return
        }
        val firstBubbleProperty = BubblePositionProperty(property)

        val springAnimation = SpringAnimation(this, firstBubbleProperty)
            .setSpring(spring)
            .addUpdateListener { animation, value, velocity ->
                update.forEach {
                    it?.run()
                }
            }
            .addEndListener { dynamicAnimation: DynamicAnimation<*>, canceled: Boolean, value: Float, velocity: Float ->
            }
            .setStartVelocity(vel)

        bubblePositionAnimations[property] = springAnimation
        springAnimation.animateToFinalPosition(finalPosition)
    }


    private fun moveBubbleWithStackFollowing(
        property: ViewProperty, value: Float
    ) {

        if (property == DynamicAnimation.TRANSLATION_X) {
            bubblePosition.x = value
        } else if (property == DynamicAnimation.TRANSLATION_Y) {
            bubblePosition.y = value
        }
        if (layout.childCount > 0) {
            property.setValue(layout.getChildAt(0), value)
        }
    }

    fun cancelBubblePositionAnimations() {
        cancelStackPositionAnimation(DynamicAnimation.TRANSLATION_X)
        cancelStackPositionAnimation(DynamicAnimation.TRANSLATION_Y)
    }

    private fun cancelStackPositionAnimation(property: ViewProperty) {
        if (bubblePositionAnimations.containsKey(property)) {
            bubblePositionAnimations[property]?.cancel()
        }
    }

    fun flingBubbleThenSpringToEdge(x: Float, velX: Float, velY: Float, vararg update: Runnable?): Float {
        val stackOnLeftSide: Boolean = x - bubbleBitmapSize / 2 < layout.width / 2
        val stackShouldFlingLeft = if (stackOnLeftSide)
            velX < ESCAPE_VELOCITY else velX < -ESCAPE_VELOCITY

        val stackBounds = getAllowableBubblePositionRegion()
        val destinationRelativeX = if (stackShouldFlingLeft) stackBounds!!.left else stackBounds!!.right
        if (layout.childCount == 0) {
            return destinationRelativeX
        }

        val stiffness = SPRING_AFTER_FLING_STIFFNESS
        val dampingRatio = SPRING_AFTER_FLING_DAMPING_RATIO
        val friction = FLING_FRICTION
        val minimumVelocityToReachEdge = (destinationRelativeX - x) * (friction * 4.2f)

        val startXVelocity =
            if (stackShouldFlingLeft) min(minimumVelocityToReachEdge, velX) else max(
                minimumVelocityToReachEdge,
                velX
            )

        flingThenSpringBubbleWithStackFollowing(
            DynamicAnimation.TRANSLATION_X,
            startXVelocity,
            friction,
            SpringForce()
                .setStiffness(stiffness.toFloat())
                .setDampingRatio(dampingRatio),
            destinationRelativeX,
            *update
        )

        flingThenSpringBubbleWithStackFollowing(
            DynamicAnimation.TRANSLATION_Y,
            velY,
            friction,
            SpringForce()
                .setStiffness(stiffness.toFloat())
                .setDampingRatio(dampingRatio),  /* destination */
            null,
            *update
        )

        springToTouchOnNextMotionEvent = true
        return destinationRelativeX

    }

    protected open fun flingThenSpringBubbleWithStackFollowing(
        property: ViewProperty,
        vel: Float,
        friction: Float,
        spring: SpringForce?,
        finalPosition: Float?,
        vararg update: Runnable?
    ) {
        val firstBubbleProperty = BubblePositionProperty(property)
        val currentValue: Float = firstBubbleProperty.getValue(this)
        val bounds = getAllowableBubblePositionRegion() ?: return
        val min = if (property == DynamicAnimation.TRANSLATION_X) bounds.left else bounds.top
        val max = if (property == DynamicAnimation.TRANSLATION_X) bounds.right else bounds.bottom
        val flingAnimation = FlingAnimation(this, firstBubbleProperty)
        flingAnimation.setFriction(friction)
            .setStartVelocity(vel)
            .setMinValue(min(currentValue, min))
            .setMaxValue(max(currentValue, max))
            .addUpdateListener { animation, value, velocity ->
                update.forEach {
                    it?.run()
                }
            }
            .addEndListener { animation: DynamicAnimation<*>?, canceled: Boolean, endValue: Float, endVelocity: Float ->
            }
        cancelStackPositionAnimation(property)
        bubblePositionAnimations[property] = flingAnimation
        flingAnimation.start()
    }


    open fun getAllowableBubblePositionRegion(): RectF {
        val allowableRegion = RectF()
        allowableRegion.left = 0f
        allowableRegion.right = layout.width - bubbleSize
        allowableRegion.top = 0f
        allowableRegion.bottom = layout.height- bubbleSize
        return allowableRegion
    }

    fun getDefaultStartPosition(): PointF {
        val isRtl = (layout.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
        return PointF(
            if (isRtl) getAllowableBubblePositionRegion().right else getAllowableBubblePositionRegion().left,
            getAllowableBubblePositionRegion().top + stackStartingVerticalOffset
        )
    }

    open fun setBubblePosition(pos: PointF) {
        bubblePosition.set(pos.x, pos.y)
        layout.getChildAt(0).translationX = pos.x
        layout.getChildAt(0).translationY = pos.y
    }

    inner class BubblePositionProperty(
        private val property: ViewProperty
    ) : FloatPropertyCompat<BubbleAnimationController>(property.toString()) {


        override fun getValue(`object`: BubbleAnimationController?): Float {
            return if (layout.childCount > 0) property.getValue(layout.getChildAt(0)) else 0f
        }

        override fun setValue(`object`: BubbleAnimationController?, value: Float) {
            moveBubbleWithStackFollowing(property, value)
        }
    }
}