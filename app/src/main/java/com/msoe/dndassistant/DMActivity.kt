package com.msoe.dndassistant

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
    private lateinit var code: String
    private lateinit var viewJoinCode: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dm)
        recyclerView = findViewById(R.id.recyclerEncounters)
        addButton = findViewById(R.id.btnNewEncounter)
        viewJoinCode = findViewById(R.id.viewJoinCode)

        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )

        // Android 12+ doesn't need NEARBY_WIFI_DEVICES, but 13+ does
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        ActivityCompat.requestPermissions(
            this,
            permissions.toTypedArray(),
            1001
        )

        startHosting()

        viewJoinCode.text = code

        list = mutableListOf()

        adapter = EncounterAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            showAddEncounterDialog()
        }
    }

    private fun showAddEncounterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_encounter_dialog, null)

        val nameField = dialogView.findViewById<EditText>(R.id.editName)
        val acField = dialogView.findViewById<EditText>(R.id.editAC)
        val hpMaxField = dialogView.findViewById<EditText>(R.id.editHPMax)
        val hpCurrentField = dialogView.findViewById<EditText>(R.id.editHPCurrent)
        val initiativeField = dialogView.findViewById<EditText>(R.id.editInitiative)

        AlertDialog.Builder(this)
            .setTitle("Add Monster")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameField.text.toString().ifBlank { "Unknown" }
                val ac = acField.text.toString().toIntOrNull() ?: 10
                val hpMax = hpMaxField.text.toString().toIntOrNull() ?: 10
                val hpCurrent = hpCurrentField.text.toString().toIntOrNull() ?: hpMax
                val initiative = initiativeField.text.toString().toIntOrNull() ?: 0

                val newEncounter = Encounter(name, ac, hpMax, hpCurrent, initiative)
                adapter.addItem(newEncounter)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startHosting() {
        code = generateRandomCode()
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