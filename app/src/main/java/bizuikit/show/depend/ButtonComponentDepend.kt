package bizuikit.show.depend

import android.app.Activity
import android.view.ViewGroup
import android.widget.TextView
import bizuikit.ComponentDepend
import bizuikit.components.button.MUIButton
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/8/27
 * @desc: Button 组件展示
 */
class ButtonComponentDepend : ComponentDepend() {

    override fun name() = "Button"

    override fun getContainerId() = R.layout.container_button

    private var tvLoading: TextView? = null
    private var tvDisable: TextView? = null

    private var large1: MUIButton? = null
    private var large2: MUIButton? = null
    private var large3: MUIButton? = null
    private var large4: MUIButton? = null

    private var medium1: MUIButton? = null
    private var medium2: MUIButton? = null
    private var medium3: MUIButton? = null
    private var medium4: MUIButton? = null
    private var mediumIcon1: MUIButton? = null
    private var mediumIcon2: MUIButton? = null
    private var mediumIcon3: MUIButton? = null
    private var mediumIcon4: MUIButton? = null

    private var small1: MUIButton? = null
//    private var small2: MButton? = null
//    private var small3: MButton? = null
//    private var small4: MButton? = null
    private var smallIcon1: MUIButton? = null
//    private var smallIcon2: MButton? = null
//    private var smallIcon3: MButton? = null
//    private var smallIcon4: MButton? = null

    private var text1: MUIButton? = null
    private var text2: MUIButton? = null
    private var text3: MUIButton? = null
    private var text4: MUIButton? = null

    private var enable = true
    private var loading = true

    override fun bindView(activity: Activity, root: ViewGroup) {
        tvLoading = root.findViewById(R.id.tv_large_loading)
        tvLoading?.setOnClickListener {
            if (loading) {
                large1?.showLoading()
                large2?.showLoading()
                large3?.showLoading()
                large4?.showLoading()
            } else {
                large1?.stopLoading()
                large2?.stopLoading()
                large3?.stopLoading()
                large4?.stopLoading()
            }
            loading = !loading
        }

        tvDisable = root.findViewById(R.id.tv_disable)
        tvDisable?.setOnClickListener {
            enable = !enable
            large1?.isEnabled = enable
            large2?.isEnabled = enable
            large3?.isEnabled = enable
            large4?.isEnabled = enable

            medium1?.isEnabled = enable
            medium2?.isEnabled = enable
            medium3?.isEnabled = enable
            medium4?.isEnabled = enable
            mediumIcon1?.isEnabled = enable
            mediumIcon2?.isEnabled = enable
            mediumIcon3?.isEnabled = enable
            mediumIcon4?.isEnabled = enable

            small1?.isEnabled = enable
//            small2?.isEnabled = enable
//            small3?.isEnabled = enable
//            small4?.isEnabled = enable
            smallIcon1?.isEnabled = enable
//            smallIcon2?.isEnabled = enable
//            smallIcon3?.isEnabled = enable
//            smallIcon4?.isEnabled = enable

            text1?.isEnabled = enable
            text2?.isEnabled = enable
            text3?.isEnabled = enable
            text4?.isEnabled = enable
        }

        large1 = root.findViewById(R.id.large_stress_btn)
        large2 = root.findViewById(R.id.large_default_btn)
        large3 = root.findViewById(R.id.large_sub_btn)
        large4 = root.findViewById(R.id.large_waring_btn)

        medium1 = root.findViewById(R.id.medium_stress_btn)
        medium2 = root.findViewById(R.id.medium_default_btn)
        medium3 = root.findViewById(R.id.medium_sub_btn)
        medium4 = root.findViewById(R.id.medium_waring_btn)
        mediumIcon1 = root.findViewById(R.id.medium_stress_icon_btn)
        mediumIcon2 = root.findViewById(R.id.medium_default_icon_btn)
        mediumIcon3 = root.findViewById(R.id.medium_sub_icon_btn)
        mediumIcon4 = root.findViewById(R.id.medium_waring_icon_btn)

        small1 = root.findViewById(R.id.small_stress_btn)
//        small2 = root.findViewById(R.id.small_default_btn)
//        small3 = root.findViewById(R.id.small_sub_btn)
//        small4 = root.findViewById(R.id.small_waring_btn)
        smallIcon1 = root.findViewById(R.id.small_stress_icon_btn)
//        smallIcon2 = root.findViewById(R.id.small_default_icon_btn)
//        smallIcon3 = root.findViewById(R.id.small_sub_icon_btn)
//        smallIcon4 = root.findViewById(R.id.small_waring_icon_btn)

        text1 = root.findViewById(R.id.text_default15)
        text2 = root.findViewById(R.id.text_stress15)
        text3 = root.findViewById(R.id.text_sub15)
        text4 = root.findViewById(R.id.text_waring15)
    }
}