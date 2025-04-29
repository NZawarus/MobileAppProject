package com.msoe.dndassistant.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlayerCharacter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerCharacterDao(): PlayerCharacterDao
}