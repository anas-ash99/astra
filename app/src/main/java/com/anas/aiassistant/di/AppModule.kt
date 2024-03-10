package com.anas.aiassistant.di

import ai.picovoice.porcupine.Porcupine
import ai.picovoice.porcupine.PorcupineManager
import android.app.Application
import android.content.Context
import android.speech.SpeechRecognizer
import androidx.room.Room
import com.anas.aiassistant.domain.db.AppDatabase
import com.anas.aiassistant.domain.db.ChatDao
import com.anas.aiassistant.domain.db.MessageDao
import com.anas.aiassistant.model.DatabaseRepository
import com.anas.aiassistant.model.OpenAiApi
import com.anas.aiassistant.model.OpenAiImpl
import com.anas.aiassistant.model.RemoteRepository
import com.anas.aiassistant.model.RoomDBRepo
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
    fun provideOPenAiRepo(openAiApi: OpenAiApi):RemoteRepository{
        return OpenAiImpl(
            openAiApi
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseRepo(chatDao: ChatDao, messageDao: MessageDao):DatabaseRepository{
        return RoomDBRepo(
            chatDao,
            messageDao
        )
    }


    @Provides
    @Singleton
    fun provideDb(context: Application): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMessageDao(db: AppDatabase): MessageDao {
        return db.messageDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(db: AppDatabase): ChatDao {
        return db.chatDao()
    }
    @Provides
    @Singleton
    fun provideSpeechRecognizer(context: Application):SpeechRecognizer{
        return SpeechRecognizer.createSpeechRecognizer(context)
    }

    @Provides
    @Singleton
    fun providePorcupineManager(context: Application):PorcupineManager{
        val keywords = arrayOf(Porcupine.BuiltInKeyword.PORCUPINE, Porcupine.BuiltInKeyword.BUMBLEBEE)
        val accessKey = "xt5oThE5dlH78GexSdzPrXH6mRr+QGF0KsD57D77o7LddS9mWvivyw=="
        val keywordPath = "file://${context.assets}/Astra_en_android_v3_0_0.ppn"
        return PorcupineManager.Builder()
            .setAccessKey(accessKey)
            .setKeyword(Porcupine.BuiltInKeyword.BUMBLEBEE)
            .build(context) { keywordIndex ->
                if (keywordIndex == 0) {
                    // BUMBLEBEE detected
//                    mainViewModel.onMickClick(speechRecognizer)
                }
            }
    }

}