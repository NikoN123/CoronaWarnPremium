package com.example.coronawarnpremium.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.room.Room
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.User
import com.example.coronawarnpremium.services.DashboardService
import com.example.coronawarnpremium.storage.user.UserDao
import com.example.coronawarnpremium.storage.user.UserDatabase
import com.example.coronawarnpremium.ui.startup.login.LoginActivity
import com.example.coronawarnpremium.ui.userrisk.UserHighRiskActivity
import com.example.coronawarnpremium.ui.userrisk.UserRiskActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), CoroutineScope by MainScope() {
    private val dashboardService = DashboardService()
    private var infectionStatusBool: Boolean = false
    private var riskBool: Boolean = false

    //create UI Elements to identify them later
    private lateinit var listView:ListView
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var imei: TextView
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

    //list of tips
    private var healthTipsList = ArrayList<String>()

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //initialize elements
        listView = root.findViewById(R.id.healthTipsList)

        //main card
        username = root.findViewById(R.id.dashboardUsername)
        email = root.findViewById(R.id.dashboardEmail)
        imei = root.findViewById(R.id.dashboardIMEI)
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



        //get list of current tips
        getList()
        //get infection status
        getInfectionStatus()
        //get risk card data
        getRiskCardParams()
        //get device count
        //getUserData()

        riskCard.setOnClickListener {
            navigateUserRiskActivity(it)
        }

        return root
    }

    private fun getUserData(){

        /*username.text = user.Username
        email.text = user.EMail
        imei.text = user.IMEI*/
    }
    private fun getList(){
        launch(Dispatchers.Main){
            try {
                healthTipsList = dashboardService.getHealthTips()

                //set adapter
                val adapter : ArrayAdapter<String>? =
                    context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, healthTipsList) }
                listView.adapter = adapter
                adapter?.notifyDataSetChanged()

                var totalHeight = listView.paddingTop + listView.paddingBottom
                for (i in 0 until adapter?.count!!) {
                    var listItem = adapter.getView(i, null, listView)
                    (listItem as? ViewGroup)?.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    listItem.measure(0, 0)
                    totalHeight += listItem.measuredHeight
                }

                var params = listView.layoutParams
                params.height = totalHeight + (listView.dividerHeight * (adapter?.count-1))
                listView.layoutParams = params
            }
            catch(e: Exception){
                Toast.makeText(context,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getInfectionStatus(){
        launch(Dispatchers.Main){
            try {
                infectionStatusBool = dashboardService.getInfectionStatus()
                if (!infectionStatusBool) {
                    infectionStatus.background.setTintList(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.GREEN)))
                } else {
                    infectionStatus.background.setTintList(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.RED)))
                }
            }
            catch(e:Exception){
                Toast.makeText(context,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getRiskCardParams(){
        launch(Dispatchers.Main){
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
}