package com.example.coronawarnpremium.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.Infected
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.ApiService
import com.example.coronawarnpremium.services.DashboardService
import com.example.coronawarnpremium.storage.contactdiary.ContactDiaryDatabaseClient
import com.example.coronawarnpremium.storage.user.UserDatabaseClient
import com.example.coronawarnpremium.ui.userrisk.UserHighRiskActivity
import com.example.coronawarnpremium.ui.userrisk.UserRiskActivity
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.util.*

private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), CoroutineScope by MainScope() {
    private val dashboardService = DashboardService()
    private var infectionStatusBool: Boolean = false
    private var riskBool: Boolean = false

    private lateinit var user: User

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

    private lateinit var infectedButton: Button
    private lateinit var notInfectedButton: Button

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var myContext: Context

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        myContext = root.context

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

        infectedButton = root.findViewById(R.id.infectedButton)
        infectedButton.setOnClickListener {
            infect()
        }
        notInfectedButton = root.findViewById(R.id.notInfectedButton)
        notInfectedButton.setOnClickListener {
            deInfect()
        }

        val faqCard = root.findViewById<CardView>(R.id.faqCard)
        faqCard.setOnClickListener {
            openFaq()
        }

        launch(Dispatchers.Main) {
            setUserData()
            ApiService.UserAdapter.setUserToken(user.token)
        }
        launch(Dispatchers.Main) {
            delay(800)
            getInfectionStatus()
        }
        launch(Dispatchers.Main) {
            getRiskCardParams(root.context)
        }

        riskCard.setOnClickListener {
            navigateUserRiskActivity(it)
        }

        return root
    }
    private suspend fun getInfectionStatus(){
            try {
                infectionStatusBool = user.infected
            }
            catch (e: Exception){
                Toast.makeText(context,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
            }
    }
    private suspend fun getRiskCardParams(context: Context){
            try{
                riskBool = dashboardService.checkUserRiskEncounters(context)
                if(!riskBool){
                    setUserRiskGreen()
                }
                else {
                    setUserRiskRed()
                }
            }
            catch (e: Exception){
                Toast.makeText(context,
                        "Error Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
            }
    }
    private fun setUserRiskGreen(){
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
    private fun setUserRiskRed(){
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
    fun navigateUserRiskActivity(view: View){
        val intent: Intent
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
                user = client.getAllUsers()!!
                username.text = user.username
                email.text = user.eMail
                if (!user.infected) {
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

    private fun infect(){
        launch(Dispatchers.Main){
            try {
                val encounters = ContactDiaryDatabaseClient(myContext).getAllContacts()
                var list = ArrayList<Infected>()
                encounters.forEach {
                    val myStr = LocalDateTime.parse(it.DateString)
                    val myStr2 = "Z"
                    if(it.Location.isEmpty()){
                        val infected = Infected(
                                id = it.UserId,
                                date = "$myStr$myStr2",
                                location = "none"
                        )
                        list.add(infected)
                        Log.e(TAG, "Empty: $infected")
                    }
                    else {
                        val myDate = LocalDateTime.parse(it.DateString).toString()
                        val infected = Infected(
                                id = it.UserId,
                                date = "${myDate}Z",
                                location = it.Location
                        )
                        list.add(infected)
                        Log.e(TAG, infected.toString())
                    }
                }
                val response = ApiService.UserAdapter.tokenClient.infectWithId(user.id, list)
                //val response = ApiService.UserAdapter.tokenClient.infectAsync(user.id)
                if (response.isSuccessful) {
                    Toast.makeText(view?.context, "Other users will be notified about your infection.", Toast.LENGTH_SHORT).show()
                    infectionStatus.background.setTintList(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.RED)))
                    setUserRiskRed()
                    infectionStatusBool = true
                    riskBool = true
                }
                else{
                    Toast.makeText(view?.context, "$response", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(view?.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun deInfect(){
        launch(Dispatchers.Main){
            try {
                val response = ApiService.UserAdapter.userClient.deInfectAsync(user.id)
                if (response.isSuccessful) {
                    Toast.makeText(view?.context, "Other users will be notified that you are not infected anymore.", Toast.LENGTH_SHORT).show()
                    infectionStatus.background.setTintList(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.GREEN)))
                    setUserRiskGreen()
                    infectionStatusBool = false
                    riskBool = false
                }
                else{
                    Toast.makeText(view?.context, "Error, please try again.", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(view?.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openFaq(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coronawarn.app/de/faq/"))
        startActivity(browserIntent)
    }
}