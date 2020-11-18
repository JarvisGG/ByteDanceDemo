package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import com.example.bytedance_demo.R
import bizuikit.ComponentDepend

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class LabelComponentDepend : ComponentDepend() {

    override fun name() = "Label"

    override fun getContainerId() = R.layout.container_label

    override fun bindView(activity: Activity, root: ViewGroup) {
    }

}