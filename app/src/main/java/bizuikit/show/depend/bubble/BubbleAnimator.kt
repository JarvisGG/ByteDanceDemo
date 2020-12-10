package bizuikit.show.depend.bubble

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import android.view.View

/**
 * @author: yyf
 * @date: 2020/12/10
 * @desc: demo 触摸反馈
 */
object BubbleAlphaAnimator {

    private var animator: ValueAnimator? = null

    private var origin = 0f

    private var target = 0f

    private var duration = 0.toLong()

    private var delay = 0.toLong()

    private var animatorChain = arrayListOf<AnimChainNode>()

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    fun alpha(values: Float): BubbleAlphaAnimator {
        target = values
        return this
    }

    fun duration(value: Long): BubbleAlphaAnimator {
        duration = value
        return this
    }

    fun clear(): BubbleAlphaAnimator {
        animatorChain.clear()
        return this
    }

    fun delay(value: Long): BubbleAlphaAnimator {
        delay = value
        return this
    }

    fun bind(vararg view: View, update: ((animValue: Float) -> Unit)? = null): BubbleAlphaAnimator {
        val node = AnimChainNode(origin = origin, target = target, duration = duration, delay = delay, view = *view, update = update)
        animatorChain.add(node)
        reset()
        return this
    }

    private fun reset() {
        origin = target
        duration = 0.toLong()
        delay = 0.toLong()
    }

    fun start() {
        execute()
    }

    private fun execute() {
        if (animatorChain.size == 0) return
        val node = animatorChain[0]
        val anim = Runnable {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(node.origin, node.target)
            animator?.duration = node.duration
            animator?.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                node.view.forEach {
                    it.alpha = value
                }
                node.update?.invoke(value)
            }
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    animatorChain.remove(node)
                    execute()
                }
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
            animator?.start()
        }

        if (node.delay == 0.toLong()) {
            anim.run()
        } else {
            mainHandler.removeCallbacksAndMessages(null)
            mainHandler.postDelayed(anim, node.delay)
        }
    }

    class AnimChainNode(
        var origin: Float = 0f,
        var target: Float = 0f,
        var duration: Long = 0,
        var delay: Long = 0,
        vararg var view: View,
        var update: ((animValue: Float) -> Unit)? = null
    )

}