  

package com.coinbase.krypty

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.coinbase.CoinbaseResponse
import com.coinbase.resources.prices.Price
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_prices.*
import kotlinx.android.synthetic.main.item_price.view.*

class PricesActivity : AppCompatActivity() {

    private lateinit var currencyId: String
    private val onDestroyDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prices)
        Toast.makeText(getApplicationContext(), "PricesActivity", Toast.LENGTH_LONG).show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        currencyId = intent.extras.getString(CURRENCY_ID, "USD")

        pricesTitle.text = getString(R.string.prices_title_format, currencyId)

        coinbase.pricesResource
                .getSpotPricesRx(currencyId)
                .observeOn(AndroidSchedulers.mainThread())
                .withProgress(this::showProgress)
                .addToDisposable(onDestroyDisposable)
                .subscribe(this@PricesActivity::setData, this@PricesActivity::showError)

    }

    override fun onDestroy() {
        onDestroyDisposable.dispose()
        super.onDestroy()
    }

    private fun setData(it: CoinbaseResponse<MutableList<Price>>) {
        recyclerView.adapter = RecyclerAdapter(this@PricesActivity, it.data)
    }

    private fun showProgress(show: Boolean) {
        recyclerView.isVisible = !show
        pricesTitle.isVisible = !show

        progressBar.isVisible = show
    }

    private class RecyclerAdapter(context: Context,
                                  private val prices: List<Price>) : RecyclerView.Adapter<ViewHolder>() {

        private val layoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
                ViewHolder(layoutInflater.inflate(R.layout.item_price, parent, false))

        override fun getItemCount(): Int = prices.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
                holder.bind(prices[position])

    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val baseCurrency: TextView = itemView.baseCurrency
        val fiatValue: TextView = itemView.fiatValue

        fun bind(price: Price) {
            baseCurrency.text = price.base
            fiatValue.text = "%s %s".format(price.amount, price.currency)
        }
    }

    companion object {
        private const val CURRENCY_ID = "currency_id"

        fun start(context: Context, currencyId: String) {
            context.startActivity(Intent(context, PricesActivity::class.java)
                    .putExtra(CURRENCY_ID, currencyId))
        }
    }

}
