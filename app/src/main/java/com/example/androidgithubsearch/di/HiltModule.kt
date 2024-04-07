package com.example.androidgithubsearch.di

import android.content.Context
import androidx.room.Room
import com.example.androidgithubsearch.api.GitHubApiService
import com.example.androidgithubsearch.database.AppDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideGitHubApiService(): GitHubApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
        
        return retrofit.create(GitHubApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    
    @Provides
    @Singleton
    fun provideUserRepositoryDao(database: AppDatabase) = database.userRepositoryDao()
    
    @Provides
    @Singleton
    fun provideFavoriteRepositoryDao(database: AppDatabase) = database.favoriteRepositoryDao()
}