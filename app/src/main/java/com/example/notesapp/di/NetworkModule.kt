package com.example.notesapp.di

import com.example.notesapp.api.AuthInterceptor
import com.example.notesapp.api.NotesAPI
import com.example.notesapp.api.UserAPI
import com.example.notesapp.models.User
import com.example.notesapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)


    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor):OkHttpClient{
        return OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI{
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient):NotesAPI{
        return retrofitBuilder.client(okHttpClient).build().create(NotesAPI::class.java)
    }

}