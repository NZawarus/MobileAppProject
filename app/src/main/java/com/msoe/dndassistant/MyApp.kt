package com.msoe.dndassistant

import android.app.Application
import android.content.Intent
import androidx.room.Room
import com.msoe.dndassistant.database.AppDatabase
import com.msoe.dndassistant.database.MessageHandler

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // MessageHandler.database = AppDatabase.getDatabase(this)
    }
}