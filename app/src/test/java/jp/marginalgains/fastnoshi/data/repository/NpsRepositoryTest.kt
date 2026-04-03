package jp.marginalgains.fastnoshi.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import jp.marginalgains.fastnoshi.data.remote.NpsApiService
import jp.marginalgains.fastnoshi.data.remote.dto.ConnectivityResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.FileInfoResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.HealthResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.StatusResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.ThumbnailItemDto
import jp.marginalgains.fastnoshi.data.remote.dto.ThumbnailResponseDto
import jp.marginalgains.fastnoshi.data.remote.dto.UploadResponseDto
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NpsRepositoryTest {

    private lateinit var api: NpsApiService
    private lateinit var repository: NpsRepository

    @BeforeEach
    fun setUp() {
        api = mockk()
        repository = NpsRepository(api)
    }

    @Nested
    inner class Health {
        @Test
        fun `成功時にResult_successを返す`() = runTest {
            coEvery { api.health() } returns HealthResponseDto(
                status = "healthy",
                version = "1.0.0",
                timestamp = "2026-04-03T00:00:00Z"
            )
            val result = repository.health()
            assertTrue(result.isSuccess)
            assertEquals("healthy", result.getOrThrow().status)
        }

        @Test
        fun `例外時にResult_failureを返す`() = runTest {
            coEvery { api.health() } throws IOException("Network error")
            val result = repository.health()
            assertTrue(result.isFailure)
        }
    }

    @Nested
    inner class Connectivity {
        @Test
        fun `成功時にResult_successを返す`() = runTest {
            coEvery { api.connectivity(any()) } returns ConnectivityResponseDto(
                success = true,
                status = "connected",
                npsUserIndex = 3,
                durationMs = 150
            )
            val result = repository.connectivity(npsUserIndex = 3)
            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow().success)
        }

        @Test
        fun `例外時にResult_failureを返す`() = runTest {
            coEvery { api.connectivity(any()) } throws IOException("timeout")
            val result = repository.connectivity(npsUserIndex = 3)
            assertTrue(result.isFailure)
        }
    }

    @Nested
    inner class Upload {
        @Test
        fun `成功時にResult_successを返す`() = runTest {
            coEvery { api.upload(any(), any(), any(), any(), any(), any()) } returns
                UploadResponseDto(
                    success = true,
                    npsUserIndex = 3,
                    durationMs = 5000,
                    sessionId = "abc",
                    resultCode = 0,
                    printId = "12345678",
                    accessKey = "ABCD"
                )
            val filePart = mockk<MultipartBody.Part>()
            val result = repository.upload(
                file = filePart,
                paperSize = "0",
                colorMode = "1",
                fileName = "noshi.pdf",
                npsUserIndex = 3
            )
            assertTrue(result.isSuccess)
            assertEquals("12345678", result.getOrThrow().printId)
        }

        @Test
        fun `waitForCompletionがデフォルトでtrueになる`() = runTest {
            coEvery {
                api.upload(any(), any(), any(), any(), any(), eq("true"))
            } returns UploadResponseDto(
                success = true,
                npsUserIndex = 3,
                durationMs = 5000,
                sessionId = "abc",
                resultCode = 0
            )
            val filePart = mockk<MultipartBody.Part>()
            val result = repository.upload(
                file = filePart,
                paperSize = "0",
                colorMode = "1",
                fileName = "noshi.pdf",
                npsUserIndex = 3
            )
            assertTrue(result.isSuccess)
        }

        @Test
        fun `例外時にResult_failureを返す`() = runTest {
            coEvery {
                api.upload(any(), any(), any(), any(), any(), any())
            } throws IOException("upload failed")
            val filePart = mockk<MultipartBody.Part>()
            val result = repository.upload(
                file = filePart,
                paperSize = "0",
                colorMode = "1",
                fileName = "noshi.pdf",
                npsUserIndex = 3
            )
            assertTrue(result.isFailure)
        }
    }

    @Nested
    inner class CheckStatus {
        @Test
        fun `成功時にResult_successを返す`() = runTest {
            coEvery { api.status(any()) } returns StatusResponseDto(
                success = true,
                resultCode = 0,
                printId = "ABCD1234",
                accessKey = "9876"
            )
            val result = repository.checkStatus(
                sessionId = "abc",
                npsUserIndex = 3
            )
            assertTrue(result.isSuccess)
            assertEquals("ABCD1234", result.getOrThrow().printId)
        }
    }

    @Nested
    inner class GetThumbnail {
        @Test
        fun `成功時にResult_successを返す`() = runTest {
            coEvery { api.thumbnail(any()) } returns ThumbnailResponseDto(
                success = true,
                thumbnails = listOf(
                    ThumbnailItemDto(
                        thumbnailUrl = "https://example.com/t.jpg",
                        thumbnailPageNo = "1",
                        thumbnailColor = "1",
                        thumbnailSize = "1"
                    )
                )
            )
            val result = repository.getThumbnail(
                accessKey = "ABCD",
                npsUserIndex = 3
            )
            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrThrow().thumbnails.size)
        }
    }

    @Nested
    inner class GetFileInfo {
        @Test
        fun `成功時にResult_successを返す`() = runTest {
            coEvery { api.fileInfo(any()) } returns FileInfoResponseDto(
                success = true,
                printId = "ABCD1234",
                fileName = "noshi.pdf",
                paperSize = "0",
                registrationDate = "2026-04-03T10:00:00+09:00",
                expiryDate = "2026-04-10T23:59:59+09:00",
                pageCount = 1,
                fileSize = 1048576
            )
            val result = repository.getFileInfo(
                accessKey = "ABCD",
                npsUserIndex = 3
            )
            assertTrue(result.isSuccess)
            assertEquals("noshi.pdf", result.getOrThrow().fileName)
        }
    }
}
