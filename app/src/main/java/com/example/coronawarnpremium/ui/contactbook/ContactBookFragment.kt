package com.example.coronawarnpremium.ui.contactbook

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.ui.home.HomeViewModel

class ContactBookFragment : Fragment() {

    private lateinit var viewModel: ContactBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var context = activity
        viewModel =
            ViewModelProvider(this).get(ContactBookViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contact_book, container, false)

        return root
    }
}