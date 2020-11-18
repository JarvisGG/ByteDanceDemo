package bizuikit.components.bubble

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import com.example.bytedance_demo.R
import java.lang.ref.WeakReference

/**
 * @author: yyf
 * @date: 2020/11/17
 * @desc:
 */
class BubbleViewInfoTask(
    context: Context,
    private val bubble: Bubble,
    bubbleLayout: BubbleLayout,
    private val callback: Callback
) : AsyncTask<Void, Void, BubbleViewInfo>() {

    private var contextWef: WeakReference<Context> = WeakReference(context)
    private var bubbleWef: WeakReference<BubbleLayout> = WeakReference(bubbleLayout)


    interface Callback {
        fun onBubbleViewsReady(bubble: Bubble)
    }

    public override fun doInBackground(vararg params: Void): BubbleViewInfo {
        return BubbleViewInfo.populate(
            contextWef.get(), bubbleWef.get(), bubble
        )
    }

    public override fun onPostExecute(viewInfo: BubbleViewInfo?) {
        if (viewInfo != null) {
            bubble.setViewInfo(viewInfo)
            if (!isCancelled) {
                callback.onBubbleViewsReady(bubble)
            }
        }
    }
}

class BubbleViewInfo {

    var bubbleView: BubbleView? = null

    companion object {
        fun populate(c: Context?, bubbleLayout: BubbleLayout?, bubble: Bubble): BubbleViewInfo {
            val info = BubbleViewInfo()
            if (!bubble.isInflated()) {
                val inflater = c?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                info.bubbleView = inflater?.inflate(
                    R.layout.mui_bubble_view, bubbleLayout, false
                ) as BubbleView
            }
            return info
        }
    }

}