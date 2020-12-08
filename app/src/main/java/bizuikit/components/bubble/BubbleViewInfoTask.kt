package bizuikit.components.bubble

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View

import java.lang.ref.WeakReference

/**
 * @author: yyf
 * @date: 2020/11/17
 * @desc:
 */
class BubbleViewInfoTask(
    private val bubble: Bubble,
    bubbleLayout: BubbleLayout,
    private val callback: Callback
) : AsyncTask<BubbleConfig, Void, BubbleViewInfo>() {

    private var bubbleWef: WeakReference<BubbleLayout> = WeakReference(bubbleLayout)
    private var contextWef: WeakReference<Context> = WeakReference(bubbleLayout.context)

    interface Callback {
        fun onBubbleViewsReady(bubble: Bubble)
    }

    public override fun onPostExecute(viewInfo: BubbleViewInfo?) {
        if (viewInfo != null) {
            bubble.setViewInfo(viewInfo)
            if (!isCancelled) {
                callback.onBubbleViewsReady(bubble)
            }
        }
    }

    public override fun doInBackground(vararg params: BubbleConfig): BubbleViewInfo {
        return BubbleViewInfo.populate(
            contextWef.get(), bubbleWef.get(), bubble, params[0]
        )
    }
}

class BubbleViewInfo {

    var bubbleView: View? = null

    companion object {
        fun populate(
            context: Context?,
            bubbleLayout: BubbleLayout?,
            bubble: Bubble,
            config: BubbleConfig
        ): BubbleViewInfo {
            val info = BubbleViewInfo()
            if (!bubble.isInflated()) {
                val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                info.bubbleView = inflater?.inflate(
                    config.getLayoutId(), bubbleLayout, false
                )
                config.onBind(info.bubbleView)
                info.bubbleView?.addOnAttachStateChangeListener(object :
                    View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View?) {
                        config.onAttachToWindow()
                    }

                    override fun onViewDetachedFromWindow(v: View?) {
                        config.onDetachFromWindow()
                    }
                })
            }
            return info
        }
    }

}