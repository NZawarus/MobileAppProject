package com.msoe.dndassistant

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

class SpellSlotTrackerFragment : Fragment() {
    private val viewModel: SpellSlotTrackerViewModel by viewModels()
    private val checkBoxMap = mutableMapOf<Pair<Int, Int>, CheckBox>() // (level, index) -> checkbox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.spell_slot_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<LinearLayout>(R.id.spellSlotContainer)
        val resetButton = view.findViewById<Button>(R.id.resetButton)

        val spellSlotMap = mapOf(
            1 to 4, 2 to 3, 3 to 3, 4 to 3, 5 to 3,
            6 to 2, 7 to 2, 8 to 1, 9 to 1
        )

        for ((level, maxSlots) in spellSlotMap) {
            val groupLabel = TextView(context).apply {
                text = "Level $level Spells"
                textSize = 18f
                setPadding(0, 24, 0, 8)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }

            val checkBoxGroup = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_HORIZONTAL // center children (checkboxes)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            for (i in 0 until maxSlots) {
                val checkBox = CheckBox(context).apply {
                    scaleX = 2.0f
                    scaleY = 2.0f

                    setOnCheckedChangeListener { _, isChecked ->
                        viewModel.toggleSlot(level, i)
                    }
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(16,8,16,8)
                    }
                    this.layoutParams = layoutParams
                }
                checkBoxMap[level to i] = checkBox
                checkBoxGroup.addView(checkBox)
            }

            container.addView(groupLabel)
            container.addView(checkBoxGroup)
        }

        // Observe LiveData from ViewModel
        viewModel.spellSlotStates.observe(viewLifecycleOwner, Observer { state ->
            state.forEach { (level, slots) ->
                slots.forEachIndexed { i, checked ->
                    checkBoxMap[level to i]?.apply {
                        // Detach listener to prevent loop
                        setOnCheckedChangeListener(null)

                        // Only update if different
                        if (isChecked != checked) {
                            isChecked = checked
                        }

                        // Reattach listener
                        setOnCheckedChangeListener { _, isChecked ->
                            val currentState = viewModel.spellSlotStates.value ?: return@setOnCheckedChangeListener
                            val currentLevelState = currentState[level]?.toMutableList() ?: return@setOnCheckedChangeListener

                            // Apply cascading check/uncheck logic
                            if (isChecked) {
                                for (j in 0..i) {
                                    currentLevelState[j] = true
                                }
                            } else {
                                for (j in i until currentLevelState.size) {
                                    currentLevelState[j] = false
                                }
                            }

                            // Update the ViewModel with the new state
                            val newState = currentState.toMutableMap()
                            newState[level] = currentLevelState
                            viewModel.setSpellSlotState(newState) // We'll add this method in your ViewModel
                        }

                    }
                }
            }
        })

        // Reset button
        resetButton.setOnClickListener {
            viewModel.resetAllSlots()
        }
    }
}

