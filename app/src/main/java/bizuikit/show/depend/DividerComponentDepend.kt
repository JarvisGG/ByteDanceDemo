package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import com.example.bytedance_demo.R
import bizuikit.ComponentDepend

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class DividerComponentDepend : ComponentDepend() {

    override fun name() = "Divider"

    override fun getContainerId() = R.layout.container_divider

    override fun bindView(activity: Activity, root: ViewGroup) {
    }

}