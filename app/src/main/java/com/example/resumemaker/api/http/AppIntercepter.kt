package com.example.resumemaker.api.http


import com.example.resumemaker.utils.Constants
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

        requestBuilder.addHeader("x-auth-token", Constants.TOKEN)
        return chain.proceed(requestBuilder.build())

    }

}
