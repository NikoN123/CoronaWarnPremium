package com.example.coronawarnpremium.services

import android.R
import android.content.Context
import android.util.Log
import android.view.View
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coronawarnpremium.classes.PersonContactDiary
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabase
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabaseClient
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.text.Charsets.UTF_8


private const val TAG = "NearbyConnectionsApi"
class ConnectionsApiService(private val context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    private val STRATEGY = Strategy.P2P_CLUSTER
    private val service_id = "com.example.coronawarnpremium"
    private val connectionsClient: ConnectionsClient = Nearby.getConnectionsClient(context)
    private lateinit var otherUsersId: String
    private lateinit var userId: String

    /** Start the periodic background work process.  **/
    override suspend fun doWork(): Result {
        return try{
            Log.v(TAG, "Starting connections service")
            connectionsClient.stopAdvertising()
            connectionsClient.stopDiscovery()
            userId = inputData.getString("userId").toString()
            startConnectionsService()
            return Result.success()
        } catch(e: Exception){
            Result.failure()
        }
    }

    /** Starts looking for other users to initiate a connection.  **/
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
            .addOnFailureListener { e: Exception? -> throw e!! }
    }

    /** Starts looking for other users using Nearby Connections.  **/
    private fun startDiscovery() {
        val discoveryOptions =
            DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        Nearby.getConnectionsClient(context)
            .startDiscovery(service_id, endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener {
                Log.v(TAG, "Discovery started successfully")
            }
            .addOnFailureListener { e: java.lang.Exception? -> throw e!!
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
                Log.i(TAG, "onDisconnected: disconnected from the other user")

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
                Log.e(TAG, "UserId: $userId endpoitId: $endpointId connection: $connectionLifecycleCallback")
                connectionsClient.requestConnection(
                    userId,
                    endpointId,
                    connectionLifecycleCallback
                )
            }

            override fun onEndpointLost(endpointId: String) {
                Log.v(TAG, "Endpoint $endpointId lost")
            }
        }

    /** Disconnects from the user.  */
    fun disconnect() {
        connectionsClient.disconnectFromEndpoint(otherUsersId)
    }

    /** Finds other users with using Nearby Connections.  */
    private fun startConnectionsService() {
        startAdvertising()
        startDiscovery()
        Log.v(TAG, "Searching for devices")
    }

    /** Callbacks for receiving payloads **/
    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            //  call function to save payload.asBytes()!! in database
            // opponentChoice = GameChoice.valueOf(String(payload.asBytes()!!, UTF_8))

            Log.v(TAG, "User found with Id: ${String(payload.asBytes()!!)}")
            val repClass = ReplacementClass(context)
            repClass.checkContactDate(String(payload.asBytes()!!))
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

class ReplacementClass(private val context: Context) : CoroutineScope by MainScope(){
    fun checkContactDate(userId: String){
        launch(Dispatchers.Main) {
            val uuid = UUID.randomUUID()
            val time = LocalDateTime.now()
            val date = LocalDate.now()
            val client = ContactDiaryDatabaseClient(context)
            val person = client.checkContactDate(userId, date)
            if (person == null) {
                val encounter = PersonContactDiary(
                        PersondId = uuid.toString(),
                        UserId = userId,
                        Name = "",
                        EMail = "",
                        Location = "",
                        Infected = false,
                        Merged = false,
                        EncounterDate = date,
                        EncounterTime = time.format(DateTimeFormatter.ISO_LOCAL_TIME),
                        DateString = LocalDateTime.now().toString()
                )
                client.insert(encounter)
                Log.v(TAG, "New encounter saved.")
            }
            Log.v(TAG, "Person already encountered today.")
        }
    }
}