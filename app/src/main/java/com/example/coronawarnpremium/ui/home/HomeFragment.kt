package com.example.coronawarnpremium.ui.home

import android.content.Intent
import android.util.Log
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.services.DashboardService
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.example.coronawarnpremium.ui.userrisk.UserHighRiskActivity
import com.example.coronawarnpremium.ui.userrisk.UserRiskActivity
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), CoroutineScope by MainScope() {
    private val dashboardService = DashboardService()
    private var infectionStatusBool: Boolean = false
    private var riskBool: Boolean = false

    //create UI Elements to identify them later
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var infectionStatus: TextView
    //risk card elements
    private lateinit var userRisk: TextView
    private lateinit var userRiskEncounters: TextView
    private lateinit var userSafety: TextView
    private lateinit var userStatusUpdated: TextView
    private lateinit var imageRisk: ImageView
    private lateinit var imageNoRisk: ImageView
    private lateinit var imageVirus: ImageView
    private lateinit var imageUpdated: ImageView
    private lateinit var riskCard: CardView

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        username = root.findViewById(R.id.dashboardUsername)
        email = root.findViewById(R.id.dashboardEmail)
        infectionStatus = root.findViewById(R.id.infectionStatus)
        //risk card
        userRisk = root.findViewById(R.id.userRisk1)
        userRiskEncounters = root.findViewById(R.id.userRiskEncounters1)
        userSafety = root.findViewById(R.id.userSafety1)
        userStatusUpdated = root.findViewById(R.id.userStatusUpdated1)
        imageRisk = root.findViewById(R.id.imageRisk1)
        imageNoRisk = root.findViewById(R.id.imageNoRisk1)
        imageVirus = root.findViewById(R.id.imageVirus1)
        imageUpdated = root.findViewById(R.id.imageUpdated1)
        riskCard = root.findViewById(R.id.riskEncounterCard)


        launch(Dispatchers.Main) {
            setUserData()
        }
        launch(Dispatchers.Main) {
            getInfectionStatus()
        }
        launch(Dispatchers.Main) {
            getRiskCardParams()
        }

        riskCard.setOnClickListener {
            navigateUserRiskActivity(it)
        }

        return root
    }
    private suspend fun getInfectionStatus(){
            try {
                infectionStatusBool = dashboardService.getInfectionStatus()
            }
            catch(e:Exception){
                Toast.makeText(context,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
    }
    private suspend fun getRiskCardParams(){
            try{
                riskBool = dashboardService.checkUserRiskEncounters()
                if(riskBool){
                    riskCard.setCardBackgroundColor(Color.parseColor("#8E3A3A"))
                    //set text
                    userRisk.text = "High risk"
                    userRiskEncounters.text = "There has been a risk encounter lately"
                    userSafety.text = "Risk detection active"
                    val sdf = SimpleDateFormat("hh:mm:ss", Locale.GERMANY)
                    val currentDate = sdf.format(Date()).toString()
                    val updateString = "Last updated: $currentDate"
                    userStatusUpdated.text = updateString
                }
                else {
                    riskCard.setCardBackgroundColor(Color.parseColor("#568753"))
                    //set text
                    val riskText = "Low risk"
                    val riskEncounterText = "There has been no risk encounter lately"
                    val safetyText = "Risk detection active"
                    userRisk.text = riskText
                    userRiskEncounters.text = riskEncounterText
                    userSafety.text = safetyText
                    val sdf = SimpleDateFormat("hh:mm:ss", Locale.GERMANY)
                    val currentDate = sdf.format(Date()).toString()
                    val updateString = "Last updated: $currentDate"
                    userStatusUpdated.text = updateString
                }
            }
            catch(e: Exception){
                Toast.makeText(context,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
            }
    }
    fun navigateUserRiskActivity(view: View){
        var intent: Intent
        if(!infectionStatusBool && !riskBool){
            intent = Intent(activity, UserRiskActivity::class.java)
        }
        else{
            intent = Intent(activity, UserHighRiskActivity::class.java)
        }
        startActivity(intent)
    }

    private suspend fun setUserData(): Boolean{
        var counter = 0
        while (counter < 5) {
            try {
                Log.v(TAG, "Setting user data")
                val client = UserDatabaseClient(requireActivity().baseContext)
                val user = client.getAllUsers()
                username.text = user?.Username
                email.text = user?.EMail
                if (!user?.Infected!!) {
                    infectionStatus.background.setTintList(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.GREEN)))
                } else {
                    infectionStatus.background.setTintList(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.RED)))
                }
                break
            } catch (e: Exception) {
                counter += 1
                delay(500)
            }
        }
        return false
    }
}