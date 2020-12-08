package bizuikit.components.bubble

import android.os.AsyncTask
import android.view.View

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
class Bubble(
    val config: BubbleConfig
) {

    var bubbleView: View? = null

    private var inflationTask: BubbleViewInfoTask? = null

    private var inflateSynchronously = true

    fun inflate(layout: BubbleLayout, callback: BubbleViewInfoTask.Callback) {
        layout.post {
            if (isBubbleLoading()) {
                inflationTask?.cancel(true)
            }
            inflationTask = BubbleViewInfoTask(
                 this, layout, callback
            ).apply {
                if (inflateSynchronously) {
                    onPostExecute(doInBackground(config))
                } else {
                    execute(config)
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
}