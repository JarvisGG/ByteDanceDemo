package bizuikit

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.annotation.CallSuper

/**
 * @author: yyf
 * @date: 2020/8/27
 * @desc: 组件展示依赖插件
 * 开发 UI 组件需预览时操作步骤
 * step.1 继承 ComponentDepend
 * step.2 填到 UiKitActivity 注解中
 */
abstract class ComponentDepend {

    companion object {
        const val COMPONENT_KEY = "component_key"
    }

    /**
     * 组件类名
     */
    abstract fun name() : String

    /**
     * UiKitAct -> DetailAct
     * 自定义 intent 数据传递
     */
    @CallSuper
    open fun processorIntent(intent: Intent) {
        intent.putExtra(COMPONENT_KEY, name())
    }

    /**
     * 展示组件布局
     */
    abstract fun getContainerId(): Int

    /**
     * 处理组件
     * 1.UI 调整
     * 2.数据绑定
     */
    abstract fun bindView(activity: Activity, root: ViewGroup)

}