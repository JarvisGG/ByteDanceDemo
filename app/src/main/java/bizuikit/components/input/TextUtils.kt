package bizuikit.components.input

/**
 * @author: yyf
 * @date: 2020/10/9
 * @desc:
 */
fun getTextCount(oriStr: String?): Int {
    return if (oriStr == null) {
        0
    } else try {
        val bytes = oriStr.toByteArray(charset("GBK"))
        (bytes.size + 1) / 2
    } catch (e: Exception) {
        e.printStackTrace()
        oriStr.length
    }
}