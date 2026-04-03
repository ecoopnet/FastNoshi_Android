package jp.marginalgains.fastnoshi.data.remote.dto

import com.squareup.moshi.JsonClass

// === Health ===

@JsonClass(generateAdapter = true)
data class HealthResponseDto(val status: String, val version: String, val timestamp: String)

// === Connectivity ===

@JsonClass(generateAdapter = true)
data class ConnectivityRequestDto(val npsUserIndex: Int)

@JsonClass(generateAdapter = true)
data class ConnectivityResponseDto(
    val success: Boolean,
    val status: String,
    val npsUserIndex: Int,
    val durationMs: Long
)

// === Upload ===

@JsonClass(generateAdapter = true)
data class UploadResponseDto(
    val success: Boolean,
    val npsUserIndex: Int,
    val durationMs: Long,
    val sessionId: String,
    val resultCode: Int,
    val resultCodeMessage: String? = null,
    val printId: String? = null,
    val accessKey: String? = null
)

// === Status ===

@JsonClass(generateAdapter = true)
data class StatusRequestDto(val sessionId: String, val npsUserIndex: Int)

@JsonClass(generateAdapter = true)
data class StatusResponseDto(
    val success: Boolean,
    val resultCode: Int,
    val printId: String? = null,
    val accessKey: String? = null
)

// === Thumbnail ===

@JsonClass(generateAdapter = true)
data class ThumbnailRequestDto(val accessKey: String, val npsUserIndex: Int)

@JsonClass(generateAdapter = true)
data class ThumbnailItemDto(
    val thumbnailUrl: String,
    val thumbnailPageNo: String,
    val thumbnailColor: String,
    val thumbnailSize: String
)

@JsonClass(generateAdapter = true)
data class ThumbnailResponseDto(val success: Boolean, val thumbnails: List<ThumbnailItemDto>)

// === File Info ===

@JsonClass(generateAdapter = true)
data class FileInfoRequestDto(val accessKey: String, val npsUserIndex: Int)

@JsonClass(generateAdapter = true)
data class FileInfoResponseDto(
    val success: Boolean,
    val printId: String,
    val fileName: String,
    val paperSize: String,
    val registrationDate: String,
    val expiryDate: String,
    val pageCount: Int,
    val fileSize: Long
)

// === Message ===

@JsonClass(generateAdapter = true)
data class MessageRequestDto(val messageNo: Int, val npsUserIndex: Int)
