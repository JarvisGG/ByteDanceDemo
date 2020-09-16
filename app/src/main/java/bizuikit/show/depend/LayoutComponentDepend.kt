package bizuikit.show.depend

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import bizuikit.ComponentDepend
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_BOTTOM
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_LEFT
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_NONE
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_RIGHT
import bizuikit.components.common.IMUIShapeLayout.Companion.HIDE_RADIUS_SIDE_TOP
import bizuikit.components.common.MUIShapeHelper.Companion.RADIUS_OF_HALF_VIEW_HEIGHT
import bizuikit.components.common.MUIShapeHelper.Companion.RADIUS_OF_HALF_VIEW_WIDTH
import bizuikit.components.layout.MUILayout
import bizuikit.utils.dp2px
import bizuikit.utils.px2dp
import com.example.bytedancedemo.R

/**
 * @author: yyf
 * @date: 2020/9/17
 * @desc:
 */
class LayoutComponentDepend : ComponentDepend() {

    override fun name() = "Layout"

    override fun getContainerId() = R.layout.container_layout

    override fun bindView(activity: Activity, root: ViewGroup) {
        var shadowAlpha = 0.25f
        var shadowElevationDp = 14
        var radius = 15.dp2px.toInt()

        val testLayout = root.findViewById<MUILayout>(R.id.layout_for_test)
        testLayout.setRadiusAndShadow(
            radius,
            shadowElevationDp.dp2px.toInt(),
            shadowAlpha
        )

        root.findViewById<View>(R.id.shadow_color_red).setOnClickListener {
            testLayout.setShadowColor(-0x10000)
        }
        root.findViewById<View>(R.id.shadow_color_blue).setOnClickListener {
            testLayout.setShadowColor(-0xffff01)
        }
        root.findViewById<View>(R.id.radius_15dp).setOnClickListener {
            radius = 15.dp2px.toInt()
            testLayout.setRadius(radius)
        }
        root.findViewById<View>(R.id.radius_half_width).setOnClickListener {
            radius = RADIUS_OF_HALF_VIEW_WIDTH
            testLayout.setRadius(radius)
        }
        root.findViewById<View>(R.id.radius_half_height).setOnClickListener {
            radius = RADIUS_OF_HALF_VIEW_HEIGHT
            testLayout.setRadius(radius)
        }
        val alphaTv = root.findViewById<TextView>(R.id.alpha_tv)
        val elevationTv = root.findViewById<TextView>(R.id.elevation_tv)
        val radiusTv = root.findViewById<TextView>(R.id.radius_tv)
        val alphaSeekBar = root.findViewById<SeekBar>(R.id.test_seekbar_alpha)
        alphaSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                shadowAlpha = progress * 1f / 100
                alphaTv.text = "alpha: $shadowAlpha"
                testLayout.setRadiusAndShadow(
                    radius,
                    shadowElevationDp.dp2px.toInt(),
                    shadowAlpha
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val elevationSeekBar = root.findViewById<SeekBar>(R.id.test_seekbar_elevation)
        elevationSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                shadowElevationDp = progress
                elevationTv.text = "elevation: " + progress + "dp"
                testLayout.setRadiusAndShadow(
                    radius,
                    shadowElevationDp.dp2px.toInt(),
                    shadowAlpha
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })



        val radiusSeekBar = root.findViewById<SeekBar>(R.id.test_seekbar_radius)
        radiusSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                radius = (progress * 130.dp2px / 100).toInt()
                radiusTv.text = "radius: " + radius.px2dp + "dp"
                testLayout.setRadiusAndShadow(
                    radius,
                    shadowElevationDp.dp2px.toInt(),
                    shadowAlpha
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        alphaSeekBar.progress = (shadowAlpha * 100).toInt()
        elevationSeekBar.progress = shadowElevationDp
        radiusSeekBar.progress = (radius * 100 / 130.dp2px).toInt()



        val hideRadiusGroup = root.findViewById<RadioGroup>(R.id.hide_radius_group)
        hideRadiusGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.hide_radius_none -> testLayout.setRadius(
                    radius,
                    HIDE_RADIUS_SIDE_NONE
                )
                R.id.hide_radius_left -> testLayout.setRadius(
                    radius,
                    HIDE_RADIUS_SIDE_LEFT
                )
                R.id.hide_radius_top -> testLayout.setRadius(
                    radius,
                    HIDE_RADIUS_SIDE_TOP
                )
                R.id.hide_radius_bottom -> testLayout.setRadius(
                    radius,
                    HIDE_RADIUS_SIDE_BOTTOM
                )
                R.id.hide_radius_right -> testLayout.setRadius(
                    radius,
                    HIDE_RADIUS_SIDE_RIGHT
                )
            }
        }
    }

}