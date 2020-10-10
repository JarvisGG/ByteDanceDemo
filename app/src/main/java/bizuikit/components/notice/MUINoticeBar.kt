package bizuikit.components.notice

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import bizuikit.components.layout.MUILayout
import bizuikit.utils.inflate
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/10/9
 * @desc:
 */
class MUINoticeBar : MUILayout {

    private var colorBg : Int = Color.TRANSPARENT
    private var colorContainer : Int = Color.DKGRAY
    private var iconDrawable : Drawable? = null
    private var hasNoticeIcon = true
    private var hasNoticeRightIcon = false
    private var contentTxt = ""

    lateinit var content: TextView
    lateinit var noticeIcon: ImageView
    lateinit var rightIcon: ImageView

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
            colorBg = getColor(R.styleable.MUINoticeBar_mui_backgroundColor, Color.TRANSPARENT)
            colorContainer = getColor(R.styleable.MUINoticeBar_mui_textColor, Color.DKGRAY)
            iconDrawable = getDrawable(R.styleable.MUINoticeBar_mui_iconSrc)
            hasNoticeIcon = getBoolean(R.styleable.MUINoticeBar_mui_hasNoticeIcon, true)
            hasNoticeRightIcon = getBoolean(R.styleable.MUINoticeBar_mui_hasNoticeRightIcon, false)
            contentTxt = getText(R.styleable.MUINoticeBar_mui_textTitle)?.toString() ?: ""
            recycle()
        }
    }

    private fun initView() {
        this.inflate<View>(R.layout.mui_noticebar, true)
        content = findViewById(R.id.tv_content)
        noticeIcon = findViewById(R.id.iv_notice)
        rightIcon = findViewById(R.id.iv_right)
        setBackgroundColor(colorBg)
        content.setTextColor(colorContainer)
        content.text = contentTxt
        noticeIcon.imageTintList = ColorStateList.valueOf(colorContainer)
        rightIcon.imageTintList = ColorStateList.valueOf(colorContainer)

        if (hasNoticeIcon) {
            noticeIcon.visibility = View.VISIBLE
        } else {
            noticeIcon.visibility = View.GONE
        }

        if (hasNoticeRightIcon) {
            rightIcon.visibility = View.VISIBLE
        } else {
            rightIcon.visibility = View.GONE
        }
    }


}