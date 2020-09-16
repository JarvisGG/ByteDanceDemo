package bizuikit.components.common

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_BOTTOM
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_LEFT
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_NONE
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_RIGHT
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_TOP
import com.example.bytedancedemo.R
import java.lang.ref.WeakReference

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIShapeHelper : IMUIShapeLayout {

    companion object {
        const val RADIUS_OF_HALF_VIEW_HEIGHT = -1
        const val RADIUS_OF_HALF_VIEW_WIDTH = -2
    }

    // round
    private var clipPaint = Paint()
    private var radius = 0
    @IMUIShapeLayout.HideRadiusSide
    private var hideRadiusSide = HIDE_RADIUS_SIDE_NONE
    private var radiusArray = FloatArray(8)
    private var shouldUseRadiusArray = false
    private var borderRect = RectF()
    private var borderColor = 0
    private var borderWidth = 1
    private val path = Path()

    // shadow
    private var isShowBorderOnlyBeforeL = true
    private var shadowElevation = 0
    private var shadowAlpha = 0f
    private var shadowColor = Color.BLACK

    private lateinit var context: Context
    private lateinit var owner: WeakReference<View>


    fun init(
        view: View,
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        this.context = context
        owner = WeakReference(view)
        clipPaint = Paint().apply {
            isAntiAlias = true
        }
        borderRect = RectF()
        var useThemeGeneralShadowElevation = false

        if (null != attrs || defStyleAttr != 0 || defStyleRes != 0) {
            val ta =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.MUILayout,
                    defStyleAttr,
                    defStyleRes
                )
            val count = ta.indexCount
            for (i in 0 until count) {
                when (val index = ta.getIndex(i)) {
                    R.styleable.MUILayout_mui_borderColor -> {
                        borderColor = ta.getColor(index, borderColor)
                    }
                    R.styleable.MUILayout_mui_borderWidth -> {
                        borderWidth = ta.getDimensionPixelSize(index, borderWidth)
                    }
                    R.styleable.MUILayout_mui_radius -> {
                        radius = ta.getDimensionPixelSize(index, 0)
                    }
                    R.styleable.MUILayout_mui_hideRadiusSide -> {
                        hideRadiusSide = ta.getInt(index, hideRadiusSide)
                    }
                    R.styleable.MUILayout_mui_showBorderOnlyBeforeL -> {
                        isShowBorderOnlyBeforeL = ta.getBoolean(index, isShowBorderOnlyBeforeL)
                    }
                    R.styleable.MUILayout_mui_shadowElevation -> {
                        shadowElevation = ta.getDimensionPixelSize(index, shadowElevation)
                    }
                    R.styleable.MUILayout_mui_shadowAlpha -> {
                        shadowAlpha = ta.getFloat(index, shadowAlpha)
                    }
                    R.styleable.MUILayout_mui_useThemeGeneralShadowElevation -> {
                        useThemeGeneralShadowElevation = ta.getBoolean(index, false)
                    }
                }
            }
            ta.recycle()
        }
        if (shadowElevation == 0 && useThemeGeneralShadowElevation) {
            shadowElevation = MUIResHelper.getAttrDimen(
                context,
                R.attr.mui_general_shadow_elevation
            )
        }
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
    }

    fun setUseThemeGeneralShadowElevation() {
        shadowElevation = MUIResHelper.getAttrDimen(context, R.attr.mui_general_shadow_elevation)
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(radius: Int, shadowElevation: Int, shadowAlpha: Float) {
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        @IMUIShapeLayout.HideRadiusSide hideRadiusSide: Int,
        shadowElevation: Int,
        shadowAlpha: Float
    ) {
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowColor, shadowAlpha)
    }

    override fun setRadiusAndShadow(
        radius: Int,
        hideRadiusSide: Int,
        shadowElevation: Int,
        shadowColor: Int,
        shadowAlpha: Float
    ) {
        val owner: View = owner.get() ?: return
        this.radius = radius
        this.hideRadiusSide = hideRadiusSide
        shouldUseRadiusArray = isRadiusWithSideHidden()
        this.shadowElevation = shadowElevation
        this.shadowAlpha = shadowAlpha
        this.shadowColor = shadowColor

        if (useFeature()) {
            if (shadowElevation == 0 || shouldUseRadiusArray) {
                owner.elevation = 0f
            } else {
                owner.elevation = shadowElevation.toFloat()
            }
            setShadowColorInner(shadowColor)
            owner.outlineProvider = object : ViewOutlineProvider() {
                @TargetApi(21)
                override fun getOutline(view: View, outline: Outline) {
                    val w = view.width
                    val h = view.height
                    if (w == 0 || h == 0) {
                        return
                    }
                    var radius: Float = getRealRadius().toFloat()
                    val min = w.coerceAtMost(h)
                    if (radius * 2 > min) {
                        radius = min / 2f
                    }
                    var left = 0
                    var top = 0
                    var right = w
                    var bottom = h
                    // 切脚
                    if (shouldUseRadiusArray) {

                        when (hideRadiusSide) {
                            HIDE_RADIUS_SIDE_LEFT -> left -= radius.toInt()
                            HIDE_RADIUS_SIDE_TOP -> top -= radius.toInt()
                            HIDE_RADIUS_SIDE_RIGHT -> right += radius.toInt()
                            HIDE_RADIUS_SIDE_BOTTOM -> bottom += radius.toInt()
                        }
                        outline.setRoundRect(left, top, right, bottom, radius)
                        return
                    }
                    var shadowAlpha: Float = shadowAlpha
                    if (shadowElevation == 0) {
                        shadowAlpha = 1f
                    }
                    outline.alpha = shadowAlpha
                    if (radius <= 0) {
                        outline.setRect(left, top, right, bottom)
                    } else {
                        outline.setRoundRect(left, top, right, bottom, radius)
                    }
                }
            }
            owner.clipToOutline = radius == RADIUS_OF_HALF_VIEW_WIDTH || radius == RADIUS_OF_HALF_VIEW_HEIGHT || radius > 0
            owner.invalidateOutline()
        }
        owner.invalidate()
    }

    private fun setShadowColorInner(shadowColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val owner: View = owner.get() ?: return
            owner.outlineAmbientShadowColor = shadowColor
            owner.outlineSpotShadowColor = shadowColor
        }
    }

    fun isRadiusWithSideHidden(): Boolean {
        return (radius == RADIUS_OF_HALF_VIEW_HEIGHT || radius == RADIUS_OF_HALF_VIEW_WIDTH || radius > 0) && hideRadiusSide != HIDE_RADIUS_SIDE_NONE
    }

    private fun getRealRadius(): Int {
        val owner: View = owner.get() ?: return radius
        return when (this.radius) {
            RADIUS_OF_HALF_VIEW_HEIGHT -> {
                owner.height / 2
            }
            RADIUS_OF_HALF_VIEW_WIDTH -> {
                owner.width / 2
            }
            else -> {
                this.radius
            }
        }
    }

    override fun setShadowElevation(shadowElevation: Int) {
        if (this.shadowElevation == shadowElevation) {
            return
        }
        this.shadowElevation = shadowElevation
        invalidateOutline()
    }

    override fun setShadowAlpha(shadowAlpha: Float) {
        if (this.shadowAlpha == shadowAlpha) {
            return
        }
        this.shadowAlpha = shadowAlpha
        invalidateOutline()
    }

    override fun setShadowColor(shadowColor: Int) {
        if (this.shadowColor == shadowColor) {
            return
        }
        this.shadowColor = shadowColor
        setShadowColorInner(shadowColor)
    }

    override fun setRadius(radius: Int) {
        if (this.radius != radius) {
            setRadiusAndShadow(radius, shadowElevation, shadowAlpha)
        }
    }

    override fun setRadius(radius: Int, @IMUIShapeLayout.HideRadiusSide hideRadiusSide: Int) {
        if (this.radius == radius && this.hideRadiusSide == hideRadiusSide) {
            return
        }
        setRadiusAndShadow(radius, hideRadiusSide, shadowElevation, shadowAlpha)
    }

    private fun invalidateOutline() {
        if (useFeature()) {
            val owner: View = owner.get() ?: return
            if (shadowElevation == 0) {
                owner.elevation = 0f
            } else {
                owner.elevation = shadowElevation.toFloat()
            }
            owner.invalidateOutline()
        }
    }

    private fun invalidate() {
        val owner: View = owner.get() ?: return
        owner.invalidate()
    }

    fun dispatchRoundBorderDraw(canvas: Canvas?) {
        val owner: View = owner.get() ?: return
        if (canvas == null) {
            return
        }
        val needDrawBorder = borderWidth > 0 && borderColor != 0
        if (!needDrawBorder) {
            return
        }
        if (useFeature() && shadowElevation != 0) {
            return
        }

        val width = canvas.width
        val height = canvas.height

        val halfBorderWith: Float = borderWidth / 2f
        borderRect.set(
            halfBorderWith,
            halfBorderWith,
            width - halfBorderWith,
            height - halfBorderWith
        )

        val radius = getRealRadius().toFloat()

        if (shouldUseRadiusArray) {
            when (hideRadiusSide) {
                HIDE_RADIUS_SIDE_TOP -> {
                    radiusArray[4] = radius
                    radiusArray[5] = radius
                    radiusArray[6] = radius
                    radiusArray[7] = radius
                }
                HIDE_RADIUS_SIDE_RIGHT -> {
                    radiusArray[0] = radius
                    radiusArray[1] = radius
                    radiusArray[6] = radius
                    radiusArray[7] = radius
                }
                HIDE_RADIUS_SIDE_BOTTOM -> {
                    radiusArray[0] = radius
                    radiusArray[1] = radius
                    radiusArray[2] = radius
                    radiusArray[3] = radius
                }
                HIDE_RADIUS_SIDE_LEFT -> {
                    radiusArray[2] = radius
                    radiusArray[3] = radius
                    radiusArray[4] = radius
                    radiusArray[5] = radius
                }
            }
        }

        canvas.save()
        canvas.translate(owner.scrollX.toFloat(), owner.scrollY.toFloat())
        if (needDrawBorder) {
            clipPaint.color = borderColor
            clipPaint.strokeWidth = borderWidth.toFloat()
            clipPaint.style = Paint.Style.STROKE
            when {
                shouldUseRadiusArray -> drawRoundRect(canvas, borderRect, radiusArray, clipPaint)
                radius <= 0 -> canvas.drawRect(borderRect, clipPaint)
                else -> canvas.drawRoundRect(borderRect, radius, radius, clipPaint)
            }
        }
        canvas.restore()
    }

    private fun drawRoundRect(canvas: Canvas, rect: RectF, radiusArray: FloatArray, paint: Paint) {
        path.reset()
        path.addRoundRect(rect, radiusArray, Path.Direction.CW)
        canvas.drawPath(path, paint)
    }

    private fun useFeature(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }


}