package com.msoe.dndassistant

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class EncounterAdapter(private val dataset: MutableList<Encounter>) : RecyclerView.Adapter<EncounterAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.encounterName)
        val textView2 = view.findViewById<TextView>(R.id.encounterInfo)
        val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.encounter_fragment, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from dataset at this position and replace the
        // contents of the view with that element
        val item = dataset[position]
        viewHolder.textView.text = item.name
        viewHolder.textView2.text = "AC: ${item.armorClass}, " +
                "HP: ${item.hpCurrent}/${item.hpMax}, " +
                "Initiative: ${item.initiative}"

        viewHolder.deleteButton.setOnClickListener {
            removeItem(viewHolder.adapterPosition)
        }

        viewHolder.itemView.setOnClickListener {
            if(position != RecyclerView.NO_POSITION){
                showEditHPDialog(viewHolder.itemView, position)
            }
        }
    }

    private fun showEditHPDialog(view: View, position: Int) {
        val encounter = dataset[position]

        val input = EditText(view.context)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText(encounter.hpCurrent.toString())

        AlertDialog.Builder(view.context)
            .setTitle("Edit HP")
            .setMessage("Enter new HP value:")
            .setView(input)
            .setPositiveButton("OK") {_, _ ->
                val newHP = input.text.toString().toIntOrNull()
                if(newHP != null){
                    encounter.hpCurrent = newHP
                    if(newHP <= 0){
                        removeItem(position)
                    } else {
                        notifyItemChanged(position)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun addItem(item: Encounter) {
        // Insert item in descending initiative order
        val insertIndex = dataset.indexOfFirst { it.initiative < item.initiative }
            .takeIf { it != -1 } ?: dataset.size

        dataset.add(insertIndex, item)
        notifyItemInserted(insertIndex)
    }


    fun removeItem(pos: Int){
        if(pos in dataset.indices){
            dataset.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, itemCount)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}