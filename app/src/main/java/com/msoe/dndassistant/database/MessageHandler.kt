package com.msoe.dndassistant.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MessageHandler {
    lateinit var database: AppDatabase

    fun handleMessage(message: String) {
        when {
            message.startsWith("ADD_USER:") -> {
                val name = message.removePrefix("ADD_USER:")
                CoroutineScope(Dispatchers.IO).launch {
                    database.playerCharacterDao().insert(PlayerCharacter(
                        database.playerCharacterDao().getAll().size+1,
                        name = name,
                        race = "Human",
                        level = 1,
                        hp_current = 10,
                        hp_max = 10,
                        armorClass = 10,
                        initiative = 0))
                }
            }
            // add more commands as needed
        }
    }
}
