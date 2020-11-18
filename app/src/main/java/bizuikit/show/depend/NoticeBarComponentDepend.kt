package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import com.example.bytedance_demo.R
import bizuikit.ComponentDepend

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class NoticeBarComponentDepend : ComponentDepend() {

    override fun name() = "NoticeBar"

    override fun getContainerId() = R.layout.container_noticebar

    override fun bindView(activity: Activity, root: ViewGroup) {

    }

}