package com.msoe.dndassistant

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.msoe.dndassistant.network.SessionManager

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

    private fun startHosting() {
        val code = generateRandomCode()
        SessionManager.isHost = true
        SessionManager.sessionCode = code

        SessionManager.connectionsClient?.startAdvertising(
            code,
            packageName,
            connectionLifecycleCallback,
            AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
        )
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            SessionManager.connectionsClient?.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                SessionManager.endpointId = endpointId
                // Connection established
            }
        }

        override fun onDisconnected(endpointId: String) {
            // Handle disconnection
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            // Handle payload
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }

    private fun generateRandomCode(): String {
        return (1..4).map { ('A'..'Z').random() }.joinToString("")
    }
}