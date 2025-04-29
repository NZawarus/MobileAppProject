package com.msoe.dndassistant.network

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionsClient

object SessionManager {
    var isHost = false
    var sessionCode: String? = null
    var endpointId: String? = null
    var connectionsClient: ConnectionsClient? = null

    fun initialize(context: Context) {
        connectionsClient = Nearby.getConnectionsClient(context)
    }

    fun reset() {
        isHost = false
        sessionCode = null
        endpointId = null
        connectionsClient?.stopAllEndpoints()
    }
}
