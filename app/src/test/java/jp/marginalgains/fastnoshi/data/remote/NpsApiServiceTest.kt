package jp.marginalgains.fastnoshi.data.remote

import com.squareup.moshi.Moshi
import jp.marginalgains.fastnoshi.data.remote.dto.ConnectivityRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.FileInfoRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.MessageRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.StatusRequestDto
import jp.marginalgains.fastnoshi.data.remote.dto.ThumbnailRequestDto
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NpsApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var api: NpsApiService

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()

        val moshi = Moshi.Builder().build()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NpsApiService::class.java)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Nested
    inner class Health {
        @Test
        fun `GETリクエストが正しいパスに送信される`() = runTest {
            server.enqueue(
                MockResponse()
                    .setBody(
                        """{"status":"healthy","version":"1.0.0","timestamp":"2026-04-03T00:00:00Z"}"""
                    )
            )
            val response = api.health()
            val request = server.takeRequest()

            assertEquals("GET", request.method)
            assertEquals("/api/v1/health", request.path)
            assertEquals("healthy", response.status)
        }
    }

    @Nested
    inner class Connectivity {
        @Test
        fun `POSTリクエストが正しいパスとボディで送信される`() = runTest {
            server.enqueue(
                MockResponse()
                    .setBody(
                        """{"success":true,"status":"connected","npsUserIndex":3,"durationMs":150}"""
                    )
            )
            val response = api.connectivity(ConnectivityRequestDto(npsUserIndex = 3))
            val request = server.takeRequest()

            assertEquals("POST", request.method)
            assertEquals("/api/v1/nps/connectivity", request.path)
            assertTrue(request.body.readUtf8().contains("\"npsUserIndex\":3"))
            assertTrue(response.success)
        }
    }

    @Nested
    inner class Upload {
        @Test
        fun `multipartリクエストが正しいパスに送信される`() = runTest {
            server.enqueue(
                MockResponse().setBody(
                    """{
                        "success":true,"npsUserIndex":3,"durationMs":5000,
                        "sessionId":"abc","resultCode":0,
                        "printId":"12345678","accessKey":"ABCD"
                    }"""
                )
            )
            val pdfBody = "fake-pdf".toByteArray()
                .toRequestBody("application/pdf".toMediaType())
            val filePart = MultipartBody.Part.createFormData("file", "noshi.pdf", pdfBody)
            val response = api.upload(
                file = filePart,
                paperSize = NpsApiService.textPart("paperSize", "0"),
                colorMode = NpsApiService.textPart("colorMode", "1"),
                fileName = NpsApiService.textPart("fileName", "noshi.pdf"),
                npsUserIndex = NpsApiService.textPart("npsUserIndex", 3),
                waitForCompletion = NpsApiService.textPart("waitForCompletion", "true")
            )
            val request = server.takeRequest()

            assertEquals("POST", request.method)
            assertEquals("/api/v1/nps/upload", request.path)
            assertTrue(response.success)
            assertEquals("12345678", response.printId)
        }
    }

    @Nested
    inner class Status {
        @Test
        fun `POSTリクエストが正しいパスに送信される`() = runTest {
            server.enqueue(
                MockResponse()
                    .setBody(
                        """{"success":true,"resultCode":0,"printId":"ABCD1234","accessKey":"9876"}"""
                    )
            )
            val response = api.status(StatusRequestDto(sessionId = "abc", npsUserIndex = 3))
            val request = server.takeRequest()

            assertEquals("POST", request.method)
            assertEquals("/api/v1/nps/status", request.path)
            assertEquals("ABCD1234", response.printId)
        }
    }

    @Nested
    inner class Thumbnail {
        @Test
        fun `POSTリクエストが正しいパスに送信される`() = runTest {
            server.enqueue(
                MockResponse().setBody(
                    """{
                        "success":true,
                        "thumbnails":[{"thumbnailUrl":"https://example.com/t.jpg","thumbnailPageNo":"1","thumbnailColor":"1","thumbnailSize":"1"}]
                    }"""
                )
            )
            val response = api.thumbnail(ThumbnailRequestDto(accessKey = "ABCD", npsUserIndex = 3))
            val request = server.takeRequest()

            assertEquals("POST", request.method)
            assertEquals("/api/v1/nps/thumbnail", request.path)
            assertEquals(1, response.thumbnails.size)
        }
    }

    @Nested
    inner class FileInfo {
        @Test
        fun `POSTリクエストが正しいパスに送信される`() = runTest {
            server.enqueue(
                MockResponse().setBody(
                    """{
                        "success":true,"printId":"ABCD1234","fileName":"noshi.pdf",
                        "paperSize":"0","registrationDate":"2026-04-03T10:00:00+09:00",
                        "expiryDate":"2026-04-10T23:59:59+09:00","pageCount":1,"fileSize":1048576
                    }"""
                )
            )
            val response = api.fileInfo(FileInfoRequestDto(accessKey = "ABCD", npsUserIndex = 3))
            val request = server.takeRequest()

            assertEquals("POST", request.method)
            assertEquals("/api/v1/nps/file-info", request.path)
            assertEquals("noshi.pdf", response.fileName)
        }
    }

    @Nested
    inner class Message {
        @Test
        fun `POSTリクエストが正しいパスに送信される`() = runTest {
            server.enqueue(MockResponse().setBody("""{"success":true}"""))
            api.message(MessageRequestDto(messageNo = 0, npsUserIndex = 3))
            val request = server.takeRequest()

            assertEquals("POST", request.method)
            assertEquals("/api/v1/nps/message", request.path)
        }
    }
}
