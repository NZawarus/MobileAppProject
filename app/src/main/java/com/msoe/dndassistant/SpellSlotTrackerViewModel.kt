package com.msoe.dndassistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpellSlotTrackerViewModel : ViewModel() {

    // Maps spell level -> list of boolean values (each for a checkbox)
    private val _spellSlotStates = MutableLiveData<Map<Int, List<Boolean>>>()

    val spellSlotStates: LiveData<Map<Int, List<Boolean>>> get() = _spellSlotStates

    private val defaultSlots = mapOf(
        1 to 4, 2 to 3, 3 to 3, 4 to 3, 5 to 3,
        6 to 2, 7 to 2, 8 to 1, 9 to 1
    )

    init {
        resetAllSlots()
    }

    fun toggleSlot(level: Int, index: Int) {
        _spellSlotStates.value = _spellSlotStates.value?.toMutableMap()?.also { map ->
            val list = map[level]?.toMutableList()
            if (list != null && index in list.indices) {
                list[index] = !list[index]
                map[level] = list
            }
        }
    }

    fun resetAllSlots() {
        _spellSlotStates.value = defaultSlots.mapValues { (_, count) ->
            List(count) { false }
        }
    }

    fun setSpellSlotState(newState: Map<Int, List<Boolean>>) {
        _spellSlotStates.value = newState
    }

}