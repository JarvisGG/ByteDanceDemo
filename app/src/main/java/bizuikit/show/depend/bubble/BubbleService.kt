package bizuikit.show.depend.bubble

import android.app.Service
import android.content.Intent
import android.graphics.PointF
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import bizuikit.components.badge.MUIBadge
import bizuikit.components.bubble.BubbleConfig
import bizuikit.components.bubble.BubbleController
import bizuikit.components.layout.MUILayout
import bizuikit.utils.DemoActivity
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

    private var icon: MUILayout? = null
    private var badge: MUIBadge? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bubbleController = BubbleController(this)
        bubbleController?.registerBubbleConfig(object : BubbleConfig() {

            override fun getLayoutId() = R.layout.mui_bubble_view

            override fun onBind(view: View?) {
                if (view == null) return

                badge = view.findViewById(R.id.ml_bubble_badge)
                badge?.showCount(10)
                badge?.z = 20f
                icon = view.findViewById(R.id.ml_bubble_icon)
                val parent = icon?.parent
                if (parent != null && parent is ViewGroup) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        parent.clipToOutline = false
                    }
                    parent.clipChildren = false
                }
            }

            override fun onAttachToWindow(v: View) {
                if (badge == null || icon == null) {
                    return
                }
                BubbleAlphaAnimator
                    .clear()
                    .alpha(1f)
                    .duration(300)
                    .bind(badge!!, icon!!) {
                        icon!!.setShadowAlpha(it)
                    }
                    .delay(1000)
                    .alpha(0.7f)
                    .duration(300)
                    .bind(badge!!, icon!!) {
                        val shadowAlpha = (it - 0.7f) / 0.3f
                        icon!!.setShadowAlpha(shadowAlpha)
                    }
                    .start()
            }

            override fun onDown(v: View) {
                if (badge == null || icon == null) {
                    return
                }
                BubbleAlphaAnimator
                    .clear()
                    .alpha(1f)
                    .duration(300)
                    .bind(badge!!, icon!!) {
                        val shadowAlpha = (it - 0.7f) / 0.3f
                        icon!!.setShadowAlpha(shadowAlpha)
                    }
                    .start()
            }

            override fun onUp(v: View) {
                if (badge == null || icon == null) {
                    return
                }
                BubbleAlphaAnimator
                    .clear()
                    .delay(1000)
                    .alpha(0.7f)
                    .duration(300)
                    .bind(badge!!, icon!!) {
                        val shadowAlpha = (it - 0.7f) / 0.3f
                        icon!!.setShadowAlpha(shadowAlpha)
                    }
                    .start()
            }

            override fun getStartMargin(): Float {
                return (-4).dp2px
            }

            override fun getEndMargin(): Float {
                return 8.dp2px
            }

            override fun getTopMargin(): Float {
                return baseContext.getStatusBarHeight().toFloat() + 20.dp2px
            }

            override fun getDefaultPosition(): PointF {
                return PointF(0f, 0f)
            }

            override fun onClick() {
                val intent = Intent(baseContext, DemoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                baseContext.startActivity(intent)
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