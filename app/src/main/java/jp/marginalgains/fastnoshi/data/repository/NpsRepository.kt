package jp.marginalgains.fastnoshi.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import jp.marginalgains.fastnoshi.data.remote.NpsApiService
import jp.marginalgains.fastnoshi.data.remote.dto.ConnectivityRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.ConnectivityResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.FileInfoRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.FileInfoResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.HealthResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.MessageRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.StatusRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.StatusResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.ThumbnailRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.ThumbnailResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.UploadResponseDto
import okhttp3.MultipartBody

@Singleton
class NpsRepository @Inject constructor(private val api: NpsApiService) {

    suspend fun health(): Result<HealthResponseDto> = runCatching {
        api.health()
    }

    suspend fun connectivity(npsUserIndex: Int): Result<ConnectivityResponseDto> = runCatching {
        api.connectivity(ConnectivityRequestDto(npsUserIndex))
    }

    suspend fun upload(
        file: MultipartBody.Part,
        paperSize: String,
        colorMode: String,
        fileName: String,
        npsUserIndex: Int,
        waitForCompletion: String = "true"
    ): Result<UploadResponseDto> = runCatching {
        api.upload(file, paperSize, colorMode, fileName, npsUserIndex, waitForCompletion)
    }

    suspend fun checkStatus(sessionId: String, npsUserIndex: Int): Result<StatusResponseDto> =
        runCatching {
            api.status(StatusRequestDto(sessionId, npsUserIndex))
        }

    suspend fun getThumbnail(accessKey: String, npsUserIndex: Int): Result<ThumbnailResponseDto> =
        runCatching {
            api.thumbnail(ThumbnailRequestDto(accessKey, npsUserIndex))
        }

    suspend fun getFileInfo(accessKey: String, npsUserIndex: Int): Result<FileInfoResponseDto> =
        runCatching {
            api.fileInfo(FileInfoRequestDto(accessKey, npsUserIndex))
        }

    suspend fun message(messageNo: Int, npsUserIndex: Int): Result<Unit> = runCatching {
        api.message(MessageRequestDto(messageNo, npsUserIndex))
    }
}
