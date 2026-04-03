package jp.marginalgains.fastnoshi.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import jp.marginalgains.fastnoshi.data.local.NoshiPaperDao
import jp.marginalgains.fastnoshi.data.local.NoshiPaperEntity
import jp.marginalgains.fastnoshi.domain.model.NoshiPaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class NoshiRepository @Inject constructor(private val dao: NoshiPaperDao) {

    fun getAllPapers(): Flow<List<NoshiPaper>> =
        dao.getAllPapers().map { entities -> entities.map { it.toDomain() } }

    fun getPaperById(id: String): Flow<NoshiPaper?> =
        dao.getPaperById(id).map { it?.toDomain() }

    fun getUnprintedPapers(): Flow<List<NoshiPaper>> =
        dao.getUnprintedPapers().map { entities -> entities.map { it.toDomain() } }

    fun getPrintedPapers(): Flow<List<NoshiPaper>> =
        dao.getPrintedPapers().map { entities -> entities.map { it.toDomain() } }

    suspend fun insertPaper(paper: NoshiPaper) {
        dao.insertPaper(paper.toEntity())
    }

    suspend fun updatePaper(paper: NoshiPaper) {
        dao.updatePaper(paper.toEntity())
    }

    suspend fun deletePaper(paper: NoshiPaper) {
        dao.deletePaper(paper.toEntity())
    }

    private fun NoshiPaperEntity.toDomain(): NoshiPaper = NoshiPaper(
        id = id,
        templateId = templateId,
        omoteGaki = omoteGaki,
        names = names,
        fontId = fontId,
        fontSize = fontSize,
        paperSize = paperSize,
        createdAt = createdAt,
        lastPrintedAt = lastPrintedAt
    )

    private fun NoshiPaper.toEntity(): NoshiPaperEntity = NoshiPaperEntity(
        id = id,
        templateId = templateId,
        omoteGaki = omoteGaki,
        names = names,
        fontId = fontId,
        fontSize = fontSize,
        paperSize = paperSize,
        createdAt = createdAt,
        lastPrintedAt = lastPrintedAt
    )
}
