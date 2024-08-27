package com.pentabit.cvmaker.resumebuilder.api.http


import com.pentabit.cvmaker.resumebuilder.BuildConfig
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.AUTH_TOKEN
import com.pentabit.cvmaker.resumebuilder.utils.Constants.GUEST_TOKEN
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

        // Choose the token based on the login status
        val token = if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.IS_LOGGED, false)
        ) {
            // If not logged in, use a different token or a guest token
            AppsKitSDKPreferencesManager.getInstance().getStringPreferences(
                AUTH_TOKEN
            )

        } else {
            // If logged in, use the logged-in token
            AppsKitSDKPreferencesManager.getInstance().getStringPreferences(
                GUEST_TOKEN
            )
        }


        // Build the request with the selected token
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("x-app-version", BuildConfig.VERSION_NAME)
            .addHeader("x-auth-token", token)
            .method(originalRequest.method, originalRequest.body)

        // Proceed with the chain
        return chain.proceed(requestBuilder.build())
    }
}
