package bizuikit.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/12/14
 * @desc:
 */
class DemoService : Service() {

    private val vm: WindowManager by lazy {
        baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val wrapperView by lazy {
        FrameLayout(baseContext)
    }

    private val remainderView by lazy {
        wrapperView.inflate<View>(R.layout.reminder_view)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        init()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun init() {
        wrapperView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        vm.addView(
            wrapperView, getDefaultWindowParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        )

        wrapperView.addView(remainderView, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        remainderView.findViewById<View>(R.id.mui_server).setOnClickListener {
            val intent = Intent(this, DemoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            dismiss()
        }
        remainderView.findViewById<View>(R.id.v_background).setOnClickListener {
            dismiss()
        }
        remainderView.findViewById<View>(R.id.mui_content).setOnClickListener {}
    }

    private fun dismiss() {
        vm.removeViewImmediate(wrapperView)
        stopSelf()
    }

    override fun onBind(intent: Intent?) = null

    companion object {

        private fun getDefaultWindowParams(width: Int, height: Int): WindowManager.LayoutParams? {
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