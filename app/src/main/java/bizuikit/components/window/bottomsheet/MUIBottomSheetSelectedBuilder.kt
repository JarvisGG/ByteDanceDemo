package bizuikit.components.window.bottomsheet

import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bizuikit.components.button.MUIButton
import bizuikit.utils.dp2px
import bizuikit.utils.screenHeight
import com.example.bytedance_demo.R
import kotlinx.android.extensions.LayoutContainer
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
class MUIBottomSheetSelectedBuilder<T : ISelectItem>(
    private val activity: Activity
) : MUIBottomSheetBuilder(activity) {

    private var title = ""

    private var hint = ""

    /**
     * 是否是单选
     */
    private var isSingleSelected = true

    private var adapter = MultiTypeAdapter()

    private var selectList: List<T> = arrayListOf()

    private var currentSelectedIndex = -1

    private var commitBtnText : String = ""
    private var commitBtnListener : ((index: Int) -> Unit) ?= null


    init {
        setRadius(6.dp2px.toInt())
        setContainerId(R.layout.mui_bottom_sheet_selected)
    }

    /**
     * 标题
     */
    fun setTitle(title: String): MUIBottomSheetSelectedBuilder<T> {
        this.title = title
        return this
    }

    /**
     * 添加标题
     */
    fun setTitleRes(titleId: Int): MUIBottomSheetSelectedBuilder<T> {
        setTitle(activity.getText(titleId).toString())
        return this
    }

    /**
     * 标题
     */
    fun setHint(hint: String): MUIBottomSheetSelectedBuilder<T> {
        this.hint = hint
        return this
    }


    /**
     * 添加标题
     */
    fun setHintRes(hintId: Int): MUIBottomSheetSelectedBuilder<T> {
        setHint(activity.getText(hintId).toString())
        return this
    }

    /**
     *
     */
    fun setData(selectList: List<T>, selectedIndex: Int = -1): MUIBottomSheetSelectedBuilder<T> {
        this.selectList = selectList
        this.currentSelectedIndex = selectedIndex
        return this
    }


    /**
     * 设置提交按钮
     */
    fun setCommitButton(text: String, listener: ((index: Int) -> Unit) ?= null): MUIBottomSheetSelectedBuilder<T> {
        commitBtnText = text
        commitBtnListener = listener
        return this
    }

    override fun addContainer(root: LinearLayout?) {
        if (root == null) {
            return
        }
        // 高度修正
        root.post {
            val screenHeight = root.context.screenHeight().toFloat()
            var rootHeight = root.measuredHeight.toFloat()
            if (rootHeight > screenHeight * 3 / 4) {
                rootHeight = screenHeight * 3 / 4
            }
            val params = root.layoutParams
            params.height = rootHeight.toInt()
            root.layoutParams = params
        }

        root.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            bottomSheet.dismiss()
        }

        initAdapter()
        val rvContent = root.findViewById<RecyclerView>(R.id.rv_content)
        rvContent.layoutManager = LinearLayoutManager(root.context)
        rvContent.adapter = adapter

        val hintView = root.findViewById<TextView>(R.id.tv_hint)
        hintView.text = hint
        if (hint.isNotEmpty()) {
            hintView.visibility = View.VISIBLE
        } else {
            hintView.visibility = View.GONE
        }

        val btCommit = root.findViewById<MUIButton>(R.id.bt_commit)
        if (TextUtils.isEmpty(commitBtnText)) {
            btCommit.visibility = View.GONE
        } else {
            btCommit.visibility = View.VISIBLE
            btCommit.text = commitBtnText
            btCommit.setOnClickListener {
                commitBtnListener?.invoke(currentSelectedIndex)
                bottomSheet.dismiss()
            }
        }

    }

    private fun initAdapter() {
        adapter.register(ISelectItem::class.java, object :
            ItemViewBinder<ISelectItem, SelectHolder>() {
            override fun onCreateViewHolder(
                inflater: LayoutInflater,
                parent: ViewGroup
            ): SelectHolder {
                return SelectHolder(inflater.inflate(R.layout.mui_item_container_selected, parent, false))
            }

            override fun onBindViewHolder(
                holder: SelectHolder,
                item: ISelectItem,
                position: Int,
                itemSize: Int
            ) {
                val isSelected = position == currentSelectedIndex
                val tvContent = holder.itemView.findViewById<TextView>(R.id.tv_content)
                val ivSelected = holder.itemView.findViewById<ImageView>(R.id.iv_selected)
                tvContent.isSelected = isSelected
                if (isSelected) {
                    ivSelected.visibility = View.VISIBLE
                } else {
                    ivSelected.visibility = View.GONE
                }
                holder.itemView.setOnClickListener {
                    notifySelected(position)
                }
            }
        })
        adapter.setItems(selectList)
    }

    private fun notifySelected(index: Int) {
        currentSelectedIndex = index
        adapter.notifyDataSetChanged()

    }

    class SelectHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}


interface ISelectItem{
    var content: String
}