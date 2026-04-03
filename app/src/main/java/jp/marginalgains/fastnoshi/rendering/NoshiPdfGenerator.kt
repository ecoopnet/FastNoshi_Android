package jp.marginalgains.fastnoshi.rendering

import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import java.io.ByteArrayOutputStream

/**
 * のし紙PDF生成エンジン
 * android.graphics.pdf.PdfDocument でPDFを生成する。
 * 座標系: 72 DPI (1mm = 72/25.4 pt)
 */
class NoshiPdfGenerator(
    private val noshiRenderer: NoshiRenderer
) {

    companion object {
        private val A4_WIDTH_PT = PaperSize.A4.widthPt
        private val A4_HEIGHT_PT = PaperSize.A4.heightPt

        /**
         * A4基準のスケール係数を計算する
         */
        fun calculateScale(paperSize: PaperSize): Float {
            val scaleX = paperSize.widthPt / A4_WIDTH_PT
            val scaleY = paperSize.heightPt / A4_HEIGHT_PT
            return minOf(scaleX, scaleY)
        }
    }

    /**
     * のし紙PDFを生成する
     * @return PDF データ (ByteArray)
     */
    fun generate(
        template: NoshiTemplate,
        omoteGaki: String,
        names: List<String>,
        typeface: Typeface?,
        fontSize: Float,
        paperSize: PaperSize
    ): ByteArray {
        val widthPt = paperSize.widthPt.toInt()
        val heightPt = paperSize.heightPt.toInt()

        val pdfDoc = PdfDocument()
        try {
            val pageInfo = PdfDocument.PageInfo.Builder(widthPt, heightPt, 1).create()
            val page = pdfDoc.startPage(pageInfo)

            val bitmap = noshiRenderer.renderToBitmap(
                template, omoteGaki, names, typeface, fontSize, paperSize
            )
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            bitmap.recycle()

            pdfDoc.finishPage(page)

            val outputStream = ByteArrayOutputStream()
            pdfDoc.writeTo(outputStream)
            return outputStream.toByteArray()
        } finally {
            pdfDoc.close()
        }
    }
}
