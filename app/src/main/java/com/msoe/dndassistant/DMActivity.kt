package com.msoe.dndassistant

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DMActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EncounterAdapter
    private lateinit var list: MutableList<Encounter>
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dm)
        recyclerView = findViewById(R.id.recyclerEncounters)
        addButton = findViewById(R.id.btnNewEncounter)

        list = mutableListOf(
            Encounter("Encounter 1"),
            Encounter("Encounter 2"))

        adapter = EncounterAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val newItem = Encounter("Encounter ${list.size+1}")
            adapter.addItem(newItem)
        }
    }
}