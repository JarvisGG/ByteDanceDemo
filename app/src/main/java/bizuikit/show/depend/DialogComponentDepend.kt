package bizuikit.show.depend

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bizuikit.ComponentDepend
import bizuikit.InnerHolder
import bizuikit.components.window.bottomsheet.ISelectItem
import bizuikit.components.window.bottomsheet.MUIBottomSheetSelectedBuilder
import bizuikit.components.window.dialog.MUIDialog
import bizuikit.components.window.dialog.MUIDialogNormalBuilder
import bizuikit.components.window.toast.showIconToast
import bizuikit.components.window.toast.showToast
import com.example.bytedance_demo.R
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class DialogComponentDepend : ComponentDepend(), View.OnClickListener {

    override fun name() = "Dialog & Toast & BottomSheet"

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
                        1 -> showBottomSheet(activity, item.type)
                        2 -> showToast(activity, item.name)
                        3 -> showIconToast(activity, item.name, R.drawable.icon_toast_success)
                        4, 5, 6, 7, 8, 9 -> showDialog(activity, item.type)
                    }
                }

            }
        })
        adapter.setItems(arrayListOf(
            InnerData("单选 Bottom Sheet", 1),
            InnerData("文字 Toast", 2),
            InnerData("图片 Toast", 3),
            InnerData("普通 Dialog", 4),
            InnerData("单个 button Dialog", 5),
            InnerData("带底部 hint Dialog", 6),
            InnerData("文字少 Dialog", 7),
            InnerData("没有文字 Dialog", 8),
            InnerData("没有 title Dialog", 9)
        ))

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(recycler.context)
    }

    var dialog: MUIDialog ? = null

    fun showDialog(activity: Activity, type: Int) {
        dialog = when(type) {
            4 -> MUIDialogNormalBuilder(activity)
                .setTitle("Dialog")
                .setMessage("电商团队Flutter通用UI组件库\n" +
                        "背景\n" +
                        "当前公司内部Flutter建设已趋于完善，使用Flutter技术能够在动态化方面媲美h5而性能、体验方面匹及native。电商各业务也都有一定程度的需求接入Flutter技术，为避免重复造轮子、提高业务开发效率。故在此倡导共建一个电商团队的Flutter通用ui组件库。\n" +
                        "\n" +
                        "各位可以将认为可以作为通用组件功能填写到下方的表格中，我们会评估通用性后在接下来的迭代中逐渐添加。一期会先将电商团队内部沉淀的Flutter通用UI库整合进该组件中。接下来的组件由各团队业务开发中逐渐完善、共同维护。\n" +
                        "\n" +
                        "共建通用UI组件库的意义\n" +
                        "1. 大大降低初期业务开发的成本\n" +
                        "2. 减轻新业务接入Flutter的基建工作\n" +
                        "3. 已有ui组件经过其他团队线上验证，质量有保证\n" +
                        "4. 同个部门，有问题能够直接找到对应同学面对面交流\n" +
                        "5. 公司内部组件，环境配置等更接地气\n" +
                        "为何不使用西瓜、火山等部门的现有组件？\n" +
                        "- 电商业务ui风格和设计与其他团队可能存在较大出入\n" +
                        "- 电商业务往往依附于其他业务某个模块，需要多套ui主题适配，现有西瓜火山基础库不会适配其他业务线ui\n" +
                        "- 沟通成本较大\n" +
                        "- 跨部门参与共同维护的成本较大")
                .setPositiveButton(R.string.button_start_now, this)
                .setNegativeButton(R.string.button_end_now, this)
                .build()
            5 -> MUIDialogNormalBuilder(activity)
                .setTitle("Dialog")
                .setMessage("电商团队Flutter通用UI组件库\n" +
                        "背景\n" +
                        "当前公司内部Flutter建设已趋于完善，使用Flutter技术能够在动态化方面媲美h5而性能、体验方面匹及native。电商各业务也都有一定程度的需求接入Flutter技术，为避免重复造轮子、提高业务开发效率。故在此倡导共建一个电商团队的Flutter通用ui组件库。\n" +
                        "\n" +
                        "各位可以将认为可以作为通用组件功能填写到下方的表格中，我们会评估通用性后在接下来的迭代中逐渐添加。一期会先将电商团队内部沉淀的Flutter通用UI库整合进该组件中。接下来的组件由各团队业务开发中逐渐完善、共同维护。\n" +
                        "\n" +
                        "共建通用UI组件库的意义\n" +
                        "1. 大大降低初期业务开发的成本\n" +
                        "2. 减轻新业务接入Flutter的基建工作\n" +
                        "3. 已有ui组件经过其他团队线上验证，质量有保证\n" +
                        "4. 同个部门，有问题能够直接找到对应同学面对面交流\n" +
                        "5. 公司内部组件，环境配置等更接地气\n" +
                        "为何不使用西瓜、火山等部门的现有组件？\n" +
                        "- 电商业务ui风格和设计与其他团队可能存在较大出入\n" +
                        "- 电商业务往往依附于其他业务某个模块，需要多套ui主题适配，现有西瓜火山基础库不会适配其他业务线ui\n" +
                        "- 沟通成本较大\n" +
                        "- 跨部门参与共同维护的成本较大")
                .setSingleButton(R.string.button_konw, this)
                .build()
            6 -> MUIDialogNormalBuilder(activity)
                .setTitle("Dialog")
                .setMessage("电商团队Flutter通用UI组件库\n" +
                        "背景\n" +
                        "当前公司内部Flutter建设已趋于完善，使用Flutter技术能够在动态化方面媲美h5而性能、体验方面匹及native。电商各业务也都有一定程度的需求接入Flutter技术，为避免重复造轮子、提高业务开发效率。故在此倡导共建一个电商团队的Flutter通用ui组件库。\n" +
                        "\n" +
                        "各位可以将认为可以作为通用组件功能填写到下方的表格中，我们会评估通用性后在接下来的迭代中逐渐添加。一期会先将电商团队内部沉淀的Flutter通用UI库整合进该组件中。接下来的组件由各团队业务开发中逐渐完善、共同维护。\n" +
                        "\n" +
                        "共建通用UI组件库的意义\n" +
                        "1. 大大降低初期业务开发的成本\n" +
                        "2. 减轻新业务接入Flutter的基建工作\n" +
                        "3. 已有ui组件经过其他团队线上验证，质量有保证\n" +
                        "4. 同个部门，有问题能够直接找到对应同学面对面交流\n" +
                        "5. 公司内部组件，环境配置等更接地气\n" +
                        "为何不使用西瓜、火山等部门的现有组件？\n" +
                        "- 电商业务ui风格和设计与其他团队可能存在较大出入\n" +
                        "- 电商业务往往依附于其他业务某个模块，需要多套ui主题适配，现有西瓜火山基础库不会适配其他业务线ui\n" +
                        "- 沟通成本较大\n" +
                        "- 跨部门参与共同维护的成本较大")
                .setPositiveButton(R.string.button_start_now, this)
                .setNegativeButton(R.string.button_end_now, this)
                .setHint("若有需要，这里是辅助文字")
                .build()
            7 -> MUIDialogNormalBuilder(activity)
                .setTitle("Dialog")
                .setMessage("电商团队Flutter通用UI组件库")
                .setPositiveButton(R.string.button_start_now, this)
                .setNegativeButton(R.string.button_end_now, this)
                .build()
            8 -> MUIDialogNormalBuilder(activity)
                .setMessage("电商团队Flutter通用UI组件库")
                .setPositiveButton(R.string.button_start_now, this)
                .setNegativeButton(R.string.button_end_now, this)
                .build()
            9 -> MUIDialogNormalBuilder(activity)
                .setTitle("Dialog")
                .setPositiveButton(R.string.button_start_now, this)
                .setNegativeButton(R.string.button_end_now, this)
                .build()
            else -> null
        }
        dialog?.show()
    }

    fun showBottomSheet(activity: Activity, type: Int) {
        val bottomSheetDialog = MUIBottomSheetSelectedBuilder<InnerSelectData>(activity)
            .setTitle("单选 Bottom Sheet")
            .setHint("若有需要，这里是辅助文字")
            .setData(arrayListOf(
                InnerSelectData("Select1"),
                InnerSelectData("Select2"),
                InnerSelectData("Select3"),
                InnerSelectData("Select4"),
                InnerSelectData("Select5"),
                InnerSelectData("Select6"),
                InnerSelectData("Select7"),
                InnerSelectData("Select8"),
                InnerSelectData("Select9"),
                InnerSelectData("Select10"),
                InnerSelectData("Select11"),
                InnerSelectData("Select12"),
                InnerSelectData("Select13"),
                InnerSelectData("Select14"),
                InnerSelectData("Select15"),
                InnerSelectData("Select16")
            ), 0)
            .setCommitButton("Submit") {
                showToast(activity, it.toString())
            }
            .build()
        bottomSheetDialog.show()
    }

    class InnerData(
        val name: String,
        val type: Int
    )

    class InnerSelectData(override var content: String) : ISelectItem

    override fun onClick(v: View?) {
        dialog?.dismiss()
    }

}