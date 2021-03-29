  

package com.coinbase.krypty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.coinbase.PagedResponse
import com.coinbase.PaginationParams
import com.coinbase.resources.transactions.Transaction
import com.coinbase.resources.transactions.TransactionsResource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_transactions.*
import kotlinx.android.synthetic.main.item_transaction.view.*
import okhttp3.*
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionsActivity : AppCompatActivity() {

    private lateinit var transactionsResource: TransactionsResource
    private lateinit var adapter: TransactionsAdapter
    private lateinit var accountId: String
    private lateinit var accountCode: String
    private var accountBalance: Double = 0.0
    private lateinit var layoutManager: LinearLayoutManager
    private var isLoading = false
    private var latestPagination: PagedResponse.Pagination? = null
    private val onDestroyDisposable = CompositeDisposable()
    private lateinit var data:String
    lateinit var change1: String
    lateinit var change7: String
    lateinit var change30: String

    private val scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (isLoading || !canLoadMore()) {
                return
            }
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItems = recyclerView.adapter.itemCount

            if (totalItems - lastVisibleItemPosition < LOAD_ITEM_THRESHOLD) {
                loadMore()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        swipeRefreshLayout.setOnRefreshListener {
            latestPagination = null
            adapter.clear()
            loadMore()
        }
        accountCode=intent.extras.getString(ACCOUNT_CODE_KEY)
        accountBalance= intent.extras.getDouble(ACCOUNT_BALANCE_KEY)
        if(accountBalance>0.0){
            getData(accountCode)
        }

        accountId = intent.extras.getString(ACCOUNT_ID_KEY)
        accountText.text = intent.extras.getString(ACCOUNT_NAME_KEY)

        transactionsResource = coinbase.transactionsResource

        layoutManager = LinearLayoutManager(this)
        adapter = TransactionsAdapter(LayoutInflater.from(this))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(scrollListener)
        recyclerView.adapter = adapter


        //buttons to change time period
        day1Button.setOnClickListener{
            percentChangeText.text=change1
            //Toast.makeText(getApplicationContext(), "Showing Market Change Over Past 1 Day", Toast.LENGTH_LONG).show()

            if(change1.startsWith("-")){
                percentChangeText.setTextColor(resources.getColor(R.color.colorNeg))
            }
            else{
                percentChangeText.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }

        day7Button.setOnClickListener{
            percentChangeText.text=change7
            //Toast.makeText(getApplicationContext(), "Showing Market Change Over Past Week", Toast.LENGTH_LONG).show()

            if(change7.startsWith("-")){
                percentChangeText.setTextColor(resources.getColor(R.color.colorNeg))
            }
            else{
                percentChangeText.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }

        day30Button.setOnClickListener{
            percentChangeText.text=change30
            //Toast.makeText(getApplicationContext(), "Showing Market Change Over Past Month", Toast.LENGTH_LONG).show()

            if(change30.startsWith("-")){
                percentChangeText.setTextColor(resources.getColor(R.color.colorNeg))
            }
            else{
                percentChangeText.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }

        loadMore()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposable.dispose()
    }

    private fun canLoadMore(): Boolean {
        return latestPagination == null || latestPagination?.nextUri != null
    }

    private fun loadMore() {
        val params = latestPagination?.nextPage()
                ?: PaginationParams().also { it.limit = PAGE_SIZE }

        transactionsResource.listTransactionsRx(accountId, params, Transaction.ExpandField.ALL)
                .observeOn(AndroidSchedulers.mainThread())
                .addToDisposable(onDestroyDisposable)
                .doOnSubscribe({ isLoading = true })
                .subscribe({ pagedResponse ->
                    swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    latestPagination = pagedResponse.pagination
                    adapter.addTransactions(pagedResponse.data)
                }, this@TransactionsActivity::showError)
    }

    internal inner class TransactionsAdapter(private val inflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val transactionList = ArrayList<Transaction>()

        fun addTransactions(transactions: List<Transaction>) {
            transactionList.addAll(transactions)
            notifyDataSetChanged()
        }

        fun clear() {
            transactionList.clear()
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int =
                if (position > transactionList.size - 1) PROGRESS_ITEM else TRANSACTION_ITEM

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
            return when (getItemViewType(i)) {
                TRANSACTION_ITEM -> TransactionViewHolder(inflater.inflate(R.layout.item_transaction, viewGroup, false))
                PROGRESS_ITEM -> object : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_progress, viewGroup, false)) {}
                else -> {
                    throw IllegalStateException("incorrect item view type")
                }
            }
        }

        override fun onBindViewHolder(transactionViewHolder: RecyclerView.ViewHolder, i: Int) {
            (transactionViewHolder as? TransactionViewHolder)?.bind(transactionList[i])
        }

        override fun getItemCount(): Int {
            return transactionList.size + if (canLoadMore()) 1 else 0
        }
    }

    internal class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val date: TextView = itemView.date
        private val type: TextView = itemView.type
        private val title: TextView = itemView.title
        private val subtitle: TextView = itemView.subtitle
        private val amount: TextView = itemView.amount
        private val nativeAmount: TextView = itemView.nativeAmount
        private val status: TextView = itemView.status

        private val dateFormat = SimpleDateFormat("MMM dd, YYYY", Locale.US)

        fun bind(transaction: Transaction) {
            this.date.text = dateFormat.format(transaction.createdAt)

            type.text = transaction.typeIcon
            title.text = transaction.details.title
            subtitle.text = transaction.details.subtitle

            amount.text = transaction.amount.formatAmount
            nativeAmount.text = transaction.nativeAmount.formatAmount

            val amountColorId = if (transaction.amount.amount.startsWith("-")) R.color.colorNeg else R.color.colorPrimary
            val resources = amount.resources
            amount.setTextColor(ResourcesCompat.getColor(resources, amountColorId, null))
            nativeAmount.setTextColor(ResourcesCompat.getColor(resources, amountColorId, null))

            status.text = transaction.statusWithIcon
        }
    }

    companion object {

        const val PAGE_SIZE = 5
        const val LOAD_ITEM_THRESHOLD = 3

        const val TRANSACTION_ITEM = 0
        const val PROGRESS_ITEM = 1

        const val ACCOUNT_ID_KEY = "id_key"
        const val ACCOUNT_NAME_KEY = "name_key"
        const val ACCOUNT_BALANCE_KEY = "balance_key"
        const val ACCOUNT_CODE_KEY="code_key"

        fun start(accountCode: String, accountBalance: Double, accountId: String, accountName: String, activity: Activity) {
            val intent = Intent(activity, TransactionsActivity::class.java)
                    .putExtra(ACCOUNT_CODE_KEY,accountCode)
                    .putExtra(ACCOUNT_BALANCE_KEY, accountBalance)
                    .putExtra(ACCOUNT_ID_KEY, accountId)
                    .putExtra(ACCOUNT_NAME_KEY, accountName)
            activity.startActivity(intent)
        }
    }

    //UNI,NMR,REPV2,YFI,ZEC,DNT,UMA,USDC,FIL,XTZ,LRC,MKR,ETH,OXT,GRT,SNX,REN,NU,XRP,ETC,WBTC,MANA,OMG,XLM,LTC,BTC
    private fun getData(symbol:String){
        val url = "https://api.nomics.com/v1/currencies/ticker?key=926b9427f2df96b042968fc11a861217&ids=$symbol&interval=1d,7d,30d"

        //formatters
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        val df = DecimalFormat("##.##%")


        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        var body =""
        var price:Double = 0.0
        var data:String

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("${e?.message}")
            }

            override fun onResponse(call: Call?, response: Response?) {
                body = response?.body()?.string().toString()
                data=body

                //split up the response
                val array : List<String> =data.split(",")

                for(i in array.indices){
                    println("["+i+"] " + array[i])
                }

                        // Get spot Price
                //array[6] is location of the spot price
                var priceString = array[6].replace("\"","")
                var priceStringDouble=(priceString.substring(6)).toDouble()
                price = priceStringDouble*accountBalance
                var priceText=format.format(price)

                        // Get market change
                var d1changeString = array[24].replace("\"","")
                var d1StringDouble = (d1changeString.substring(17)).toDouble()
                change1=df.format(d1StringDouble)
                if(d1StringDouble>0){
                    change1= "+$change1"
                }

                var d7changeString = array[31].replace("\"","")
                var d7StringDouble = (d7changeString.substring(17)).toDouble()
                change7=df.format(d7StringDouble)
                if(d7StringDouble>0){
                    change7= "+$change7"
                }

                var d30changeString = array[38].replace("\"","")
                var d30StringDouble = (d30changeString.substring(17)).toDouble()
                change30=df.format(d30StringDouble)
                if(d30StringDouble>0){
                    change30= "+$change30"
                }

                //set text
                percentChangeText.text=change1
                if(change1.startsWith("-")){
                    percentChangeText.setTextColor(resources.getColor(R.color.colorNeg))
                }
                else{
                    percentChangeText.setTextColor(resources.getColor(R.color.colorPrimary))
                }


                walletBalanceText.text=priceText
                println("$$$$priceText 1d: $change1 7d: $change7 30d: $change30")
            }
        })



    }
    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
}

