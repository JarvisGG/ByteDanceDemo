package bizuikit.components.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import bizuikit.CircularProgressDrawable
import bizuikit.utils.dp2px
import java.lang.ref.WeakReference

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class MUIButtonHelper : IMUIButtonLoading {

    private var holderView: WeakReference<MUIButton>? = null
    private var backgroundDrawable: MUIButtonBackgroundDrawable? = null
    private var coverDrawable: MUIButtonCoverDrawable? = null
    private var progressDrawable: CircularProgressDrawable? = null
    private var iconDrawable: MUIButtonIconDrawable? = null

    private var originText = ""

    fun init(view: MUIButton, context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        holderView = WeakReference(view)

        backgroundDrawable = MUIButtonBackgroundDrawable.fromAttributeSet(context, attrs, defStyleAttr)
        val padding = intArrayOf(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
        view.background = backgroundDrawable
        view.setPadding(padding[0], padding[1], padding[2], padding[3])


        coverDrawable = MUIButtonCoverDrawable.fromAttributeSet(context, attrs, defStyleAttr)
        coverDrawable?.callback = view

        progressDrawable = CircularProgressDrawable(view.context)
        progressDrawable?.setColorSchemeColors(view.textColors.defaultColor)
        progressDrawable?.setBounds(0, 0, 16.dp2px.toInt(), 16.dp2px.toInt())
        progressDrawable?.strokeWidth = 5f
        progressDrawable?.callback = view

        iconDrawable = MUIButtonIconDrawable.fromAttributeSet(view, context, attrs, defStyleAttr)
        iconDrawable?.addIcon()

    }

    fun setBgData(colorStateList: ColorStateList?) {
        backgroundDrawable?.setBgData(colorStateList)
    }

    fun setBgStrokeData(width: Int, colors: ColorStateList?) {
        backgroundDrawable?.setStrokeData(width, colors)
    }

    fun getBgStrokeWidth(): Int {
        return backgroundDrawable?.getStrokeWidth() ?: 0
    }

    fun setBgStrokeColors(colors: ColorStateList?) {
        backgroundDrawable?.setStrokeColors(colors)
    }

    fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        coverDrawable?.setBounds(left, top, right, bottom)
    }

    fun draw(canvas: Canvas) {
        val view = holderView?.get() ?: return
        if (!view.isEnabled) {
            coverDrawable?.draw(canvas)
        } else {
            val processRect = progressDrawable?.bounds ?: Rect()
            val transX = (view.measuredWidth - processRect.width()) / 2f
            val transY = (view.measuredHeight - processRect.height()) / 2f
            canvas.save()
            canvas.translate(transX, transY)
            progressDrawable?.draw(canvas)
            canvas.restore();
        }
    }

    fun verifyDrawable(who: Drawable): Boolean {
        return who === coverDrawable || who === progressDrawable
    }

    override fun showLoading() {
        save()
        progressDrawable?.start()
    }

    override fun stopLoading() {
        restore()
        progressDrawable?.stop()
    }

    private fun save() {
        val view = holderView?.get() ?: return
        originText = view.text.toString()
        view.text = ""
        iconDrawable?.save()
    }

    private fun restore() {
        val view = holderView?.get() ?: return
        view.text = originText
        iconDrawable?.restore()
    }
}