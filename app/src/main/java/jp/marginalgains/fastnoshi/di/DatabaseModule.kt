package jp.marginalgains.fastnoshi.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.marginalgains.fastnoshi.data.local.NoshiDatabase
import jp.marginalgains.fastnoshi.data.local.NoshiPaperDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoshiDatabase =
        Room.databaseBuilder(
            context,
            NoshiDatabase::class.java,
            "noshi_database"
        ).build()

    @Provides
    fun provideNoshiPaperDao(database: NoshiDatabase): NoshiPaperDao =
        database.noshiPaperDao()
}