//region Transaction binding extensions

private val Transaction.typeIcon: String
    get() {
        return when (type) {
            Transaction.TYPE_SEND -> if (amount.amount.startsWith("-")) "➖️" else "➕️"
            Transaction.TYPE_REQUEST -> "❓"
            Transaction.TYPE_TRANSFER -> "🔁"
            Transaction.TYPE_BUY, Transaction.TYPE_SELL -> "💰"
            Transaction.TYPE_FIAT_DEPOSIT, Transaction.TYPE_FIAT_WITHDRAWAL -> "💵"
            Transaction.TYPE_EXCHANGE_DEPOSIT, Transaction.TYPE_EXCHANGE_WITHDRAWAL -> "💱"
            Transaction.TYPE_VAULT_WITHDRAWAL -> "🏦"
            else -> "💸"
        }
    }

private val Transaction.statusWithIcon: String
    get() {

        val icon = when (status) {
            Transaction.STATUS_PENDING -> "⚫"
            Transaction.STATUS_COMPLETED -> "✅"
            Transaction.STATUS_FAILED -> "⚠️"
            Transaction.STATUS_EXPIRED -> "⌛️"
            Transaction.STATUS_CANCELED -> "❌"
            Transaction.STATUS_WAITING_FOR_SIGNATURE -> "🕰"
            Transaction.STATUS_WAITING_FOR_CLEARING -> "🕰"
            else -> "⚫"
        }

        return String.format(Locale.US, "%s %s", icon, status.replace("_", " "))
    }

//endregion