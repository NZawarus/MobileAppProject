package com.msoe.dndassistant.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "characters")
data class PlayerCharacter (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var level: Int,
    var race: String,
    var armorClass: Int,
    var hp_max: Int,
    var hp_current: Int,
    var initiative: Int

)