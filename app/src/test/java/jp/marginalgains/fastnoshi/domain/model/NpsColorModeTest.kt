package jp.marginalgains.fastnoshi.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NpsColorModeTest {

    @Test
    fun `カラーモードのAPIコード値が正しい`() {
        assertEquals("1", NpsColorMode.COLOR.code)
        assertEquals("2", NpsColorMode.MONOCHROME.code)
    }

    @Test
    fun `カラーテンプレートからCOLORが返る`() {
        val colorTemplate = NoshiTemplate.all.first { it.isColor }
        assertEquals(NpsColorMode.COLOR, NpsColorMode.fromTemplate(colorTemplate))
    }

    @Test
    fun `白黒テンプレートからMONOCHROMEが返る`() {
        val monoTemplate = NoshiTemplate.all.first { !it.isColor }
        assertEquals(NpsColorMode.MONOCHROME, NpsColorMode.fromTemplate(monoTemplate))
    }

    @Test
    fun `全テンプレートに対してfromTemplateが正しく動作する`() {
        NoshiTemplate.all.forEach { template ->
            val expected = if (template.isColor) NpsColorMode.COLOR else NpsColorMode.MONOCHROME
            assertEquals(expected, NpsColorMode.fromTemplate(template))
        }
    }
}
