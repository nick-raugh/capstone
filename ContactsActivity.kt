package com.coinbase.krypty

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.coinbase.CoinbaseResponse
import com.coinbase.network.ApiCall
import com.coinbase.network.ApiCallAdapterFactory
import com.coinbase.resources.accounts.Account
import com.coinbase.resources.prices.Price
import com.coinbase.resources.prices.PricesApi
import com.coinbase.resources.prices.PricesApiRx
import com.coinbase.resources.prices.PricesResource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_accounts.*
import kotlinx.android.synthetic.main.activity_currencies.*
import kotlinx.android.synthetic.main.item_account.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant.now
import java.time.LocalDate
import java.time.LocalDate.now
import java.util.*
import kotlin.collections.ArrayList



class ContactsActivity : AppCompatActivity() {

    private val onDestroyDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //recyclerView.layoutManager = LinearLayoutManager(this)


    }
    override fun onDestroy() {
        onDestroyDisposable.dispose()
        super.onDestroy()
    }

}