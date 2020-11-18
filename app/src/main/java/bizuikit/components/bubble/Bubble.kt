package bizuikit.components.bubble

import android.os.AsyncTask
import android.view.LayoutInflater
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
class Bubble {

    var bubbleView: BubbleView? = null

    private var inflationTask: BubbleViewInfoTask? = null

    private var inflateSynchronously = false

    fun inflate(inflater: LayoutInflater, layout: BubbleLayout, callback: BubbleViewInfoTask.Callback) {

        bubbleView = inflater.inflate(R.layout.mui_bubble_view, layout, false) as BubbleView
        layout.post {
            if (isBubbleLoading()) {
                inflationTask?.cancel(true)
            }
            inflationTask = BubbleViewInfoTask(
                layout.context, this, layout, callback
            ).apply {
                if (inflateSynchronously) {
                    onPostExecute(doInBackground())
                } else {
                    execute()
                }
            }
        }
    }

    fun isInflated(): Boolean {
        return bubbleView != null
    }

    private fun isBubbleLoading(): Boolean {
        return inflationTask != null && inflationTask!!.status != AsyncTask.Status.FINISHED
    }

    fun setViewInfo(viewInfo: BubbleViewInfo) {
        this.bubbleView = viewInfo.bubbleView
    }

    fun update(bubbleData: BubbleData) {
        // bind data
    }

}