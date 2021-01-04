package com.example.coronawarnpremium.ui.contactdiary

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.ui.contactbook.ContactBookViewModel
import java.util.*

class ContactDiaryFragment : Fragment() {

    private lateinit var viewModel: ContactDiaryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var context = activity
        viewModel =
                ViewModelProvider(this).get(ContactDiaryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contact_book, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactDiaryViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun getListOfDates(){
        val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.GERMANY)
        val currentDate = sdf.format(Date()).toString()
    }

}