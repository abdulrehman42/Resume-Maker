package com.pentabit.cvmaker.resumebuilder.api.http


import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.AUTH_TOKEN
import com.pentabit.cvmaker.resumebuilder.utils.Constants.TOKEN
import com.pentabit.cvmaker.resumebuilder.utils.Constants.TOKEN_AUTH
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

        // Get the instance of SharedPreferences manager
        val preferencesManager = AppsKitSDKPreferencesManager.getInstance()

        // Check if the user is logged in
        val isLoggedIn = preferencesManager.getBooleanPreferences(Constants.IS_LOGGED, false)
        val versionName=preferencesManager.getStringPreferences(Constants.VERSION_NAME)
        // Choose the token based on the login status
        val token = if (isLoggedIn) {
            // If logged in, use the logged-in token
            preferencesManager.getStringPreferences(AUTH_TOKEN, TOKEN_AUTH)

        } else {
            // If not logged in, use a different token or a guest token
            preferencesManager.getStringPreferences(AUTH_TOKEN, TOKEN)

        }


        // Build the request with the selected token
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("x-app-version",versionName)
            .addHeader("x-auth-token", token)
            .method(originalRequest.method, originalRequest.body)

        // Proceed with the chain
        return chain.proceed(requestBuilder.build())
    }
}
