package com.anas.aiassistant.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.anas.aiassistant.domain.viewModel.AppDatabase
import com.anas.aiassistant.model.OpenAiApi
import com.anas.aiassistant.model.OpenAiImpl
import com.anas.aiassistant.model.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .build()
            ).build()
    }

    @Provides
    @Singleton
    fun provideOpenAiApi(retrofit: Retrofit):OpenAiApi {
        return retrofit.create(OpenAiApi::class.java)
    }


    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(context: Context):Context{
        return context
    }


    @Provides
    @Singleton
    fun provideOPenAiRepo(db:AppDatabase, openAiApi: OpenAiApi):Repository{
        return OpenAiImpl(
            db,
            openAiApi
        )
    }

    @Provides
    @Singleton
    fun provideDb(context: Application):AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app-database"
        ).build()
    }



}