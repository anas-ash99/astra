package com.anas.aiassistant.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anas.aiassistant.model.ChatForDB
import com.anas.aiassistant.model.Message


@Database(
    entities = [Message::class, ChatForDB::class],
    version = 3,
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao

}