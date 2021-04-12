package com.coinbase.krypty

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    var tvName: TextView = itemView.findViewById(R.id.contactName)
    var tvPhoneNum: TextView = itemView.findViewById(R.id.phoneNum)
    var deleteContact: ImageView = itemView.findViewById(R.id.deleteContact)
    var editContact: ImageView = itemView.findViewById(R.id.editContact)
    var copyContact: ImageView = itemView.findViewById(R.id.copyContact)

}