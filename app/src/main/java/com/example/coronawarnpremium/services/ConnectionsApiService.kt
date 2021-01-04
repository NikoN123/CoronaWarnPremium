package com.example.coronawarnpremium.services

import android.R
import android.content.Context
import android.util.Log
import android.view.View
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlin.text.Charsets.UTF_8


private const val TAG = "NearbyConnectionsApi"
class ConnectionsApiService(private val context: Context, private val userId: String) {
    private val STRATEGY = Strategy.P2P_CLUSTER
    private val service_id = "com.example.coronawarnpremium"
    private val connectionsClient: ConnectionsClient = Nearby.getConnectionsClient(context)
    private lateinit var otherUsersId: String

    private fun startAdvertising() {
        val advertisingOptions: AdvertisingOptions =
            AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        Nearby.getConnectionsClient(context)
            .startAdvertising(
                userId, service_id, connectionLifecycleCallback, advertisingOptions
            )
            .addOnSuccessListener {
                Log.v(TAG, "Advertiser started successfully")
            }
            .addOnFailureListener { e: Exception? -> }
    }

    /** Starts looking for other players using Nearby Connections.  */
    private fun startDiscovery() {
        val discoveryOptions =
            DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        Nearby.getConnectionsClient(context)
            .startDiscovery(service_id, endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener {
                Log.v(TAG, "Discoverystarted successfully")
            }
            .addOnFailureListener { e: java.lang.Exception? ->
                //Discovering not possible
            }
    }

    /** Callbacks for connections to other devices **/
    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(
                endpointId: String,
                connectionInfo: ConnectionInfo
            ) {
                Log.i(TAG, "onConnectionInitiated: accepting connection")
                connectionsClient.acceptConnection(endpointId, payloadCallback)
            }

            override fun onConnectionResult(
                endpointId: String,
                result: ConnectionResolution
            ) {
                if (result.status.isSuccess) {
                    Log.i(TAG, "onConnectionResult: connection successful")
                    otherUsersId = endpointId
                    connectionsClient.sendPayload(
                        endpointId, Payload.fromBytes(userId.toByteArray(UTF_8))
                    )
                } else {
                    Log.i(TAG, "onConnectionResult: connection failed")
                }
            }

            override fun onDisconnected(endpointId: String) {
                Log.i(TAG, "onDisconnected: disconnected from the opponent")

            }
        }

    /** Callbacks for finding other devices **/
    private val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(
                endpointId: String,
                info: DiscoveredEndpointInfo
            ) {
                Log.i(TAG, "onEndpointFound: endpoint found, connecting")
                connectionsClient.requestConnection(
                    userId,
                    endpointId,
                    connectionLifecycleCallback
                )
            }

            override fun onEndpointLost(endpointId: String) {}
        }

    /** Disconnects from the user.  */
    fun disconnect() {
        connectionsClient.disconnectFromEndpoint(otherUsersId)
    }

    /** Finds other users with using Nearby Connections.  */
    suspend fun startConnectionsService() {
        startAdvertising()
        startDiscovery()
        Log.v(TAG, "Searching for devices")
    }

    /** Callbacks for receiving payloads **/
    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            //  call function to save payload.asBytes()!! in database
            // opponentChoice = GameChoice.valueOf(String(payload.asBytes()!!, UTF_8))
            Log.v(TAG, String(payload.asBytes()!!))
            //disconnect
            disconnect()
        }

        override fun onPayloadTransferUpdate(
            endpointId: String,
            update: PayloadTransferUpdate
        ) {
            if (update.status == PayloadTransferUpdate.Status.SUCCESS && otherUsersId != "") {
                //finishRound()
            }
        }
    }
}