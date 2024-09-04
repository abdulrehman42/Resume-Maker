package com.pentabit.cvmaker.resumebuilder.di

import com.pentabit.cvmaker.resumebuilder.BuildConfig
import com.pentabit.cvmaker.resumebuilder.api.http.AppIntercepter
import com.pentabit.cvmaker.resumebuilder.api.http.ChooseTemplateService
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("GsonRetrofit")
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().apply {
           /* if (BuildConfig.DEBUG)
            {
                baseUrl(Constants.BASE_URL_DEVELOPMENT)

            }else{*/
                baseUrl(Constants.BASE_URL_PRODUCTION)
           // }
            client(provideOkHttpClient(AppIntercepter()))
            addConverterFactory(GsonConverterFactory.create())
        }
    }

    @Singleton
    @Provides
    @Named("ScalarsRetrofit")
    fun providesRetrofitWithScalarsConverterFactory(): Retrofit.Builder {
        return Retrofit.Builder().apply {
           /* if (BuildConfig.DEBUG)
            {
                baseUrl(Constants.BASE_URL_DEVELOPMENT)

            }else{*/
                baseUrl(Constants.BASE_URL_PRODUCTION)
           // }
            client(provideOkHttpClient(AppIntercepter()))
            addConverterFactory(ScalarsConverterFactory.create())
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AppIntercepter): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(4, TimeUnit.MINUTES).build()
    }


    @Singleton
    @Provides
    @Named("GsonService")
    fun chooseTemplate(@Named("GsonRetrofit") retrofitBuilder: Retrofit.Builder): ChooseTemplateService {
        return retrofitBuilder.build().create(ChooseTemplateService::class.java)
    }

    @Singleton
    @Provides
    @Named("ScalarsService")
    fun chooseTemplateWithScalarsConverterFactory(@Named("ScalarsRetrofit") retrofitBuilder: Retrofit.Builder): ChooseTemplateService {
        return retrofitBuilder.build().create(ChooseTemplateService::class.java)
    }

}