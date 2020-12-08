package bizuikit.components.bubble

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.View
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/11/18
 * @desc:
 */
open class BubbleService : Service() {

    private var bubbleController: BubbleController? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bubbleController = BubbleController(this)
        bubbleController?.registerBubbleConfig(object : BubbleConfig() {

            override fun getLayoutId() = R.layout.mui_bubble_view

            override fun onBind(view: View?) {

            }

            override fun onDown() {
            }

            override fun onUp() {
            }

            override fun onAttachToWindow() {
                super.onAttachToWindow()
            }

            override fun onDetachFromWindow() {
                super.onDetachFromWindow()
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