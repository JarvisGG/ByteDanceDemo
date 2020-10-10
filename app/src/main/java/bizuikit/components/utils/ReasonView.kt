package bizuikit.components.utils

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import bizuikit.utils.dp2px
import com.example.bytedancedemo.R

/**
 * Created by yangjianjun on 2020/6/11
 * 原因状态view
 * 行展示
 */
class ReasonView : LinearLayout {
    private val mReasonListView: LinearLayout
    private var mErrorView: TextView? = null
    private var mRejectView: TextView? = null
    private val mTopLine :View
    private var mTopLineVisible = false
    private var mShouldHideHost = false
    private var mShotTopLineVisibleWhenReason = true
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        orientation = VERTICAL
        mTopLine = View(context).apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0.5.dp2px.toInt())
            setBackgroundColor(context.getColor(COLOR_TEXT_GRAY))
        }
        addView(mTopLine)
        mTopLine.visibility = if(mTopLineVisible) View.VISIBLE else View.GONE
        mReasonListView = LinearLayout(context)
        mReasonListView.apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = 4.dp2px.toInt()
            }
            setPadding(0, 0, 0, 4.dp2px.toInt())
            orientation = VERTICAL
        }
        addView(mReasonListView)
        mReasonListView.visibility = View.GONE
    }

    fun showErrorView(reason: String) {
        showReasonListView()

        if (mErrorView == null) {
            mErrorView = makeTextView()

        }
        val errorView = mErrorView!!
        val index = mReasonListView.indexOfChild(errorView)
        if (index > 0) {
            mReasonListView.removeView(errorView)
            tryRemoveAgain(errorView)
            mReasonListView.addView(errorView, 0)
        } else if (index < 0) {
            tryRemoveAgain(errorView)
            mReasonListView.addView(errorView, 0)
        }
        errorView.text = reason
    }

    fun showRejectView(reason: String) {
        if (mRejectView == null) {
            mRejectView = makeTextView()
        }
        val rejectView = mRejectView!!
        val index = mReasonListView.indexOfChild(rejectView)
        if (index < 0) {
            tryRemoveAgain(rejectView)
            mReasonListView.addView(rejectView)
        }
        rejectView.apply {
            (layoutParams as? MarginLayoutParams)?.let {

                it.topMargin = (if (mReasonListView.childCount == 2) 8F else 0F).dp2px.toInt()
            }
        }
        rejectView.text = reason
    }

    fun showReasonListView() {

        this.visibility = View.VISIBLE
        mReasonListView.visibility = View.VISIBLE
        if (mShotTopLineVisibleWhenReason) {
            mTopLine.visibility = View.VISIBLE
        }
        // mTopLine.setBackgroundColor(COLOR_TEXT_RED)
        mTopLine.setBackgroundColor(context.getColor(COLOR_TEXT_GRAY))
    }

    fun hideReasonListView(){
        if (mShouldHideHost) {
            this.visibility = View.GONE
        }
        mReasonListView.visibility = View.GONE
        if (mTopLineVisible) {
            mTopLine.visibility = View.VISIBLE
            mTopLine.setBackgroundColor(context.getColor(COLOR_TEXT_GRAY))
        } else {
            mTopLine.visibility = View.GONE
        }
    }

    fun setShouldHideHost(shouldShow: Boolean) {
        this.mShouldHideHost = shouldShow
    }

    fun setTopLineVisible(topLineVisible:Boolean){
        mTopLineVisible = topLineVisible
        mTopLine.visibility = if(topLineVisible) View.VISIBLE else View.GONE
    }

    fun setShotTopLineVisibleWhenReason(mShotWeb:Boolean){
        mShotTopLineVisibleWhenReason = mShotWeb
        mTopLine.visibility = if(mShotWeb) View.VISIBLE else View.GONE
    }

    fun hideRejectView() {
        mRejectView?.let {
            if (mReasonListView.indexOfChild(it) < 0 && it.parent == null) {
                return@let
            }
            mReasonListView.removeView(it)
            tryRemoveAgain(it)
            it.text = ""
        }
    }

    fun hideErrorView() {
        mErrorView?.let {
            if (mReasonListView.indexOfChild(it) < 0 && it.parent == null) {
                return@let
            }
            mReasonListView.removeView(it)
            tryRemoveAgain(it)
            it.text
        }
    }

    fun canHide(): Boolean {
        return mReasonListView.childCount <= 0
    }

    fun removeReasonList() {
        mReasonListView.removeAllViews()
        hideErrorView()
        hideRejectView()
    }

    private fun makeTextView(): TextView {
        val textView = AppCompatTextView(context)
        textView.apply {
            textSize = 11F
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(context.getColor(COLOR_TEXT_RED))
            layoutParams = MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin =0
            }
        }
        return textView
    }

    private fun tryRemoveAgain(view: View) {
        (view.parent as? ViewGroup)?.removeView(view)
    }


    fun hideReason() {
        hideReasonListView()
        removeReasonList()
    }

    fun hideErrorReason() {
        hideErrorView()
        if (canHide()) {
            hideReasonListView()
        }
    }

    fun hideRejectReason() {
        hideRejectView()
        if (canHide()) {
            hideReasonListView()
        }
    }

    companion object {
        private val COLOR_TEXT_RED = R.color.text_color_f24141
        private val COLOR_TEXT_GRAY = R.color.color_EDEEEF
    }
}


