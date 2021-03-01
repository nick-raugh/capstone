import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.coinbase.Coinbase
import com.coinbase.CoinbaseResponse
import com.coinbase.errors.CoinbaseException
import com.coinbase.network.Callback
import com.coinbase.resources.users.AuthUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

private const val DEFAULT_CURRENCY = "USD"

class MainActivity : AppCompatActivity() {

    private lateinit var coinbase: Coinbase
    private lateinit var mainApplication: MainApplication

    private var user: AuthUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainApplication = (application as MainApplication)
        coinbase = mainApplication.coinbase

        accountsButton.setOnClickListener { startActivity(Intent(this, AccountsActivity::class.java)) }
        spotPricesButton.setOnClickListener {
            PricesActivity.start(this, user?.nativeCurrency ?: DEFAULT_CURRENCY)
        }
        currenciesButton.setOnClickListener { startActivity(Intent(this, CurrenciesActivity::class.java)) }
        paymentMethodsButton.setOnClickListener { startActivity(Intent(this, PaymentMethodsActivity::class.java)) }

        authorizationInfoButton.setOnClickListener { startActivity(Intent(this, AuthInfoActivity::class.java)) }

        getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.logout -> {
                mainApplication.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getUser() {
        coinbase.userResource.currentUser.enqueue(this, object : Callback<CoinbaseResponse<AuthUser>> {
            override fun onSuccess(result: CoinbaseResponse<AuthUser>?) {
                val user = result?.data ?: return
                this@MainActivity.user = user

                Picasso.get().load(user.avatarUrl).into(userAvatar)
                userInfo.text = "${user.username ?: ""}\n${user.name}\n"
            }

            override fun onFailure(t: Throwable?) {
                if (t is CoinbaseException) {
                    showErrorMessage(t.serverError.message)
                } else {
                    showErrorMessage(null)
                }
            }

        })
    }

    private fun showErrorMessage(message: String?) {
        Toast.makeText(this, message
                ?: getString(R.string.load_user_error), Toast.LENGTH_SHORT).show()
    }

}

