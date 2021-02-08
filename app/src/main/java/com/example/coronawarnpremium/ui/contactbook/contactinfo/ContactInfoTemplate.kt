package com.example.coronawarnpremium.ui.contactbook.contactinfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.ui.contactbook.ContactBookFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

private const val TAG = "ContactInfoTemplate"
class ContactInfoTemplate : BottomSheetDialogFragment(), CoroutineScope by MainScope() {
    private var contactId: String = ""
    private var contactName: String = ""
    private var contactEmail: String = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.template_contact_info, container, false)
        val name: TextView = root.findViewById(R.id.contactInfoName)
        val email: TextView = root.findViewById(R.id.contactInfoMail)
        name.text = contactName
        email.text = contactEmail

        val button: Button = root.findViewById(R.id.deleteContact)
        button.setOnClickListener {
            Log.v(TAG, "Deleting contact $contactName")

            view?.let { it1 -> ContactBookFragment().onItemCLick(it1, contactId) }

            dismiss()
        }
        return root
    }



    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle, contactJson: String): ContactInfoTemplate {
            val fragment = ContactInfoTemplate()
            val gson = Gson()
            val contact = gson.fromJson(contactJson, Contact::class.java)
            fragment.contactName = contact.username
            fragment.contactEmail = contact.email
            fragment.contactId = contact.id
            fragment.arguments = bundle
            return fragment
        }
    }
}