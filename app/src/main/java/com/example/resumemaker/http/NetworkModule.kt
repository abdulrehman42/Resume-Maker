package com.example.resumemaker.http

/*
import com.example.resumemaker.utils.Constants
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
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(provideOkHttpClient(AppIntercepter()))
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AppIntercepter): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(4, TimeUnit.MINUTES).build()
    }

   */
/* @Singleton
    @Provides
    fun providesAuthAPI(retrofitBuilder: Retrofit.Builder): AuthService {
        return retrofitBuilder.build().create(AuthService::class.java)
    }*//*





}*/
