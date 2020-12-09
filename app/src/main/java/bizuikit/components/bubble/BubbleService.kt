package bizuikit.components.bubble

import android.animation.ValueAnimator
import android.app.Service
import android.content.Intent
import android.graphics.PointF
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import bizuikit.components.layout.MUILayout
import bizuikit.utils.dp2px
import bizuikit.utils.getStatusBarHeight
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/11/18
 * @desc:
 */
open class BubbleService : Service() {

    private var bubbleController: BubbleController? = null

    private var alphaAnimator: ValueAnimator? = null

    private var mainHandler = Handler(Looper.getMainLooper())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bubbleController = BubbleController(this)
        bubbleController?.registerBubbleConfig(object : BubbleConfig() {

            override fun getLayoutId() = R.layout.mui_bubble_view

            override fun onAttachToWindow(v: View) {
                v.alpha = 0.7f
                if (v is MUILayout) {
                    v.setShadowAlpha(0f)
                }
            }

            override fun onDown(v: View) {
                mainHandler.removeCallbacksAndMessages(null)
                alphaAnimator?.cancel()
                alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
                alphaAnimator?.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    v.alpha = 0.7f + 0.3f * animatedValue
                    if (v is MUILayout) {
                        v.setShadowAlpha(animatedValue)
                    }
                }
                alphaAnimator?.duration = 300
                alphaAnimator?.start()
            }

            override fun onUp(v: View) {
                mainHandler.postDelayed({
                    alphaAnimator?.cancel()
                    alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
                    alphaAnimator?.addUpdateListener {
                        val animatedValue = it.animatedValue as Float
                        v.alpha = 1f - 0.3f * animatedValue
                        if (v is MUILayout) {
                            v.setShadowAlpha(1 - animatedValue)
                        }
                    }
                    alphaAnimator?.duration = 300
                    alphaAnimator?.start()
                }, 1000)

            }

            override fun getStartMargin(): Float {
                return (-4).dp2px
            }

            override fun getEndMargin(): Float {
                return 8.dp2px
            }

            override fun getTopMargin(): Float {
                return baseContext.getStatusBarHeight().toFloat()
            }

            override fun getDefaultPosition(): PointF {
                return PointF(0f, 0f)
            }

        })
        return super.onStartCommand(intent, flags, START_STICKY)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        bubbleController?.clear()
    }
}