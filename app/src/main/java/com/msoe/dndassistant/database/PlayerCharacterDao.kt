package com.msoe.dndassistant.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerCharacterDao {
    @Insert
    suspend fun insert(playerCharacter: PlayerCharacter)

    @Query("SELECT * FROM characters")
    suspend fun getAll(): List<PlayerCharacter>

}