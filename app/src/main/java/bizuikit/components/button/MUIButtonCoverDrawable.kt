package bizuikit.components.button

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.example.bytedance_demo.R
import bizuikit.utils.dp2px

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIButtonCoverDrawable : Drawable() {
    private val shimmerPaint = Paint()
    private val drawRect = RectF()

    @ColorInt
    var coverColor: Int = 0
        set(value) {
            field = value
            shimmerPaint.color = coverColor
            invalidateSelf()
        }
    var coverAlpha: Int = 0
        set(value) {
            field = value
            shimmerPaint.alpha = coverAlpha
            invalidateSelf()
        }

    init {
        shimmerPaint.isAntiAlias = true
        shimmerPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
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

    companion object {
        fun fromAttributeSet(context: Context, attrs: AttributeSet?, defStyleAttr: Int): MUIButtonCoverDrawable {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MUIButton, defStyleAttr, 0)
            val coverColor = typedArray.getColor(R.styleable.MUIButton_mui_coverColor, Color.WHITE)
            val coverAlpha = (typedArray.getFloat(R.styleable.MUIButton_mui_coverAlpha, 0f) * 255).toInt()
            typedArray.recycle()
            val cover = MUIButtonCoverDrawable()
            cover.coverColor = coverColor
            cover.coverAlpha = coverAlpha
            return cover
        }
    }
}