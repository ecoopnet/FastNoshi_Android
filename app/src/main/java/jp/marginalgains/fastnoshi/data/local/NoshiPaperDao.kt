package jp.marginalgains.fastnoshi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoshiPaperDao {

    @Query("SELECT * FROM noshi_papers ORDER BY createdAt DESC")
    fun getAllPapers(): Flow<List<NoshiPaperEntity>>

    @Query("SELECT * FROM noshi_papers WHERE id = :id")
    fun getPaperById(id: String): Flow<NoshiPaperEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaper(paper: NoshiPaperEntity)

    @Update
    suspend fun updatePaper(paper: NoshiPaperEntity)

    @Delete
    suspend fun deletePaper(paper: NoshiPaperEntity)

    @Query("SELECT * FROM noshi_papers WHERE lastPrintedAt IS NULL ORDER BY createdAt DESC")
    fun getUnprintedPapers(): Flow<List<NoshiPaperEntity>>

    @Query("SELECT * FROM noshi_papers WHERE lastPrintedAt IS NOT NULL ORDER BY createdAt DESC")
    fun getPrintedPapers(): Flow<List<NoshiPaperEntity>>
}
