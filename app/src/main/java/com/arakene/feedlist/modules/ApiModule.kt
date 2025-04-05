package com.arakene.feedlist.modules

import com.arakene.data.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level =  HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor {
                        it.proceed(
                            it.request().newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "3jIoUzX5NHTqXH84M41UumY676KIrp05LN4itIMRTOBferYjzF2zo8iE"
                                )
                                .build()
                        )
                    }
                    .build())
            .build()
            .create(Api::class.java)
    }

}