package jp.marginalgains.fastnoshi.data.remote.dto

import com.squareup.moshi.Moshi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NpsDtoTest {

    private val moshi = Moshi.Builder().build()

    @Nested
    inner class HealthResponse {
        private val adapter = moshi.adapter(HealthResponseDto::class.java)

        @Test
        fun `JSONからデシリアライズ`() {
            val json =
                """{"status":"healthy","version":"1.0.0","timestamp":"2026-04-03T00:00:00Z"}"""
            val dto = adapter.fromJson(json)!!
            assertEquals("healthy", dto.status)
            assertEquals("1.0.0", dto.version)
            assertEquals("2026-04-03T00:00:00Z", dto.timestamp)
        }
    }

    @Nested
    inner class ConnectivityRequest {
        private val adapter = moshi.adapter(ConnectivityRequestDto::class.java)

        @Test
        fun `JSONにシリアライズ`() {
            val dto = ConnectivityRequestDto(npsUserIndex = 3)
            val json = adapter.toJson(dto)
            assertTrue(json.contains("\"npsUserIndex\":3"))
        }
    }

    @Nested
    inner class ConnectivityResponse {
        private val adapter = moshi.adapter(ConnectivityResponseDto::class.java)

        @Test
        fun `JSONからデシリアライズ`() {
            val json = """{"success":true,"status":"connected","npsUserIndex":3,"durationMs":150}"""
            val dto = adapter.fromJson(json)!!
            assertTrue(dto.success)
            assertEquals("connected", dto.status)
            assertEquals(3, dto.npsUserIndex)
            assertEquals(150, dto.durationMs)
        }
    }

    @Nested
    inner class UploadResponse {
        private val adapter = moshi.adapter(UploadResponseDto::class.java)

        @Test
        fun `waitForCompletion=trueのレスポンスをデシリアライズ`() {
            val json = """{
                "success":true,"npsUserIndex":3,"durationMs":5000,
                "sessionId":"abc123","resultCode":0,
                "resultCodeMessage":"処理完了",
                "printId":"12345678","accessKey":"ABCD"
            }
            """.trimIndent()
            val dto = adapter.fromJson(json)!!
            assertTrue(dto.success)
            assertEquals("12345678", dto.printId)
            assertEquals("ABCD", dto.accessKey)
            assertEquals(0, dto.resultCode)
        }

        @Test
        fun `waitForCompletion=falseのレスポンスでprintIdがnull`() {
            val json =
                """{"success":true,"npsUserIndex":3,"durationMs":2500,"sessionId":"abc","resultCode":0}"""
            val dto = adapter.fromJson(json)!!
            assertTrue(dto.success)
            assertNull(dto.printId)
            assertNull(dto.accessKey)
        }
    }

    @Nested
    inner class StatusRequest {
        private val adapter = moshi.adapter(StatusRequestDto::class.java)

        @Test
        fun `JSONにシリアライズ`() {
            val dto = StatusRequestDto(sessionId = "abc123", npsUserIndex = 3)
            val json = adapter.toJson(dto)
            assertTrue(json.contains("\"sessionId\":\"abc123\""))
            assertTrue(json.contains("\"npsUserIndex\":3"))
        }
    }

    @Nested
    inner class StatusResponse {
        private val adapter = moshi.adapter(StatusResponseDto::class.java)

        @Test
        fun `完了レスポンスをデシリアライズ`() {
            val json = """{"success":true,"resultCode":0,"printId":"ABCD1234","accessKey":"9876"}"""
            val dto = adapter.fromJson(json)!!
            assertTrue(dto.success)
            assertEquals(0, dto.resultCode)
            assertEquals("ABCD1234", dto.printId)
            assertEquals("9876", dto.accessKey)
        }

        @Test
        fun `処理中レスポンスでprintIdがnull`() {
            val json = """{"success":true,"resultCode":1}"""
            val dto = adapter.fromJson(json)!!
            assertEquals(1, dto.resultCode)
            assertNull(dto.printId)
        }
    }

    @Nested
    inner class ThumbnailRequest {
        private val adapter = moshi.adapter(ThumbnailRequestDto::class.java)

        @Test
        fun `JSONにシリアライズ`() {
            val dto = ThumbnailRequestDto(accessKey = "ABCD", npsUserIndex = 3)
            val json = adapter.toJson(dto)
            assertTrue(json.contains("\"accessKey\":\"ABCD\""))
        }
    }

    @Nested
    inner class ThumbnailResponse {
        private val adapter = moshi.adapter(ThumbnailResponseDto::class.java)

        @Test
        fun `サムネイル一覧をデシリアライズ`() {
            val json = """{
                "success":true,
                "thumbnails":[{"thumbnailUrl":"https://example.com/thumb.jpg","thumbnailPageNo":"1","thumbnailColor":"1","thumbnailSize":"1"}]
            }
            """.trimIndent()
            val dto = adapter.fromJson(json)!!
            assertTrue(dto.success)
            assertEquals(1, dto.thumbnails.size)
            assertEquals("https://example.com/thumb.jpg", dto.thumbnails[0].thumbnailUrl)
        }
    }

    @Nested
    inner class FileInfoRequest {
        private val adapter = moshi.adapter(FileInfoRequestDto::class.java)

        @Test
        fun `JSONにシリアライズ`() {
            val dto = FileInfoRequestDto(accessKey = "ABCD", npsUserIndex = 3)
            val json = adapter.toJson(dto)
            assertTrue(json.contains("\"accessKey\":\"ABCD\""))
        }
    }

    @Nested
    inner class FileInfoResponse {
        private val adapter = moshi.adapter(FileInfoResponseDto::class.java)

        @Test
        fun `ファイル情報をデシリアライズ`() {
            val json = """{
                "success":true,"printId":"ABCD1234","fileName":"noshi.pdf",
                "paperSize":"0","registrationDate":"2026-04-03T10:00:00+09:00",
                "expiryDate":"2026-04-10T23:59:59+09:00","pageCount":1,"fileSize":1048576
            }
            """.trimIndent()
            val dto = adapter.fromJson(json)!!
            assertTrue(dto.success)
            assertEquals("ABCD1234", dto.printId)
            assertEquals("noshi.pdf", dto.fileName)
            assertEquals(1, dto.pageCount)
            assertEquals(1048576, dto.fileSize)
        }
    }

    @Nested
    inner class MessageRequest {
        private val adapter = moshi.adapter(MessageRequestDto::class.java)

        @Test
        fun `JSONにシリアライズ`() {
            val dto = MessageRequestDto(messageNo = 0, npsUserIndex = 3)
            val json = adapter.toJson(dto)
            assertTrue(json.contains("\"messageNo\":0"))
        }
    }
}
