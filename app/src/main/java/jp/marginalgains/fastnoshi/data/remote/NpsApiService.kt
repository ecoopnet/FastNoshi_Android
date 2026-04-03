package jp.marginalgains.fastnoshi.data.remote

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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NpsApiService {

    companion object {
        /** multipart text partを作成（JSON encodingを避ける） */
        fun textPart(name: String, value: String): MultipartBody.Part =
            MultipartBody.Part.createFormData(name, value)

        fun textPart(name: String, value: Int): MultipartBody.Part =
            MultipartBody.Part.createFormData(name, value.toString())
    }

    @GET("api/v1/health")
    suspend fun health(): HealthResponseDto

    @POST("api/v1/nps/connectivity")
    suspend fun connectivity(@Body request: ConnectivityRequestDto): ConnectivityResponseDto

    @Multipart
    @POST("api/v1/nps/upload")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Part paperSize: MultipartBody.Part,
        @Part colorMode: MultipartBody.Part,
        @Part fileName: MultipartBody.Part,
        @Part npsUserIndex: MultipartBody.Part,
        @Part waitForCompletion: MultipartBody.Part
    ): UploadResponseDto

    @POST("api/v1/nps/status")
    suspend fun status(@Body request: StatusRequestDto): StatusResponseDto

    @POST("api/v1/nps/thumbnail")
    suspend fun thumbnail(@Body request: ThumbnailRequestDto): ThumbnailResponseDto

    @POST("api/v1/nps/file-info")
    suspend fun fileInfo(@Body request: FileInfoRequestDto): FileInfoResponseDto

    @POST("api/v1/nps/message")
    suspend fun message(@Body request: MessageRequestDto)
}
