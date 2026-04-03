package jp.marginalgains.fastnoshi.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val serviceToken: String,
    private val appName: String,
    private val userId: String? = null
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header("X-App-Service-Token", serviceToken)
            .header("X-App-Name", appName)

        if (userId != null) {
            builder.header("X-App-User-Id", userId)
        }

        return chain.proceed(builder.build())
    }
}
