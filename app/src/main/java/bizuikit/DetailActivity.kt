package bizuikit

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import bizuikit.ComponentDepend.Companion.COMPONENT_KEY
import bizuikit.utils.inflate
import com.example.bytedancedemo.R
import kotlinx.android.synthetic.main.activity_detial.*

/**
 * @author: yyf
 * @date: 2020/8/27
 * @desc: 组件展示页面
 */
class DetailActivity : AppCompatActivity() {

    private lateinit var componentKey: String

    private lateinit var depend: ComponentDepend


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityTransUtils.startActivityAnim(this, ActivityTransUtils.TYPE_ACTIVITY_TRANS_NORMAL)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_detial)
        initArguments()
        initView()
    }

    private fun initArguments() {
        componentKey = intent.getStringExtra(COMPONENT_KEY).toString()
        depend = sDepends[componentKey]!!
    }

    private fun initView() {

        fl_container.run {
            removeAllViews()
            addView(inflate(depend.getContainerId()))
        }
    }

    override fun onResume() {
        super.onResume()
        depend.bindView(this, window.decorView.findViewById(android.R.id.content))
    }

    override fun finish() {
        super.finish()
        ActivityTransUtils.finishActivityAnim(this, ActivityTransUtils.TYPE_ACTIVITY_TRANS_NORMAL)
    }
}