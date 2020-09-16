package bizuikit.show.depend

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bizuikit.ComponentDepend
import bizuikit.DetailActivity
import bizuikit.UiKitActivity
import bizuikit.components.InnerHolder
import bizuikit.components.dialog.bottomsheet.MUIBottomSheet
import bizuikit.sDepends
import bizuikit.utils.dp2px
import com.example.bytedancedemo.R
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class DialogComponentDepend : ComponentDepend() {

    override fun name() = "Dialog"

    override fun getContainerId() = R.layout.container_dialog

    private val adapter: MultiTypeAdapter by lazy {
        MultiTypeAdapter()
    }

    private lateinit var recycler: RecyclerView

    override fun bindView(activity: Activity, root: ViewGroup) {
        recycler = root.findViewById(R.id.rv_container)

        adapter.register(InnerData::class.java, object : ItemViewBinder<InnerData, InnerHolder>() {
            override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                InnerHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_component, parent, false)
                )

            override fun onBindViewHolder(holder: InnerHolder, item: InnerData, position: Int, itemSize: Int) {
                holder.itemView.findViewById<TextView>(R.id.tv_name).text = item.name
                holder.itemView.setOnClickListener {
                    when (item.type) {
                        1 -> openBottomSheet(activity)
                    }
                }

            }
        })
        adapter.setItems(arrayListOf(
            InnerData("Bottom Sheet", 1),
            InnerData("Toast", 2),
            InnerData("Dialog", 3),
            InnerData("底部弹窗4", 1),
            InnerData("底部弹窗5", 1)
        ))

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)
    }

    fun openBottomSheet(activity: Activity) {
        val bottomSheetDialog = MUIBottomSheet(activity)
        val demoView = RecyclerView(activity)
        val adapter = MultiTypeAdapter()
        adapter.register(InnerData::class.java, object : ItemViewBinder<InnerData, InnerHolder>() {
            override fun onCreateViewHolder(
                inflater: LayoutInflater,
                parent: ViewGroup
            ) = InnerHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_component, parent, false)
            )

            override fun onBindViewHolder(
                holder: InnerHolder,
                item: InnerData,
                position: Int,
                itemSize: Int
            ) {
                holder.itemView.findViewById<TextView>(R.id.tv_name).text = item.name
            }

        })
        adapter.setItems(arrayListOf(
            InnerData("Test1", 1),
            InnerData("Test2", 1),
            InnerData("Test3", 1),
            InnerData("Test4", 1),
            InnerData("Test5", 1),
            InnerData("Test6", 1),
            InnerData("Test7", 1),
            InnerData("Test8", 1),
            InnerData("Test9", 1),
            InnerData("Test10", 1),
            InnerData("Test11", 1),
            InnerData("Test12", 1),
            InnerData("Test13", 1),
            InnerData("Test14", 1),
            InnerData("Test15", 1)
        ))
        demoView.adapter = adapter
        demoView.layoutManager = LinearLayoutManager(recycler.context)
        bottomSheetDialog.addContainer(demoView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 300.dp2px.toInt()
        ))
        bottomSheetDialog.show()
    }

    class InnerData(
        val name: String,
        val type: Int
    )

}