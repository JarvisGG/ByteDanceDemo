package bizuikit

import bizuikit.ComponentDepend
import kotlin.reflect.KClass

/**
 * @author: yyf
 * @date: 2020/8/27
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UIComponents(vararg val depends: KClass<out ComponentDepend>)