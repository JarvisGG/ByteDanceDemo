package bizuikit.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * @author: yyf
 * @date: 2020/9/16
 * @desc:
 */
class InnerHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer
