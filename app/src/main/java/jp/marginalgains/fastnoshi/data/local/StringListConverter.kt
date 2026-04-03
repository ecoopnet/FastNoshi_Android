package jp.marginalgains.fastnoshi.data.local

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class StringListConverter {

    private val moshi: Moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<List<String>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, String::class.java))

    @TypeConverter
    fun fromStringList(value: List<String>): String = adapter.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value == null) return emptyList()
        return adapter.fromJson(value) ?: emptyList()
    }
}
