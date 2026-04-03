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
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NpsApiService {

    @GET("api/v1/health")
    suspend fun health(): HealthResponseDto

    @POST("api/v1/nps/connectivity")
    suspend fun connectivity(@Body request: ConnectivityRequestDto): ConnectivityResponseDto

    @Multipart
    @POST("api/v1/nps/upload")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Part("paperSize") paperSize: String,
        @Part("colorMode") colorMode: String,
        @Part("fileName") fileName: String,
        @Part("npsUserIndex") npsUserIndex: Int,
        @Part("waitForCompletion") waitForCompletion: String
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
