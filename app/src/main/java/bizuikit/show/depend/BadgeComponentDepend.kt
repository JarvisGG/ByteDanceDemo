package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import com.example.bytedancedemo.R
import bizuikit.ComponentDepend
import bizuikit.components.badge.MUIBadge
import bizuikit.components.button.MUIButton

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class BadgeComponentDepend : ComponentDepend() {

    private var pointBadge: MUIBadge? = null
    private var numBadge: MUIBadge? = null

    private var add: MUIButton? = null
    private var del: MUIButton? = null

    private var show: MUIButton? = null
    private var hide: MUIButton? = null

    private var count = 0

    override fun name() = "Badge"

    override fun getContainerId() = R.layout.container_badge

    override fun bindView(activity: Activity, root: ViewGroup) {
        pointBadge = root.findViewById(R.id.point)
        numBadge = root.findViewById(R.id.num)
        show = root.findViewById(R.id.show)
        hide = root.findViewById(R.id.hide)
        add = root.findViewById(R.id.add)
        del = root.findViewById(R.id.delete)

        show?.setOnClickListener {
            pointBadge?.showPoint()
            numBadge?.showCount(count)
        }

        hide?.setOnClickListener {
            pointBadge?.hidden()
            numBadge?.hidden()
        }


        add?.setOnClickListener {
            numBadge?.showCount(count++)
        }

        del?.setOnClickListener {
            numBadge?.showCount(count--)
        }

    }

}