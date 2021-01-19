package com.example.coronawarnpremium.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawarnpremium.R
import com.example.coronawarnpremium.classes.Contact
import com.example.coronawarnpremium.viewholders.ContactViewHolder

private const val TAG = "ContactAdapter"
class ContactAdapter () : RecyclerView.Adapter<ContactViewHolder>() {

    companion object{
        var contacts = ArrayList<Contact>()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_contact_card, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (contacts.isNotEmpty()) {
            contacts.size
        } else{
            0
        }
    }

    fun reloadData(){
        notifyDataSetChanged()
    }

    fun addNewData(contact: Contact){
        if(!contacts.contains(contact)) {
            contacts.add(contact)
            Log.v(TAG, "Contact added")
            notifyDataSetChanged()
        }
    }

    fun getContact(position: Int): Contact {
        return contacts[position]
    }

    fun removeContact(contact: Contact){
        Log.v(TAG, contacts.size.toString())
        val index = contacts.indexOfFirst { it.UserId == contact.UserId }
        contacts.removeAt(index)
        notifyDataSetChanged()
    }

    fun loadNewData(newList : ArrayList<Contact>){
        contacts.clear()
        contacts.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contactUsername.text = contacts[position].Username
        holder.contactEmail.text = contacts[position].EMail
    }
}