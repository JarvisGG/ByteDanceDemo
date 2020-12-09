/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bizuikit.components.magnetictarget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.os.Vibrator
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlin.math.abs
import kotlin.math.hypot

/**
 * 磁场吸附效果
 */
abstract class MagnetizedObject<T : Any>(
    val context: Context,

    val underlyingObject: T,

    val xProperty: FloatPropertyCompat<in T>,

    val yProperty: FloatPropertyCompat<in T>
) {

    abstract fun getWidth(underlyingObject: T): Float

    abstract fun getHeight(underlyingObject: T): Float

    abstract fun getLocationInWindow(underlyingObject: T, loc: IntArray)

    interface MagnetListener {

        fun onStuckToTarget(target: MagneticTarget)

        fun onUnstuckFromTarget(
            target: MagneticTarget,
            velX: Float,
            velY: Float,
            wasFlungOut: Boolean
        )

        fun onReleasedInTarget(target: MagneticTarget)
    }

    private val animatorX by lazy {
        SpringAnimation(underlyingObject, xProperty)
    }

    private val animatorY by lazy {
        SpringAnimation(underlyingObject, yProperty)
    }


    private val objectLocationOnScreen = IntArray(2)

    private val associatedTargets = ArrayList<MagneticTarget>()

    private val velocityTracker: VelocityTracker = VelocityTracker.obtain()
    private val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private var touchDown = PointF()
    private var touchSlop = 0
    private var movedBeyondSlop = false

    val objectStuckToTarget: Boolean
        get() = targetObjectIsStuckTo != null

    private var targetObjectIsStuckTo: MagneticTarget? = null

    lateinit var magnetListener: MagnetListener

    var animateStuckToTarget: (MagneticTarget, Float, Float, Boolean, (() -> Unit)?) -> Unit =
            ::animateStuckToTargetInternal

    var flingToTargetEnabled = true

    var flingToTargetWidthPercent = 3f

    var flingToTargetMinVelocity = 4000f

    var flingUnstuckFromTargetMinVelocity = 4000f

    var stickToTargetMaxXVelocity = 2000f

    fun addTarget(target: MagneticTarget) {
        associatedTargets.add(target)
        target.updateLocationOnScreen()
    }

    fun addTarget(target: View, magneticFieldRadiusPx: Int): MagneticTarget {
        return MagneticTarget(target, magneticFieldRadiusPx).also {
            addTarget(it) }
    }

    fun removeTarget(target: MagneticTarget) {
        associatedTargets.remove(target)
    }

    fun maybeConsumeMotionEvent(ev: MotionEvent): Boolean {
        if (associatedTargets.size == 0) {
            return false
        }

        if (ev.action == MotionEvent.ACTION_DOWN) {
            updateTargetViews()

            velocityTracker.clear()
            targetObjectIsStuckTo = null

            touchDown.set(ev.rawX, ev.rawY)
            movedBeyondSlop = false
        }

        addMovement(ev)

        if (!movedBeyondSlop) {
            val dragDistance = hypot(ev.rawX - touchDown.x, ev.rawY - touchDown.y)
            if (dragDistance > touchSlop) {
                movedBeyondSlop = true
            } else {
                return false
            }
        }

        // 筛选进入的第一个磁场对象
        val targetObjectIsInMagneticFieldOf = associatedTargets.firstOrNull { target ->
            val distanceFromTargetCenter = hypot(
                ev.rawX - target.centerOnScreen.x,
                ev.rawY - target.centerOnScreen.y
            )
            distanceFromTargetCenter < target.magneticFieldRadiusPx
        }

        // 是否第一次被吸附
        val objectNewlyStuckToTarget =
                !objectStuckToTarget && targetObjectIsInMagneticFieldOf != null

        // 是否被其他吸附
        val objectMovedIntoDifferentTarget =
                objectStuckToTarget &&
                        targetObjectIsInMagneticFieldOf != null &&
                        targetObjectIsStuckTo != targetObjectIsInMagneticFieldOf

        // 总之是被吸附
        if (objectNewlyStuckToTarget || objectMovedIntoDifferentTarget) {
            velocityTracker.computeCurrentVelocity(1000)
            val velX = velocityTracker.xVelocity
            val velY = velocityTracker.yVelocity

            // 只要速度够快，你就抓不住我
            if (objectNewlyStuckToTarget && abs(velX) > stickToTargetMaxXVelocity) {
                return false
            }

            targetObjectIsStuckTo = targetObjectIsInMagneticFieldOf
            cancelAnimations()
            magnetListener.onStuckToTarget(targetObjectIsInMagneticFieldOf!!)
            animateStuckToTarget(targetObjectIsInMagneticFieldOf, velX, velY, false, null)
            vibrateIfEnabled()
        }
        // 是否脱离当前吸附区域
        else if (targetObjectIsInMagneticFieldOf == null && objectStuckToTarget) {
            velocityTracker.computeCurrentVelocity(1000)
            cancelAnimations()
            magnetListener.onUnstuckFromTarget(
                targetObjectIsStuckTo!!, velocityTracker.xVelocity, velocityTracker.yVelocity,
                wasFlungOut = false
            )
            targetObjectIsStuckTo = null
            vibrateIfEnabled()
        }

        if (ev.action == MotionEvent.ACTION_UP) {

            velocityTracker.computeCurrentVelocity(1000 /* units */)
            val velX = velocityTracker.xVelocity
            val velY = velocityTracker.yVelocity

            cancelAnimations()

            if (objectStuckToTarget) {
                if (-velY > flingUnstuckFromTargetMinVelocity) {
                    magnetListener.onUnstuckFromTarget(
                        targetObjectIsStuckTo!!, velX, velY, wasFlungOut = true
                    )
                } else {
                    magnetListener.onReleasedInTarget(targetObjectIsStuckTo!!)
                    vibrateIfEnabled()
                }

                targetObjectIsStuckTo = null
                return true
            }

            val flungToTarget = associatedTargets.firstOrNull { target ->
                isForcefulFlingTowardsTarget(target, ev.rawX, ev.rawY, velX, velY)
            }

            if (flungToTarget != null) {
                magnetListener.onStuckToTarget(flungToTarget)
                targetObjectIsStuckTo = flungToTarget

                animateStuckToTarget(flungToTarget, velX, velY, true) {
                    magnetListener.onReleasedInTarget(flungToTarget)
                    targetObjectIsStuckTo = null
                    vibrateIfEnabled()
                }

                return true
            }

            return false
        }

        // 进入吸附区域，吃掉所有 touch 事件
        return objectStuckToTarget
    }

    @SuppressLint("MissingPermission")
    private fun vibrateIfEnabled() {
        vibrator.vibrate(40)
    }

    private fun addMovement(event: MotionEvent) {
        val deltaX = event.rawX - event.x
        val deltaY = event.rawY - event.y
        event.offsetLocation(deltaX, deltaY)
        velocityTracker.addMovement(event)
        event.offsetLocation(-deltaX, -deltaY)
    }

    private fun animateStuckToTargetInternal(
        target: MagneticTarget,
        velX: Float,
        velY: Float,
        flung: Boolean,
        after: (() -> Unit)? = null
    ) {
        target.updateLocationOnScreen()
        getLocationInWindow(underlyingObject, objectLocationOnScreen)

        val xDiff = target.centerOnScreen.x -
                getWidth(underlyingObject) / 2f - objectLocationOnScreen[0]
        val yDiff = target.centerOnScreen.y -
                getHeight(underlyingObject) / 2f - objectLocationOnScreen[1]

        cancelAnimations()

        animatorX.spring = SpringForce().apply {
            stiffness = SpringForce.STIFFNESS_MEDIUM
            dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
            finalPosition = xProperty.getValue(underlyingObject) + xDiff
        }
        animatorX.setStartVelocity(velX)
        animatorX.start()
        animatorX.addEndListener { _, _, _, _ -> after?.invoke() }

        animatorY.spring = SpringForce().apply {
            stiffness = SpringForce.STIFFNESS_MEDIUM
            dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
            finalPosition = yProperty.getValue(underlyingObject) + yDiff
        }
        animatorY.setStartVelocity(velY)
        animatorY.start()



    }

    private fun isForcefulFlingTowardsTarget(
        target: MagneticTarget,
        rawX: Float,
        rawY: Float,
        velX: Float,
        velY: Float
    ): Boolean {
        if (!flingToTargetEnabled) {
            return false
        }

        val velocitySufficient =
                if (rawY < target.centerOnScreen.y) velY > flingToTargetMinVelocity
                else velY < flingToTargetMinVelocity

        if (!velocitySufficient) {
            return false
        }

        var targetCenterXIntercept = rawX

        if (velX != 0f) {
            val slope = velY / velX
            val yIntercept = rawY - slope * rawX

            targetCenterXIntercept = (target.centerOnScreen.y - yIntercept) / slope
        }

        val targetAreaWidth = target.targetView.width * flingToTargetWidthPercent

        return targetCenterXIntercept > target.centerOnScreen.x - targetAreaWidth / 2 &&
                targetCenterXIntercept < target.centerOnScreen.x + targetAreaWidth / 2
    }

    internal fun cancelAnimations() {
        animatorX.cancel()
        animatorY.cancel()
    }

    internal fun updateTargetViews() {
        associatedTargets.forEach { it.updateLocationOnScreen() }

        // Update the touch slop, since the configuration may have changed.
        if (associatedTargets.size > 0) {
            touchSlop =
                    ViewConfiguration.get(associatedTargets[0].targetView.context).scaledTouchSlop
        }
    }

    class MagneticTarget(
        val targetView: View,
        var magneticFieldRadiusPx: Int
    ) {
        val centerOnScreen = PointF()

        private val tempLoc = IntArray(2)

        fun updateLocationOnScreen() {
            targetView.post {
                targetView.getLocationInWindow(tempLoc)
                centerOnScreen.set(
                    tempLoc[0] + targetView.width / 2f - targetView.translationX,
                    tempLoc[1] + targetView.height / 2f - targetView.translationY
                )
            }
        }
    }
}