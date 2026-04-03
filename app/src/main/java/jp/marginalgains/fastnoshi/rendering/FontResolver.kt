package jp.marginalgains.fastnoshi.rendering

import android.graphics.Typeface
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet

/**
 * NoshiFontSet から android.graphics.Typeface を解決する。
 * フォントファイル未バンドル時はシステムフォントにフォールバック。
 */
object FontResolver {

    fun resolve(fontSet: NoshiFontSet): Typeface {
        val base = when {
            fontSet.fontFamily.contains("Serif", ignoreCase = true) ->
                Typeface.SERIF

            fontSet.fontFamily.contains("Sans", ignoreCase = true) ->
                Typeface.SANS_SERIF

            else -> Typeface.DEFAULT
        }
        val style = if (fontSet.isBold) Typeface.BOLD else Typeface.NORMAL
        return Typeface.create(base, style)
    }
}
