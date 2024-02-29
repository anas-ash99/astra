package com.anas.aiassistant.domain.viewModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anas.aiassistant.domain.viewModel.db.MessageDao
import com.anas.aiassistant.model.Message


@Database(
    entities = [Message::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun messageDao():MessageDao
}