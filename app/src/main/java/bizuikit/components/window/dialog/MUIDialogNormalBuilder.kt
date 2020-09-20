package bizuikit.components.window.dialog

import android.app.Activity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import bizuikit.components.button.MUIButton
import bizuikit.utils.dp2px
import bizuikit.utils.inflate
import com.example.bytedancedemo.R


/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
class MUIDialogNormalBuilder(
    private val activity: Activity
): MUIDialogBuilder(activity) {


    private var title = ""

    private var message = ""

    private var hint = ""

    private var isSingleBtn = false
    private var isBtnGroup = false

    private var singleBtnText = ""
    private var singleBtnListener : View.OnClickListener? = null

    private var positiveBtnText = ""
    private var positiveBtnListener : View.OnClickListener? = null

    private var negativeBtnText = ""
    private var negativeBtnListener : View.OnClickListener? = null


    init {
        setRadius(6.dp2px.toInt())
        setContainerId(R.layout.mui_dialog_container_normal)
    }

    /**
     * 标题
     */
    fun setTitle(title: String): MUIDialogNormalBuilder {
        this.title = title
        return this
    }


    /**
     * 添加标题
     */
    fun setTitleRes(titleId: Int): MUIDialogNormalBuilder {
        setTitle(activity.getText(titleId).toString())
        return this
    }

    /**
     * 文字信息
     */
    fun setMessage(message: String): MUIDialogNormalBuilder {
        this.message = message
        return this
    }

    /**
     * 文字信息
     */
    fun setMessageRes(messageId: Int): MUIDialogNormalBuilder {
        setMessage(activity.getText(messageId).toString())
        return this
    }

    /**
     * 标题
     */
    fun setHint(hint: String): MUIDialogNormalBuilder {
        this.hint = hint
        return this
    }


    /**
     * 添加标题
     */
    fun setHintRes(hintId: Int): MUIDialogNormalBuilder {
        setHint(activity.getText(hintId).toString())
        return this
    }

    /**
     * 设置单一按钮
     */
    fun setSingleButton(textId: Int, listener: View.OnClickListener): MUIDialogNormalBuilder {
        setSingleButton(activity.getText(textId).toString(), listener)
        return this
    }

    /**
     * 设置单一按钮
     */
    fun setSingleButton(text: String, listener: View.OnClickListener): MUIDialogNormalBuilder {
        isSingleBtn = true
        isBtnGroup = false
        singleBtnText = text
        singleBtnListener = listener
        return this
    }


    fun setPositiveButton(textId: Int, listener: View.OnClickListener): MUIDialogNormalBuilder {
        setPositiveButton(activity.getText(textId).toString(), listener)
        return this
    }

    fun setPositiveButton(text: String, listener: View.OnClickListener): MUIDialogNormalBuilder {
        isSingleBtn = false
        isBtnGroup = true
        positiveBtnText = text
        positiveBtnListener = listener
        return this
    }


    fun setNegativeButton(textId: Int, listener: View.OnClickListener): MUIDialogNormalBuilder {
        setNegativeButton(activity.getText(textId).toString(), listener)
        return this
    }

    fun setNegativeButton(text: String, listener: View.OnClickListener): MUIDialogNormalBuilder {
        isSingleBtn = false
        isBtnGroup = true
        negativeBtnText = text
        negativeBtnListener = listener
        return this
    }

    /**
     * 添加 Container
     */
    override fun addContainer(root: LinearLayout?) {
        if (root == null) {
            return
        }
        val titleView = root.findViewById<TextView>(R.id.tv_title)
        titleView.text = title
        if (title.isNotEmpty()) {
            titleView.visibility = View.VISIBLE
        } else {
            titleView.visibility = View.GONE
        }

        val messageView = root.findViewById<TextView>(R.id.tv_message)
        messageView.text = message
        // 滚起来
        messageView.movementMethod = ScrollingMovementMethod()
        if (message.isNotEmpty()) {
            messageView.visibility = View.VISIBLE
        } else {
            messageView.visibility = View.GONE
        }
        val gradientView = root.findViewById<View>(R.id.v_gradient)

        messageView.post {
            if (messageView.layout != null && messageView.layout.height > messageView.measuredHeight) {
                gradientView.visibility = View.VISIBLE
            } else {
                gradientView.visibility = View.GONE
            }
        }

        messageView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY >= messageView.layout.height - messageView.measuredHeight - 4.dp2px) {
                gradientView.visibility = View.GONE
            } else {
                gradientView.visibility = View.VISIBLE
            }
        }

        val hintView = root.findViewById<TextView>(R.id.tv_hint)
        hintView.text = hint
        if (hint.isNotEmpty()) {
            hintView.visibility = View.VISIBLE
        } else {
            hintView.visibility = View.GONE
        }

        val singleBtn = root.findViewById<MUIButton>(R.id.mb_single)
        val btnGroup = root.findViewById<LinearLayout>(R.id.ll_group)
        val negativeBtn = root.findViewById<MUIButton>(R.id.mb_negative)
        val positiveBtn = root.findViewById<MUIButton>(R.id.mb_positive)
        when {
            isSingleBtn -> {
                singleBtn.visibility = View.VISIBLE
                singleBtn.text = singleBtnText
                singleBtn.setOnClickListener(singleBtnListener)
                btnGroup.visibility = View.GONE
            }
            isBtnGroup -> {
                singleBtn.visibility = View.GONE
                btnGroup.visibility = View.VISIBLE

                negativeBtn.text = negativeBtnText
                negativeBtn.setOnClickListener(negativeBtnListener)

                positiveBtn.text = positiveBtnText
                positiveBtn.setOnClickListener(positiveBtnListener)
            }
            else -> {
                singleBtn.visibility = View.GONE
                btnGroup.visibility = View.GONE
            }
        }

    }



}

