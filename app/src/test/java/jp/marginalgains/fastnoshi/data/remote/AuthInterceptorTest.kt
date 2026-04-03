package jp.marginalgains.fastnoshi.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()

        val interceptor = AuthInterceptor(
            serviceToken = "test-token",
            appName = "fastnoshi"
        )
        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `X-App-Service-Tokenヘッダーが付与される`() {
        server.enqueue(MockResponse().setBody("{}"))
        client.newCall(Request.Builder().url(server.url("/test")).build()).execute()

        val request = server.takeRequest()
        assertEquals("test-token", request.getHeader("X-App-Service-Token"))
    }

    @Test
    fun `X-App-Nameヘッダーが付与される`() {
        server.enqueue(MockResponse().setBody("{}"))
        client.newCall(Request.Builder().url(server.url("/test")).build()).execute()

        val request = server.takeRequest()
        assertEquals("fastnoshi", request.getHeader("X-App-Name"))
    }

    @Test
    fun `X-App-User-Idヘッダーが付与される`() {
        val interceptor = AuthInterceptor(
            serviceToken = "test-token",
            appName = "fastnoshi",
            userId = "user-123"
        )
        val testClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        server.enqueue(MockResponse().setBody("{}"))
        testClient.newCall(Request.Builder().url(server.url("/test")).build()).execute()

        val request = server.takeRequest()
        assertEquals("user-123", request.getHeader("X-App-User-Id"))
    }

    @Test
    fun `userId未指定時はX-App-User-Idヘッダーなし`() {
        server.enqueue(MockResponse().setBody("{}"))
        client.newCall(Request.Builder().url(server.url("/test")).build()).execute()

        val request = server.takeRequest()
        assertEquals(null, request.getHeader("X-App-User-Id"))
    }
}
