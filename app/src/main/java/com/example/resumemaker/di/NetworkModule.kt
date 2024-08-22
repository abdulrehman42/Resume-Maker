package com.example.resumemaker.di



import com.example.resumemaker.api.http.AppIntercepter
import com.example.resumemaker.api.http.ChooseTemplateService
import com.example.resumemaker.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {

        return Retrofit.Builder().apply {
            baseUrl(Constants.BASE_URL)
            client(provideOkHttpClient(AppIntercepter()))
            addConverterFactory(GsonConverterFactory.create())
        }    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AppIntercepter): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(4, TimeUnit.MINUTES).build()
    }

    @Singleton
    @Provides
    fun chooseTemplate(retrofitBuilder: Retrofit.Builder): ChooseTemplateService
    {
        return retrofitBuilder.build().create(ChooseTemplateService::class.java)
    }

}