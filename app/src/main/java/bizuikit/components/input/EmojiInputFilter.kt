package bizuikit.components.input

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * Created by yangjianjun on 2020/6/25
 * 表情过滤
 */
class EmojiInputFilter : InputFilter {
    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?,
                        dstart: Int, dend: Int): CharSequence? {
        return if (isEmoji(source)) {
            ""
        } else null
    }

    fun isEmoji(input: CharSequence?): Boolean {
        val p = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udd00-\ud83e\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        val m = p.matcher(input)
        return m.find()
    }
}