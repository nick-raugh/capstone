
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.coinbase.Coinbase
import com.coinbase.CoinbaseBuilder
import com.coinbase.errors.CoinbaseOAuthException
import com.coinbase.resources.auth.AccessToken
import com.coinbase.resources.auth.RevokeTokenResponse
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*

class MainApplication : Application() {

    lateinit var coinbase: Coinbase
        private set

    val savedExpireDate: Date? by lazy {
        val time = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getLong(KEY_EXPIRE_DATE, 0)
        if (time != 0L) {
            Date(time)
        } else {
            null
        }
    }

    var accessToken: AccessToken
        get() {
            val json = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_ACCESS_TOKEN, null)
            return if (json == null) AccessToken() else gson.fromJson(json, AccessToken::class.java)
        }
        set(value) {
            val json = gson.toJson(value)

            val expiresIn = Calendar.getInstance()
            expiresIn.add(Calendar.SECOND, value.expiresIn)

            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_ACCESS_TOKEN, json)
                    .putLong(KEY_EXPIRE_DATE, expiresIn.time.time)
                    .apply()
        }

    private val gson = Gson()

    private val tokenUpdateListener = object : Coinbase.TokenListener {
        override fun onNewTokensAvailable(accessToken: AccessToken) {
            this@MainApplication.accessToken = accessToken
        }

        override fun onRefreshFailed(cause: CoinbaseOAuthException) {
            Log.e("Krypty", "Access token autorefresh failed, logging out", cause)
            logout()
        }

        override fun onTokenRevoked() {
            logout()
        }
    }

    override fun onCreate() {
        super.onCreate()

        coinbase = buildCoinbase()
    }

    private fun buildCoinbase(): Coinbase {
        val builder = CoinbaseBuilder.withTokenAutoRefresh(this,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                accessToken.accessToken,
                accessToken.refreshToken,
                tokenUpdateListener
        )
        builder.withLoggingLevel(HttpLoggingInterceptor.Level.BODY)
        return builder.withLoggingLevel(HttpLoggingInterceptor.Level.BODY).build()
    }

    fun revokeTokenRx(): Single<RevokeTokenResponse> {
        val savedAccessToken = accessToken

        return coinbase.authResource
                .revokeTokenRx(savedAccessToken.accessToken)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    clearToken()
                }
    }

    fun refreshTokensRx(): Single<AccessToken> {
        return coinbase.authResource
                .refreshTokensRx(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, accessToken.refreshToken)
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun clearToken() {
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
    }

    fun logout() {
        clearToken()
        coinbase.logout()

        startActivity(Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    companion object {

        const val PREF_NAME = "Krypty"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_EXPIRE_DATE = "expireDate"
    }

}
