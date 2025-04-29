package com.msoe.dndassistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.msoe.dndassistant.network.SessionManager

class PlayerActivity : AppCompatActivity() {
    private lateinit var desiredCode: String
    private var discoveryStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)

        SessionManager.initialize(this)
        SessionManager.isHost = false

        val joinButton = findViewById<Button>(R.id.btnJoinSession)
        val joinCode = findViewById<EditText>(R.id.etSessionCode)

        joinButton.setOnClickListener {
            desiredCode = joinCode.text.toString().uppercase()
            if (desiredCode.length==4) {
                startDiscovery()
            } else {
                Toast.makeText(this, "Session codes must be 4 letters.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startDiscovery() {
        SessionManager.connectionsClient?.startDiscovery(
            packageName,
            endpointDiscoveryCallback,
            DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
        )
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            if (info.endpointName == desiredCode) { // You’d get this from user input
                SessionManager.connectionsClient?.requestConnection(
                    "Client",
                    endpointId,
                    connectionLifecycleCallback
                )
            }
        }

        override fun onEndpointLost(endpointId: String) {}
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            SessionManager.connectionsClient?.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if (result.status.isSuccess) {
                SessionManager.endpointId = endpointId
                goToPlayerSheet()
            }
        }

        override fun onDisconnected(endpointId: String) {}
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {}
        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }

    private fun goToPlayerSheet() {
        val intent = Intent(this, PlayerSheetActivity::class.java)
        startActivity(intent)
    }
}