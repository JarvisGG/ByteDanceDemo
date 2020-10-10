package bizuikit.components.control.selection

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import bizuikit.switchview.SwitchButton
import com.example.bytedancedemo.R
import bizuikit.utils.dp2px

/**
 * @author: yyf
 * @date: 2020/9/2
 */
class MUISwitch : SwitchButton {

    @ColorInt
    private var coverColor: Int = 0
    private var coverAlpha: Int = 0
    private var cover: CoverDrawable? = null

    private var attrs: AttributeSet?

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        applyAttrs()
        initCover()
    }

    private fun applyAttrs() {
        if (attrs == null) {
            return
        }
        context.obtainStyledAttributes(attrs, R.styleable.MUISwitch).run {
            coverColor = getColor(R.styleable.MUISwitch_mui_coverColor, Color.WHITE)
            coverAlpha = (getFloat(R.styleable.MUISwitch_mui_coverAlpha, 0f) * 255).toInt()
            recycle()
        }
    }

    private fun initCover() {
        cover = CoverDrawable(coverColor, coverAlpha)
        cover?.callback = this
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            setLayerType(LAYER_TYPE_NONE, null)
        } else {
            setLayerType(LAYER_TYPE_HARDWARE, null)
        }
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        cover?.setBounds(0, 0, width, height)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (!isEnabled) {
            cover?.draw(canvas)
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === cover
    }

    class CoverDrawable(
        coverColor: Int,
        coverAlpha: Int
    ) : Drawable() {
        private val shimmerPaint = Paint()
        private val drawRect = RectF()

        init {
            shimmerPaint.isAntiAlias = true
            shimmerPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
            shimmerPaint.color = coverColor
            shimmerPaint.alpha = coverAlpha
        }

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            drawRect.set(bounds)
        }

        override fun draw(canvas: Canvas) {
            canvas.drawRoundRect(drawRect, 4.dp2px, 4.dp2px, shimmerPaint)
        }

        override fun setAlpha(alpha: Int) {
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
        }

        override fun getOpacity() = PixelFormat.TRANSLUCENT
    }
}