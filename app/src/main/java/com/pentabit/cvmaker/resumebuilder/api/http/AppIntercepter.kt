package com.pentabit.cvmaker.resumebuilder.api.http


import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.AUTH_TOKEN
import com.pentabit.cvmaker.resumebuilder.utils.Constants.TOKEN
import com.pentabit.cvmaker.resumebuilder.utils.SharePref
import com.pentabit.cvmaker.resumebuilder.utils.getToken
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
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
        requestBuilder.addHeader(
            "x-auth-token", AppsKitSDKPreferencesManager.getInstance()
                .getStringPreferences(AUTH_TOKEN, TOKEN)
        )
        return chain.proceed(requestBuilder.build())
    }

}
