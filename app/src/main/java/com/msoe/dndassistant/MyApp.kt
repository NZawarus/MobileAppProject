package com.msoe.dndassistant

import android.app.Application
import androidx.room.Room
import com.msoe.dndassistant.database.AppDatabase
import com.msoe.dndassistant.database.MessageHandler

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "characters").build()
        MessageHandler.database = db
    }
}