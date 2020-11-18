package bizuikit.components.bubble

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
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

    /**
     * 添加气泡
     */
    fun addBubble(bubble: Bubble, bubbleData: BubbleData) {
        if (this.bubble != null) {
            throw Throwable("controller already add Bubble!")
        }
        ensureBubbleViewCreated()
        bubble.inflate(layout, object : BubbleViewInfoTask.Callback {
            override fun onBubbleViewsReady(bubble: Bubble) {
                layout.addBubble(bubble)
                updateBubble(bubbleData)
            }
        })
    }

    /**
     * 更新气泡
     */
    fun updateBubble(bubbleData: BubbleData) {
        bubble?.update(bubbleData)
    }

    private fun ensureBubbleViewCreated() {
        try {
            vm.addView(fakeView, getDefaultWindowParams())
            vm.addView(layout, getDefaultWindowParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            ))
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
                if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
            )
        }

    }

}