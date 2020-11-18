package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import bizuikit.ComponentDepend
import bizuikit.components.control.selection.MUISwitch
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/9/2
 */
class ControlComponentDepend : ComponentDepend() {

    private var enable = true

    private var tvDisable: TextView? = null

    private var sv1: MUISwitch? = null

    private var rb1: RadioButton? = null
    private var rb2: RadioButton? = null

    override fun name() = "Control"

    override fun getContainerId() = R.layout.container_control

    override fun bindView(activity: Activity, root: ViewGroup) {

        tvDisable = root.findViewById(R.id.tv_disable)
        tvDisable?.setOnClickListener {
            enable = !enable
            sv1?.isEnabled = enable
            rb1?.isEnabled = enable
            rb2?.isEnabled = enable
        }

        sv1 = root.findViewById(R.id.sv_1)
        rb1 = root.findViewById(R.id.rb_1)
        rb2 = root.findViewById(R.id.rb_2)

    }

}