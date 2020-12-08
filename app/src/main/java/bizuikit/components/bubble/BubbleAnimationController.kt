package bizuikit.components.bubble

import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.View
import androidx.dynamicanimation.animation.*
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import bizuikit.components.magnetictarget.MagnetizedObject
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

        private const val FLING_TO_DISMISS_MIN_VELOCITY = 4000f
        const val TAG = "BubbleAnimationController"
    }

    private var bubble: Bubble? = null

    private fun bubbleView() =
        bubble?.bubbleView

    /**
     * bubble 大小
     */
    private fun bubbleSize() = bubbleView()?.width?.toFloat() ?: 0f

    /**
     * bubble 位置
     */
    private val bubblePosition = PointF((-1).toFloat(), (-1).toFloat())

    /**
     * bubble XY 轴动画集
     */
    private val bubblePositionAnimations = HashMap<ViewProperty, DynamicAnimation<*>>()

    /**
     * 默认左对齐 & Y 轴位置
     */
    private val startingVerticalOffset = 100.dp2px

    private val context: Context by lazy {
        layout.context
    }

    fun bindBubble(bubble: Bubble) {
        this.bubble = bubble
    }
    
    private var springToTouchOnNextMotionEvent = true
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
        springBubbleFollowing(
            DynamicAnimation.TRANSLATION_X,
            SpringForce()
                .setStiffness(stiffness)
                .setDampingRatio(SPRING_AFTER_FLING_DAMPING_RATIO),
            0f,
            destinationX,
            *update
        )
        springBubbleFollowing(
            DynamicAnimation.TRANSLATION_Y,
            SpringForce()
                .setStiffness(stiffness)
                .setDampingRatio(SPRING_AFTER_FLING_DAMPING_RATIO),
            0f,
            destinationY,
            *update
        )
    }

    protected open fun springBubbleFollowing(
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

        cancelPositionAnimation(property)
        bubblePositionAnimations[property] = springAnimation
        springAnimation.animateToFinalPosition(finalPosition)
    }


    private fun moveBubbleFollowing(
        property: ViewProperty, value: Float
    ) {

        if (property == DynamicAnimation.TRANSLATION_X) {
            bubblePosition.x = value
        } else if (property == DynamicAnimation.TRANSLATION_Y) {
            bubblePosition.y = value
        }
        if (layout.childCount > 0) {
            property.setValue(bubbleView(), value)
        }
    }

    fun flingBubbleThenSpringToEdge(x: Float, velX: Float, velY: Float, vararg update: Runnable?): Float {
        val isLeftSide: Boolean = x < layout.width / 2
        val shouldFlingLeft = if (isLeftSide)
            velX < ESCAPE_VELOCITY else velX < -ESCAPE_VELOCITY

        val bounds = getAllowableBubblePositionRegion()
        val destinationRelativeX = if (shouldFlingLeft) bounds.left else bounds.right
        if (layout.childCount == 0) {
            return destinationRelativeX
        }

        val stiffness = SPRING_AFTER_FLING_STIFFNESS.toFloat()
        val dampingRatio = SPRING_AFTER_FLING_DAMPING_RATIO
        val friction = FLING_FRICTION

        val minimumVelocityToReachEdge = (destinationRelativeX - x) * (friction * 4.8f)

        val startXVelocity =
            if (shouldFlingLeft) min(minimumVelocityToReachEdge, velX) else max(
                minimumVelocityToReachEdge,
                velX
            )
        flingThenSpringBubbleFollowing(
            DynamicAnimation.TRANSLATION_X,
            startXVelocity,
            friction,
            SpringForce()
                .setStiffness(stiffness)
                .setDampingRatio(dampingRatio),
            destinationRelativeX,
            *update
        )

        flingThenSpringBubbleFollowing(
            DynamicAnimation.TRANSLATION_Y,
            velY,
            friction,
            SpringForce()
                .setStiffness(stiffness)
                .setDampingRatio(dampingRatio),
            null,
            *update
        )

        springToTouchOnNextMotionEvent = true
        return destinationRelativeX

    }

    protected open fun flingThenSpringBubbleFollowing(
        property: ViewProperty,
        vel: Float,
        friction: Float,
        spring: SpringForce,
        finalPosition: Float?,
        vararg update: Runnable?
    ) {
        val firstBubbleProperty = BubblePositionProperty(property)
        val currentValue: Float = firstBubbleProperty.getValue(this)
        val bounds = getAllowableBubblePositionRegion()

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
                if (!canceled) {
                    springBubbleFollowing(
                        property, spring, endVelocity,
                        finalPosition ?: Math.max(min, Math.min(max, endValue))
                    )
                }
            }
        cancelPositionAnimation(property)
        bubblePositionAnimations[property] = flingAnimation
        flingAnimation.start()
    }

    fun cancelBubblePositionAnimations() {
        cancelPositionAnimation(DynamicAnimation.TRANSLATION_X)
        cancelPositionAnimation(DynamicAnimation.TRANSLATION_Y)
    }

    private fun cancelPositionAnimation(property: ViewProperty) {
        if (bubblePositionAnimations.containsKey(property)) {
            bubblePositionAnimations[property]?.cancel()
        }
    }

    open fun getAllowableBubblePositionRegion(): RectF {
        val allowableRegion = RectF()
        allowableRegion.left = 0f
        allowableRegion.right = layout.width - bubbleSize()
        allowableRegion.top = 0f
        allowableRegion.bottom = layout.height - bubbleSize()
        return allowableRegion
    }

    fun getDefaultStartPosition(): PointF {
        val isRtl = (layout.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL)
        return PointF(
            if (isRtl) getAllowableBubblePositionRegion().right else getAllowableBubblePositionRegion().left,
            getAllowableBubblePositionRegion().top + startingVerticalOffset
        )
    }

    open fun setBubblePosition(pos: PointF) {
        bubblePosition.set(pos.x, pos.y)
        bubbleView()?.translationX = pos.x
        bubbleView()?.translationY = pos.y
    }

    fun getBoundsOnScreen(outRect: Rect) {
        val location = IntArray(2)
        bubbleView()?.getLocationInWindow(location)
        outRect.left = location[0]
        outRect.top = location[1]
        outRect.right = outRect.left + bubbleSize().toInt()
        outRect.bottom = outRect.top + bubbleSize().toInt()
    }

    inner class BubblePositionProperty(
        private val property: ViewProperty
    ) : FloatPropertyCompat<BubbleAnimationController>(property.toString()) {


        override fun getValue(`object`: BubbleAnimationController?): Float {
            return if (layout.childCount > 0) property.getValue(bubbleView()) else 0f
        }

        override fun setValue(`object`: BubbleAnimationController?, value: Float) {
            moveBubbleFollowing(property, value)
        }
    }


    private lateinit var magnetizedObject: MagnetizedObject<BubbleAnimationController>

    open fun getMagnetizedStack(
        target: MagnetizedObject.MagneticTarget
    ): MagnetizedObject<BubbleAnimationController> {
        if (!::magnetizedObject.isInitialized) {
            magnetizedObject = object : MagnetizedObject<BubbleAnimationController>(
                context, this@BubbleAnimationController,
                BubblePositionProperty(
                    DynamicAnimation.TRANSLATION_X
                ),
                BubblePositionProperty(
                    DynamicAnimation.TRANSLATION_Y
                )
            ) {
                override fun getWidth(underlyingObject: BubbleAnimationController): Float {
                    return bubbleSize()
                }

                override fun getHeight(underlyingObject: BubbleAnimationController): Float {
                    return bubbleSize()
                }

                override fun getLocationInWindow(
                    underlyingObject: BubbleAnimationController,
                    loc: IntArray
                ) {
                    loc[0] = bubblePosition.x.toInt()
                    loc[1] = bubblePosition.y.toInt()
                }
            }
            magnetizedObject.addTarget(target)
            magnetizedObject.flingToTargetMinVelocity = FLING_TO_DISMISS_MIN_VELOCITY
        }
        return magnetizedObject
    }
}
