package com.msoe.dndassistant

data class Encounter(
    val name: String,
    val armorClass: Int,
    val hpMax: Int,
    var hpCurrent: Int,
    val initiative: Int
)