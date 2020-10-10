package bizuikit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bizuikit.show.depend.*
import com.example.bytedancedemo.R
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import kotlin.reflect.full.createInstance

/**
 * @author: yyf
 * @date: 2020/8/27
 * @desc: UIKIT 展示页面
 */
@UIComponents(
    ButtonComponentDepend::class,
    ControlComponentDepend::class,
    LabelComponentDepend::class,
    DividerComponentDepend::class,
    DialogComponentDepend::class,
    LayoutComponentDepend::class,
    InputComponentDepend::class,
    NoticeBarComponentDepend::class
)
class UiKitActivity : AppCompatActivity() {

    init {
        parseCreator()
    }

    private fun parseCreator() {
        val params = javaClass.getAnnotation(UIComponents::class.java)
        params?.depends?.forEach {
            val depend = it.createInstance()
            sDepends[depend.name()] = depend
        }
    }

    private val adapter: MultiTypeAdapter by lazy {
        MultiTypeAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityTransUtils.startActivityAnim(this, ActivityTransUtils.TYPE_ACTIVITY_TRANS_NORMAL)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    override fun finish() {
        super.finish()
        ActivityTransUtils.finishActivityAnim(this, ActivityTransUtils.TYPE_ACTIVITY_TRANS_NORMAL)
    }

    private fun initView() {

        adapter.register(ComponentDepend::class.java, object : ItemViewBinder<ComponentDepend, InnerHolder>() {
            override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) = InnerHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_component, parent, false))

            override fun onBindViewHolder(holder: InnerHolder, item: ComponentDepend, position: Int, itemSize: Int) {
                holder.itemView.findViewById<TextView>(R.id.tv_name).text = item.name()
                holder.itemView.setOnClickListener {
                    this@UiKitActivity.startActivity(Intent(this@UiKitActivity, DetailActivity::class.java)
                            .apply { item.processorIntent(this) })
                }

            }
        })

        rv_container.adapter = adapter
        rv_container.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {
        adapter.setItems(sDepends.values.toList().reversed())
    }

}

val sDepends = hashMapOf<String, ComponentDepend>()
