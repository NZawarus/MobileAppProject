package com.msoe.dndassistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EncounterAdapter(private val dataset: MutableList<Encounter>) : RecyclerView.Adapter<EncounterAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.encounterName)
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

        viewHolder.deleteButton.setOnClickListener {
            removeItem(viewHolder.adapterPosition)
        }
    }



    fun addItem(item: Encounter) {
        dataset.add(item)
        notifyItemInserted(dataset.size-1)
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