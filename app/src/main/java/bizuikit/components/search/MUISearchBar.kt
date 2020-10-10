package bizuikit.components.search

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import bizuikit.components.input.getTextCount
import bizuikit.components.layout.MUILayout
import bizuikit.utils.inflate
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/10/10
 * @desc:
 */
class MUISearchBar : MUILayout {

    lateinit var etSearch: EditText
    lateinit var tvCancel: TextView
    lateinit var ivClear: ImageView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        applyAttrs(attrs)
        initView()
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        context.obtainStyledAttributes(attrs, R.styleable.MUINoticeBar).run {
            recycle()
        }
    }

    private fun initView() {
        this.inflate<View>(R.layout.mui_search_bar, true)
        etSearch = findViewById(R.id.et_search)
        tvCancel = findViewById(R.id.tv_cancel)
        ivClear = findViewById(R.id.iv_clear)

        etSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val length = getTextCount(s?.toString())
                if (length > 0) {
                    ivClear.visibility = VISIBLE
                    ivClear.setOnClickListener {
                        etSearch.text.clear()
                    }
                } else {
                    ivClear.visibility = GONE
                    ivClear.setOnClickListener(null)
                }
            }

        })
    }
}