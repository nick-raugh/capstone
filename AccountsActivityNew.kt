
package com.coinbase.krypty

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.item_account.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant.now
import java.time.LocalDate
import java.time.LocalDate.now
import java.util.*
import kotlin.collections.ArrayList


class AccountsActivity : AppCompatActivity() {

    private val onDestroyDisposable = CompositeDisposable()
    private val accountsAdapter = AccountsAdapter()



    @RequiresApi(Build.VERSION_CODES.O)
    var date = Date()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        Toast.makeText(getApplicationContext(), "AccountsActivity", Toast.LENGTH_LONG).show()

        /*
        var currencies = ArrayList<Price>() // array of currencies in system
        var fiatValues = ArrayList<String>()    // array of fiat values associated with each currency
        for(i in currencies.indices){
            fiatValues[i]= fillPriceList(currencies[i])
        }
        println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+fiatValues.size+"%%%%%%%%%")
        for(i in fiatValues.indices){
            println("            FIAT VALUE: "+fiatValues[i])
        }
        */

        accountsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        accountsRecyclerView.adapter = accountsAdapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        coinbase.accountResource
                .accountsRx
                .observeOn(AndroidSchedulers.mainThread())
                .addToDisposable(onDestroyDisposable)
                .withProgress(this::showProgress)
                .subscribe({ pagedResponse -> accountsAdapter.setAccounts(pagedResponse.data) })
                { throwable ->
                    showError(throwable)
                }


    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposable.dispose()
    }


    private fun showProgress(show: Boolean) {
        progressBar.isVisible = show
        accountsRecyclerView.isVisible = !show
    }

    internal inner class AccountsAdapter : RecyclerView.Adapter<AccountsActivity.AccountViewHolder>() {
        private var accountsList = ArrayList<Account>()

        fun setAccounts(transactions: List<Account>) {
            accountsList.clear()
            accountsList.addAll(transactions)

            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AccountsActivity.AccountViewHolder {
            val inflater = LayoutInflater.from(viewGroup.context)
            val view = inflater.inflate(R.layout.item_account, viewGroup, false)
            return this@AccountsActivity.AccountViewHolder(view)
        }

        override fun onBindViewHolder(accountViewHolder: AccountsActivity.AccountViewHolder, i: Int) {

            // sort accounts
            var accountsList = accountsList.sortedByDescending { Account -> Account.convertedBalance }

            accountViewHolder.bind(accountsList[i])

        }

        override fun getItemCount(): Int {
            return accountsList.size
        }
    }

    internal inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.name
        val currencyAmount: TextView = itemView.currencyAmount
        val primary: TextView = itemView.primary

        @SuppressLint("SetTextI18n")
        fun bind(account: Account) {
            val accountNameWithIcon = account.nameWithIcon
            name.text = accountNameWithIcon
            name.setTextColor(Color.parseColor(account.currency.color))


            //get USD value of coin
            var amountOfCoin = account.convertedBalance

            //      get coin symbol
            var coinName = account.currency.code

            //      match symbol to price



            /*
            var spotPrice = account.fiatValue
            spotPrice *= amountOfCoin
            var spotDecimal=BigDecimal(spotPrice).setScale(2, RoundingMode.HALF_EVEN)
            var spotDecimalToString = "$".plus(spotDecimal.toString())
            */
            currencyAmount.text= account.balance.formatAmount

            System.out.println("****"+account.name+"$$$$ "+account.currency+"      "+account.convertedBalance+"^^^^^ ")
            primary.visibility = if (account.primary) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                TransactionsActivity.start(account.id, accountNameWithIcon, this@AccountsActivity)
            }
        }

    }

    /*
    fun fillPriceList(price: Price): String {
        var info = price.base+price.amount
        return info
    }
     */

}
