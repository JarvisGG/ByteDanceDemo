package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import com.example.bytedancedemo.R
import bizuikit.ComponentDepend
import bizuikit.components.search.MUISearchBar

/**
 * @author: yyf
 * @date: 2020/9/3
 */
class SearchBarComponentDepend : ComponentDepend() {

    private var searchBar: MUISearchBar? = null

    override fun name() = "SearchBar"

    override fun getContainerId() = R.layout.container_search_bar

    override fun bindView(activity: Activity, root: ViewGroup) {
        searchBar = root.findViewById(R.id.search_bar)
        searchBar?.etSearch?.hint = "测试测试测试"
    }

}