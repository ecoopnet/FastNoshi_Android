package jp.marginalgains.fastnoshi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NoshiPaperEntity::class], version = 1, exportSchema = true)
@TypeConverters(StringListConverter::class)
abstract class NoshiDatabase : RoomDatabase() {
    abstract fun noshiPaperDao(): NoshiPaperDao
}
