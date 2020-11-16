package bizuikit.components.input

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatEditText
import bizuikit.components.utils.ReasonView
import bizuikit.utils.inflate
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/9/3
 */
typealias TextChangedCallback = (text: String?) -> Unit

class MUIInput : FrameLayout {

    lateinit var editText: AppCompatEditText
    lateinit var reason: ReasonView
    private lateinit var ivClear: ImageView
    private lateinit var tvCount: TextView

    private var textChangedCallback: TextChangedCallback? = null

    @LayoutRes
    private var layoutResIdRes: Int = R.layout.mui_input_single

    /**
     * 是否具有晴空按钮
     */
    private var hasClear = true

    /**
     * 是否开启计数
     */
    private var hasCount = true
    private var maxCount = -1

    private var countWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            if (s == null) {
                ivClear.visibility = View.GONE
            } else {
                ivClear.visibility = if (editText.hasFocus() && s.isNotEmpty()) View.VISIBLE else View.GONE
            }
            val length = getTextCount(s?.toString())
            setCount(length)
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyAttrs(attrs)
        initView()
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        context.obtainStyledAttributes(attrs, R.styleable.MUIInput).run {
            hasClear = getBoolean(R.styleable.MUIInput_mui_hasClear, true)
            hasCount = getBoolean(R.styleable.MUIInput_mui_hasCount, true)
            maxCount = getInt(R.styleable.MUIInput_mui_maxCount, -1)
            layoutResIdRes = getResourceId(R.styleable.MUIInput_layout, R.layout.mui_input_single)
            recycle()
        }
    }

    private fun initView() {
        val container = this.inflate<View>(layoutResIdRes, false)
        addView(container, LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        ))
        editText = findViewById(R.id.rt_content)
        ivClear = findViewById(R.id.iv_clear)
        tvCount = findViewById(R.id.tv_count)
        reason = findViewById(R.id.rv_reason)

        setCount(0)

        editText.filters += EmojiInputFilter()
        if (maxCount != -1) {
            editText.filters += MaxLengthFilter(maxCount)
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            actionId != KeyEvent.KEYCODE_ENTER
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                textChangedCallback?.invoke(s?.toString())
            }
        })

        if (hasClear) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    ivClear.visibility = View.VISIBLE
                } else {
                    ivClear.visibility = View.GONE
                }
            }
            ivClear.visibility = View.VISIBLE
            ivClear.setOnClickListener {
                editText.text?.clear()
            }
        } else {
            editText.onFocusChangeListener = null
            ivClear.visibility = View.GONE
            ivClear.setOnClickListener(null)
        }

        if (hasCount) {
            tvCount.visibility = View.VISIBLE
            editText.addTextChangedListener(countWatcher)
        } else {
            tvCount.visibility = View.GONE
            editText.removeTextChangedListener(countWatcher)
        }
    }

    fun setCount(length: Int) {
        if (maxCount != -1){
            tvCount.text = "$length/$maxCount"
        } else {
            tvCount.text = "$length"
        }
    }

    /**
     * 设置最大
     */
    fun setMaxCount(count: Int) {
        maxCount = count
        invalidate()
    }

    fun setTextChangeCallback(textChangedCallback: TextChangedCallback?) {
        this.textChangedCallback = textChangedCallback
    }


}