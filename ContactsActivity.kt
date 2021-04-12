package com.coinbase.krypty

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*


class ContactsActivity : AppCompatActivity() {

    private lateinit var dataBase : SqliteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val contactView: RecyclerView = findViewById(R.id.myContactList)
        val linearLayoutManager = LinearLayoutManager(this)
        contactView.layoutManager = linearLayoutManager
        contactView.setHasFixedSize(true)
        dataBase = SqliteDatabase(this)
        var allContacts: ArrayList<Contacts> = dataBase.listContacts()


        // determine whether there are contacts to be displayed
        if(allContacts.size > 0){
            contactView.visibility= View.VISIBLE
            val mAdapter = ContactAdapter(this,allContacts)
            contactView.adapter = mAdapter
        }
        else{
            contactView.visibility=View.GONE
            //tell user the database is empty
            Toast.makeText(this,"No contacts stored.",Toast.LENGTH_LONG).show()
        }

        //add contact button
        val btnAdd: Button = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener{addTaskDialog()}
    }

    private fun addTaskDialog(){
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.add_contacts,null)
        val nameField : EditText = subView.findViewById(R.id.enterName)
        val noField : EditText = subView.findViewById(R.id.enterPhoneNum)
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
        builder.setTitle("Add contact")
        builder.setView(subView)
        builder.create()

        builder.setPositiveButton("ADD CONTACT"){ _, _ ->
            val name = nameField.text.toString()
            val phoneNum = noField.text.toString()
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this@ContactsActivity,"Oops, something went wrong. Make sure there is a name entered.",Toast.LENGTH_LONG).show()
            }
            else{
                val newContact = Contacts(name, phoneNum)
                dataBase.addContacts(newContact)
                finish()
                startActivity(intent)
            }
        }

        builder.setNegativeButton("CANCEL"){ _, _ ->
            Toast.makeText(this@ContactsActivity,"Add contact cancelled.",Toast.LENGTH_LONG).show()
        }

        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        //make sure to close the database
        dataBase.close()
    }
}
