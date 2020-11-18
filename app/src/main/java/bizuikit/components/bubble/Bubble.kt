package bizuikit.components.bubble

import android.os.AsyncTask

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
class Bubble {

    var bubbleView: BubbleView? = null

    private var inflationTask: BubbleViewInfoTask? = null

    private var inflateSynchronously = false

    fun inflate(layout: BubbleLayout, callback: BubbleViewInfoTask.Callback) {

        layout.post {
            if (isBubbleLoading()) {
                inflationTask?.cancel(true)
            }
            inflationTask = BubbleViewInfoTask(
                 this, layout, callback
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