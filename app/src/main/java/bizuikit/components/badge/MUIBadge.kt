package bizuikit.components.badge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import bizuikit.components.layout.MUILayout
import bizuikit.utils.inflate
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/10/10
 * @desc:
 */
class MUIBadge : MUILayout {

    private var count = 0
    private var hasNum = false

    private var showBadge = false

    private lateinit var tvCount: TextView
    private lateinit var container: View

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        applyAttrs(attrs)
        refreshView()
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        context.obtainStyledAttributes(attrs, R.styleable.MUIBadge).run {
            hasNum = getBoolean(R.styleable.MUIBadge_mui_hasNum, false)
            count = getInt(R.styleable.MUIBadge_mui_count, 0)
            recycle()
        }
    }

    private fun refreshView() {
        visibility = if (showBadge) {
            View.VISIBLE
        } else {
            View.GONE
        }
        removeAllViews()
        if (hasNum) {
            container = this.inflate(R.layout.mui_num_badge, false)
            tvCount = container as TextView
            if (count > 99) {
                tvCount.text = "99+"
                return
            }
            tvCount.text = count.toString()
        } else {
            container = this.inflate(R.layout.mui_point_badge, false)
        }
        addView(container)
    }

    fun showCount(count: Int) {
        this.count = count
        hasNum = true
        showBadge = true
        refreshView()
    }

    fun showPoint() {
        hasNum = false
        showBadge = true
        refreshView()
    }

    fun hidden() {
        showBadge = false
        refreshView()
    }
}