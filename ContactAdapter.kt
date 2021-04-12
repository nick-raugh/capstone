package com.coinbase.krypty

import android.R.attr.label
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList


internal class ContactAdapter(private val context: Context, listContacts: ArrayList<Contacts>):
RecyclerView.Adapter<ContactViewHolder>(), Filterable{

    private var listContacts: ArrayList<Contacts>
    private val mArrayList: ArrayList<Contacts>
    private val mDatabase: SqliteDatabase

    //initialize the variables
    init{
        this.listContacts = listContacts
        this.mArrayList = listContacts
        mDatabase = SqliteDatabase(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_layout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contacts = listContacts[position]
        holder.tvName.text = contacts.name
        holder.tvPhoneNum.text = contacts.phno
        holder.editContact.setOnClickListener{
            editTaskDialog(contacts)
        }
        holder.deleteContact.setOnClickListener{
            mDatabase.deleteContact(contacts.id)
            (context as Activity).finish()
            context.startActivity(context.intent)
        }
        holder.copyContact.setOnClickListener {
            copyContact(contacts)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                listContacts = if (charString.isEmpty()){
                    mArrayList
                }
                else{
                    val filteredList = ArrayList<Contacts>()
                    for(contacts in mArrayList){
                        if(contacts.name.toLowerCase().contains(charString)){
                            filteredList.add(contacts)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = listContacts
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listContacts = filterResults.values as ArrayList<Contacts>
                notifyDataSetChanged()
            }
        }
    }
    private fun copyContact(contacts: Contacts){
        val contactAddress = contacts.phno.toString()
        System.out.println(contactAddress+"    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ")
        //Toast.makeText(context,"Address copied to clipboard",Toast.LENGTH_LONG).show()

        //copy the address to clipboard

        val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("Label", contactAddress)
        myClipboard.setPrimaryClip(myClip)

    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    private fun editTaskDialog(contacts: Contacts){
        val inflater = LayoutInflater.from(context)

        // create a subview within the activity for adding a contact
        val subView = inflater.inflate(R.layout.add_contacts,null)
        val nameField : EditText = subView.findViewById(R.id.enterName)
        val contactField : EditText = subView.findViewById(R.id.enterPhoneNum)

        nameField.setText(contacts.name)
        contactField.setText(contacts.phno)

        //change colours of button text

        // an alert for editing a contact
        val builder = AlertDialog.Builder(context,R.style.AlertDialogTheme)
        builder.setTitle("Edit contact")
        builder.setView(subView)
        builder.create()

        builder.setPositiveButton("EDIT CONTACT")
        { _, _ ->
            val name = nameField.text.toString()
            val phNo = contactField.text.toString()

            //check if the name field for the contact is empty
            if(TextUtils.isEmpty(name)){
                Toast.makeText(context,"Oops, something went wrong. Make sure there is a name entered.", Toast.LENGTH_LONG).show()
            }
            else{
                mDatabase.updateContacts(
                        Contacts(
                                Objects.requireNonNull<Any>(contacts.id) as Int,
                                name,
                                phNo
                        )
                )
                (context as Activity).finish()
                context.startActivity(context.intent)
            }
        }

        builder.setNegativeButton("CANCEL")
        { _, _ -> Toast.makeText(context, "Edit contact cancelled.", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }
}