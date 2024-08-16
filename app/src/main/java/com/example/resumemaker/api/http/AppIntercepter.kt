package com.example.resumemaker.api.http


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

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjBlYWM5Mjc3LWNjYjgtNDAyOC1hM2UxLTYzNWI1OTM2NGU4NyIsIm5hbWUiOm51bGwsImVtYWlsIjoiYS5yZWhtYW5AcGVudGFiaXRsYWIuY29tIiwib2F1dGhJZCI6InN0cmluZyIsInBob25lTGFuZ3VhZ2UiOm51bGwsIm9hdXRoUHJvdmlkZXIiOiJsaW5rZWRJbiIsImlzRGVsZXRlZCI6ZmFsc2UsInByb2ZpbGVzIjpbXSwidXNlclR5cGUiOiJ1c2VyIiwiaWF0IjoxNzIzNzk1OTA0fQ.XYxqURcuhmHRoOGy4u2mexlcojcE1acqQroiq8AmjnQ"
        requestBuilder.addHeader("x-auth-token", "$token")
        return chain.proceed(requestBuilder.build())

    }

}
