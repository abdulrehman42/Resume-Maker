package com.example.resumemaker.http


import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AppIntercepter @Inject constructor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .method(originalRequest.method, originalRequest.body)

        val token = "getToken()"
        requestBuilder.addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())

    }

}
