package com.coinbase.krypty

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView.VERTICAL
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.coinbase.krypty.db.UserEntity
import kotlinx.android.synthetic.main.activity_contacts.*


class ContactsActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: ContactsActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@ContactsActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(ContactsActivityViewModel::class.java)
        viewModel.getAllUsersObservers().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        saveButton.setOnClickListener {
            val name  = nameInput.text.toString()
            val email  = emailInput.text.toString()
            val phone = phoneInput.text.toString()
            if(saveButton.text.equals("Save")) {
                val user = UserEntity(0, name, email, phone)
                viewModel.insertUserInfo(user)
            } else {
                val user = UserEntity(nameInput.getTag(nameInput.id).toString().toInt(), name, email, phone)
                viewModel.updateUserInfo(user)
                saveButton.setText("Save")
            }
            nameInput.setText("")
            emailInput.setText("")
        }
    }



    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        nameInput.setText(user.name)
        emailInput.setText(user.email)
        phoneInput.setText(user.phone)
        nameInput.setTag(nameInput.id, user.id)
        saveButton.setText("Update")
    }
}
