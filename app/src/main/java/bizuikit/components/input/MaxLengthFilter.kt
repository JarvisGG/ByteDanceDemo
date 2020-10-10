package bizuikit.components.input

import android.text.InputFilter
import android.text.Spanned

/**
 * 计算最大长度
 */
class MaxLengthFilter(private val max: Int, private val enableDouble: Boolean = true) : InputFilter {

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        val keep = max - (if (enableDouble) getTextCount(dest.toString().trim()) else dest.toString().trim().length - (dend - dstart))
        return when {
            keep <= 0 -> ""
            keep >= end - start -> {
                val st = if (enableDouble) getTextCount("$source") else source?.length
                if (keep == 1 && st == 1) {
                    source
                } else {
                    null
                }
            } // keep original
            else -> {
                var length = source?.length?:0
                while (getTextCount(source?.subSequence(0, length).toString()) > keep) {
                    if (length > 0) {
                        length--
                    } else {
                        break
                    }
                }
                if (length == 0) "" else source?.subSequence(start, start + length)
            }
        }
    }
}
