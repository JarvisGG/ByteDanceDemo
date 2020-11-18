package bizuikit.show.depend

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import bizuikit.ComponentDepend
import bizuikit.components.bubble.BubbleService
import bizuikit.components.bubble.FloatingBubblePermissions
import bizuikit.components.button.MUIButton
import com.example.bytedance_demo.R

/**
 * @author: yyf
 * @date: 2020/11/16
 * @desc:
 */
class BubbleComponentDepend : ComponentDepend() {


    override fun name() = "Bubble"

    override fun getContainerId() = R.layout.container_bubble

    override fun bindView(activity: Activity, root: ViewGroup) {

        FloatingBubblePermissions.startPermissionRequest(activity)

        val btn: MUIButton = root.findViewById(R.id.start_bubble)
        btn.setOnClickListener {
            activity.startService(Intent(activity.applicationContext, BubbleService::class.java))
        }


    }

}