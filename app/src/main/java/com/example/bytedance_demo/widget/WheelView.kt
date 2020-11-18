package com.example.bytedance_demo.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.annotation.IntDef
import java.lang.Float.max
import kotlin.properties.Delegates

interface IWheelEntity {
    val wheelText: String?
}

class WheelView<T : IWheelEntity> : View {

    private val dataList: List<T> = arrayListOf()

    private val scroller: Scroller by lazy { Scroller(context) }

    private val paint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    @TextAlign private val textAlign = DEFAULT_TEXT_ALIGN

    private var textSize = DEFAULT_TEXT_SIZE

    private var lineSpace = DEFAULT_LINE_SPACING

    private var fontMetrics: Paint.FontMetrics by Delegates.notNull()

    private var maxTextWidth = 0.0f

    private var itemHeight = 0.0f

    //是否是弯曲（3D）效果
    private val isCurved = false

    private val visibleCount = DEFAULT_VISIBLE_ITEM

    private val viewConfiguration: ViewConfiguration by lazy {
        ViewConfiguration.get(context)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initValue()
    }

    /**
     * 初始化参数
     */
    private fun initValue() {
        applyTextValue()
        applyTextAlign()
    }

    private fun applyTextValue() {
        paint.textSize = textSize
        fontMetrics = paint.fontMetrics
        for (i in dataList.indices) {
            maxTextWidth = max(maxTextWidth, paint.measureText(dataList[i].wheelText))
        }
        itemHeight = fontMetrics.bottom - fontMetrics.top + lineSpace
    }

    private fun applyTextAlign() {
        when (textAlign) {
            TEXT_ALIGN_LEFT -> paint.textAlign = Paint.Align.LEFT
            TEXT_ALIGN_RIGHT -> paint.textAlign = Paint.Align.RIGHT
            else -> paint.textAlign = Paint.Align.CENTER
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }




    companion object {
        //文字对齐方式
        const val TEXT_ALIGN_LEFT = 0
        const val TEXT_ALIGN_CENTER = 1
        const val TEXT_ALIGN_RIGHT = 2

        val DEFAULT_LINE_SPACING: Float = 2f.dp2px()
        val DEFAULT_TEXT_SIZE: Float = 15f.sp2px()
        val DEFAULT_TEXT_BOUNDARY_MARGIN: Float = 2f.dp2px()
        val DEFAULT_DIVIDER_HEIGHT: Float = 1f.dp2px()
        const val DEFAULT_TEXT_ALIGN = TEXT_ALIGN_CENTER
        const val DEFAULT_NORMAL_TEXT_COLOR = Color.DKGRAY
        const val DEFAULT_SELECTED_TEXT_COLOR = Color.BLACK
        const val DEFAULT_VISIBLE_ITEM = 5
        const val DEFAULT_SCROLL_DURATION = 250
        const val DEFAULT_CLICK_CONFIRM: Long = 120
    }

    @IntDef(TEXT_ALIGN_LEFT, TEXT_ALIGN_CENTER, TEXT_ALIGN_RIGHT)
    @Retention(value = AnnotationRetention.SOURCE)
    annotation class TextAlign

}

fun Number.dp2px() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), Resources.getSystem().displayMetrics)
fun Number.sp2px() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(), Resources.getSystem().displayMetrics)
