package jp.marginalgains.fastnoshi.data.local

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class NoshiPaperEntityTest {

    @Test
    fun `エンティティのデフォルト値が正しい`() {
        val entity = NoshiPaperEntity(
            templateId = "05_cho_red_on",
            omoteGaki = "御祝",
            names = listOf("山田太郎"),
            fontId = "mincho",
            fontSize = 24f,
            paperSize = "A4"
        )
        assertNull(entity.lastPrintedAt)
    }

    @Test
    fun `5名までの名前リストを保持できる`() {
        val names = listOf("田中", "鈴木", "佐藤", "高橋", "伊藤")
        val entity = NoshiPaperEntity(
            templateId = "05_cho_red_on",
            omoteGaki = "御祝",
            names = names,
            fontId = "mincho",
            fontSize = 24f,
            paperSize = "A4"
        )
        assertEquals(5, entity.names.size)
        assertEquals(names, entity.names)
    }
}

class StringListConverterTest {

    private val converter = StringListConverter()

    @Test
    fun `空リストをJSONに変換`() {
        assertEquals("[]", converter.fromStringList(emptyList()))
    }

    @Test
    fun `リストをJSONに変換`() {
        val json = converter.fromStringList(listOf("山田", "鈴木"))
        assertEquals("[\"山田\",\"鈴木\"]", json)
    }

    @Test
    fun `JSONをリストに変換`() {
        val list = converter.toStringList("[\"山田\",\"鈴木\"]")
        assertEquals(listOf("山田", "鈴木"), list)
    }

    @Test
    fun `空JSON配列をリストに変換`() {
        val list = converter.toStringList("[]")
        assertEquals(emptyList<String>(), list)
    }

    @Test
    fun `nullをリストに変換すると空リスト`() {
        val list = converter.toStringList(null)
        assertEquals(emptyList<String>(), list)
    }

    @Test
    fun `ラウンドトリップ変換が正しい`() {
        val original = listOf("田中", "鈴木", "佐藤")
        val json = converter.fromStringList(original)
        val result = converter.toStringList(json)
        assertEquals(original, result)
    }
}
