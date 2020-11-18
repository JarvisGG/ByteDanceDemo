package bizuikit.components.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.IntDef
import bizuikit.utils.dp2px
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIButtonIconDrawable(
    private val view: MUIButton, private val drawable: Drawable?, @IconPosition private val iconPosition: Int
) {

    private var drawables : Array<out Drawable>? = null

    @IntDef(START, TOP, END, BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class IconPosition

    fun addIcon() {
        if (drawable == null) {
            return
        }
        drawable.setBounds(0, 0, 13.dp2px.toInt(), 13.dp2px.toInt())
        view.compoundDrawablePadding = 4.dp2px.toInt()
        when(iconPosition) {
            START -> view.setCompoundDrawables(drawable, null, null, null)
            TOP -> view.setCompoundDrawables(null, drawable, null, null)
            END -> view.setCompoundDrawables(null, null, drawable, null)
            BOTTOM -> view.setCompoundDrawables(null, null, null, drawable)
        }
    }

    fun save() {
        if (drawable == null) {
            return
        }
        drawables = view.compoundDrawables
    }

    fun restore() {
        if (drawable == null) {
            return
        }
        drawables?.let {
            view.setCompoundDrawables(it[0], it[1], it[2], it[3])
        }
    }

    companion object {
        const val START = 0x00
        const val TOP = 0x01
        const val END = 0x10
        const val BOTTOM = 0x11

        fun fromAttributeSet(view: MUIButton, context: Context, attrs: AttributeSet?, defStyleAttr: Int): MUIButtonIconDrawable? {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MUIButton, defStyleAttr, 0)
            val iconDrawable = typedArray.getDrawable(R.styleable.MUIButton_mui_iconSrc)
            val iconTint = typedArray.getColorStateList(R.styleable.MUIButton_mui_iconTint)
            val iconPosition = typedArray.getInt(R.styleable.MUIButton_mui_iconPosition, 0x00)
            typedArray.recycle()
            if (iconTint != null) {
                iconDrawable?.setTintList(iconTint)
            }
            return MUIButtonIconDrawable(view, iconDrawable, iconPosition)
        }
    }

}