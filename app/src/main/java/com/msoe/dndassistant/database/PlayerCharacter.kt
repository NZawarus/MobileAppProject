package com.msoe.dndassistant.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "characters")
data class PlayerCharacter (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "level") var level: Int,
    @ColumnInfo(name = "race") var race: String,
    @ColumnInfo(name = "armorClass") var armorClass: Int,
    @ColumnInfo(name = "hp_max") var hp_max: Int,
    @ColumnInfo(name = "hp_current") var hp_current: Int,
    @ColumnInfo(name = "initiative") var initiative: Int

)