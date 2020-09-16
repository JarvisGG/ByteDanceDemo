package bizuikit.components.bar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import bizuikit.components.bar.TitleBar.LayoutParams.Companion.CENTER
import bizuikit.components.bar.TitleBar.LayoutParams.Companion.HORIZONTAL_GRAVITY_MASK
import bizuikit.components.bar.TitleBar.LayoutParams.Companion.LEFT
import bizuikit.components.bar.TitleBar.LayoutParams.Companion.RIGHT
import bizuikit.components.bar.TitleBar.LayoutParams.Companion.UNSPECIFIED_PAIR
import bizuikit.components.bar.TitleBar.LayoutParams.Companion.getAbsolutePart
import com.example.bytedancedemo.R
import bizuikit.utils.sp2px
import java.util.*
import kotlin.math.max

/**
 * @author: yyf
 * @date: 2020/7/28
 * @desc: 通用标题栏
<bizuikit.components.bar.TitleBar
...
app:textSize="17sp"
app:textColor="@color/im_main_black"
app:textTitle="@string/im_receive_num">

<View
...
app:layout_part="left" />

<View
...
app:layout_part="right" />

</bizuikit.components.bar.TitleBar>
 */
class TitleBar : ViewGroup {

    private var title = ""
        set(value) {
            field = value
            invalidate()
        }

    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    private var textColor = Color.DKGRAY
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    private var textSize = 0f
        set(value) {
            field = value
            paint.textSize = value
            invalidate()
        }

    var isHasCenterView = false

    private var parentLeft: Int = 0
    private var parentRight: Int = 0
    private var parentTop: Int = 0
    private var parentBottom: Int = 0

    private val matchParentChildren = ArrayList<View>(1)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyAttrs(attrs)
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        context.obtainStyledAttributes(attrs, R.styleable.TitleBar).run {
            textColor = getColor(R.styleable.TitleBar_textColor, Color.DKGRAY)
            textSize = getDimension(R.styleable.TitleBar_textSize, 15f.sp2px)
            title = getText(R.styleable.TitleBar_textTitle).toString()
            recycle()
        }
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(lp)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        matchParentChildren.clear()
        var maxHeight = 0
        var maxWidth = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                val lp = child.layoutParams as LayoutParams

                if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT ||
                        lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    matchParentChildren.add(child)
                } else {
                    maxWidth = max(maxWidth, child.measuredWidth + lp.leftMargin + lp.rightMargin)
                    maxHeight = max(maxHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)
                }
            }
        }

        maxWidth += paddingLeft + paddingRight
        maxHeight += paddingTop + paddingBottom

        maxHeight = max(maxHeight, suggestedMinimumHeight)
        maxWidth = max(maxWidth, suggestedMinimumWidth)

        setMeasuredDimension(
                View.resolveSize(maxWidth, widthMeasureSpec),
                View.resolveSize(maxHeight, heightMeasureSpec)
        )

        val count = matchParentChildren.size
        if (count > 1) {
            for (i in 0 until count) {
                val child: View = matchParentChildren[i]
                val lp = child.layoutParams as MarginLayoutParams
                val childWidthMeasureSpec: Int
                childWidthMeasureSpec = if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    val width = max(0, measuredWidth - paddingLeft - paddingRight - lp.leftMargin - lp.rightMargin)
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                } else {
                    getChildMeasureSpec(widthMeasureSpec,paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin, lp.width)
                }
                val childHeightMeasureSpec: Int
                childHeightMeasureSpec = if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    val height = max(0, measuredHeight - paddingTop - paddingBottom - lp.topMargin - lp.bottomMargin)
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                } else {
                    getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin, lp.height)
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        parentLeft = paddingLeft
        parentRight = right - left - paddingRight

        parentTop = paddingTop
        parentBottom = bottom - top - paddingBottom


        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {

                val width = child.measuredWidth
                val height = child.measuredHeight

                var childLeft: Int
                var childTop: Int

                val lp = child.layoutParams as LayoutParams
                if (lp.part == UNSPECIFIED_PAIR) {
                    lp.part = LEFT
                }
                if (lp.part == CENTER) {
                    isHasCenterView = true
                }

                val absolutePart = getAbsolutePart(lp.part, layoutDirection)

                childTop = parentTop + (parentBottom - parentTop - height) / 2 + lp.topMargin - lp.bottomMargin
                childLeft = when (absolutePart and HORIZONTAL_GRAVITY_MASK) {
                    CENTER -> parentLeft + (parentRight - parentLeft - width) / 2 + lp.leftMargin - lp.rightMargin
                    RIGHT -> parentRight - width - lp.rightMargin
                    else -> parentLeft + lp.leftMargin
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isHasCenterView) {
            return
        }
        paint.textAlign = Paint.Align.CENTER;
        val fontMetrics = paint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseLineY = measuredHeight / 2f + distance
        val centerX = measuredWidth / 2f
        canvas?.run {
            save()
            drawText(title, centerX, baseLineY, paint)
            restore()
        }
    }

    class LayoutParams : MarginLayoutParams {

        companion object {
            const val UNSPECIFIED_PAIR = -1
            private const val AXIS_SPECIFIED = 0x0001
            private const val AXIS_PULL_BEFORE = 0x0002
            private const val AXIS_PULL_AFTER = 0x0004
            private const val AXIS_X_SHIFT = 0
            private const val AXIS_Y_SHIFT = 4
            const val LEFT = AXIS_PULL_BEFORE or AXIS_SPECIFIED shl AXIS_X_SHIFT
            const val RIGHT = AXIS_PULL_AFTER or AXIS_SPECIFIED shl AXIS_X_SHIFT
            private const val CENTER_VERTICAL = AXIS_SPECIFIED shl AXIS_Y_SHIFT
            private const val CENTER_HORIZONTAL = AXIS_SPECIFIED shl AXIS_X_SHIFT
            const val CENTER = CENTER_VERTICAL or CENTER_HORIZONTAL
            const val HORIZONTAL_GRAVITY_MASK = AXIS_SPECIFIED or AXIS_PULL_BEFORE or AXIS_PULL_AFTER shl AXIS_X_SHIFT
            private const val RELATIVE_LAYOUT_DIRECTION = 0x00800000

            fun getAbsolutePart(part: Int, layoutDirection: Int): Int {
                var result = part
                if (result and RELATIVE_LAYOUT_DIRECTION > 0) {
                    if (result and LEFT == LEFT) {
                        result = result and LEFT.inv()
                        result = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                            result or RIGHT
                        } else {
                            result or LEFT
                        }
                    } else if (result and RIGHT == RIGHT) {
                        result = result and RIGHT.inv()
                        result = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                            result or LEFT
                        } else {
                            result or RIGHT
                        }
                    }
                    result = result and RELATIVE_LAYOUT_DIRECTION.inv()
                }
                return result
            }
        }

        var part = UNSPECIFIED_PAIR
        @SuppressLint("CustomViewStyleable")
        constructor(@NonNull c: Context, @Nullable attrs: AttributeSet?) : super(c, attrs) {
            val a = c.obtainStyledAttributes(attrs, R.styleable.TitleBar)
            part = a.getInt(R.styleable.TitleBar_layout_part, UNSPECIFIED_PAIR)
            a.recycle()
        }
        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, part: Int) : super(width, height) {
            this.part = part
        }
        constructor(@NonNull source: ViewGroup.LayoutParams?) : super(source)
        constructor(@NonNull source: MarginLayoutParams?) : super(source)
        constructor(@NonNull source: LayoutParams) : super(source) {
            this.part = source.part
        }
    }
}

