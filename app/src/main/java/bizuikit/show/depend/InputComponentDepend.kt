package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import com.example.bytedancedemo.R
import bizuikit.ComponentDepend
import bizuikit.components.button.MUIButton
import bizuikit.components.input.MUIInput

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class InputComponentDepend : ComponentDepend() {

    private var input: MUIInput? = null
    private var input1: MUIInput? = null
    private var error: MUIButton? = null
    private var normal: MUIButton? = null

    override fun name() = "Input"

    override fun getContainerId() = R.layout.container_input

    override fun bindView(activity: Activity, root: ViewGroup) {
        input = root.findViewById(R.id.input)
        input?.editText?.hint = "测试测试测试测试"
        input1 = root.findViewById(R.id.input1)
        input1?.editText?.hint = "测试测试测试测试1"
        error = root.findViewById(R.id.error)
        error?.setOnClickListener {
            input?.reason?.showErrorView("哈哈哈哈")
        }

        normal = root.findViewById(R.id.normal)
        normal?.setOnClickListener {
            input?.reason?.hideReason()
        }

    }

}