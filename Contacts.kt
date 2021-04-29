package com.coinbase.krypty

class Contacts {
    var id = 0
    var name : String
    var phno : String

    internal constructor(name: String, phno: String){
        this.name = name
        this.phno = phno
    }

    internal constructor(id: Int, name: String, phno: String){
        this.id = id
        this.name = name
        this.phno = phno
    }
}