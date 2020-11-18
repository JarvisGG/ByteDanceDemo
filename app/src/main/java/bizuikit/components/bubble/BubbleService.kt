package bizuikit.components.bubble

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author: yyf
 * @date: 2020/11/18
 * @desc:
 */
open class BubbleService : Service() {

    private var bubbleController: BubbleController? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bubbleController = BubbleController(this)
        bubbleController?.addBubble(Bubble(), BubbleData("asdasd", 1))
        bubbleController?.updateBubble(BubbleData("1234", 1))
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