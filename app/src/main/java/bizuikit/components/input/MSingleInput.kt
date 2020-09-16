package bizuikit.components.input

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import bizuikit.utils.dp2px

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class MSingleInput : LinearLayout {

    private lateinit var editText: AppCompatEditText

    private var attrs: AttributeSet? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyAttrs(attrs)
        initView()
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        this.attrs = attrs
    }

    private fun initView() {
        orientation = HORIZONTAL
        setPadding(12.dp2px.toInt(), 0, 12.dp2px.toInt(), 0)

    }

    private fun addEditor() {


    }


}