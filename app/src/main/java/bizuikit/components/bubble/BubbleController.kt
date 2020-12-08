package bizuikit.components.bubble

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * @author: yyf
 * @date: 2020/11/17
 * @desc:
 */
open class BubbleController(
    private val context: Context
) {

    private val vm: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }


    private val fakeView: FakeView by lazy {
        FakeView(context)
    }

    val layout: BubbleLayout by lazy {
        BubbleLayout(context)
    }
    var bubble: Bubble? = null

    fun registerBubbleConfig(config: BubbleConfig) {
        if (this.bubble != null) {
            throw Throwable("controller already add Bubble!")
        }
        ensureBubbleViewCreated()
        bubble = Bubble(config)
        bubble?.inflate(layout, object : BubbleViewInfoTask.Callback {
            override fun onBubbleViewsReady(bubble: Bubble) {
                layout.addBubble(bubble)
            }
        })
    }


    private fun ensureBubbleViewCreated() {
        try {
            vm.addView(fakeView, getDefaultWindowParams())
            layout.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            vm.addView(
                layout, getDefaultWindowParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun clear() {
        try {
            vm.removeView(layout)
            vm.removeView(fakeView)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }



    companion object {
        fun getDefaultWindowParams(): WindowManager.LayoutParams? {
            return getDefaultWindowParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

        fun getDefaultWindowParams(width: Int, height: Int): WindowManager.LayoutParams? {
            return WindowManager.LayoutParams(
                width,
                height,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_PHONE
                },
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
            ).apply {
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
        }

    }

}