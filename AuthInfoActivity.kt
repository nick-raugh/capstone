//activity to get authorization info from coinbase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_auth_info.*

class AuthInfoActivity : AppCompatActivity() {

    private val onDestroyDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_auth_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        revokeTokenButton.setOnClickListener { revokeToken() }
        refreshTokenButton.setOnClickListener { refreshToken() }
        logoutButton.setOnClickListener { app.logout() }

        showAuthData()
    }

    override fun onDestroy() {
        onDestroyDisposable.dispose()
        super.onDestroy()
    }

    private fun showAuthData() {
        scopesTextView.text = app.accessToken.scope?.replace(" ", "\n")
        expiresTextView.text = app.savedExpireDate?.toString()
    }

    private fun revokeToken() {
        app.revokeTokenRx()
                .withProgress(this::showProgress)
                .addToDisposable(onDestroyDisposable)
                .subscribe()
    }

    private fun refreshToken() {
        app.refreshTokensRx()
                .withProgress(this::showProgress)
                .addToDisposable(onDestroyDisposable)
                .doOnSuccess { showAuthData() }
                .subscribe()
    }

    private fun showProgress(show: Boolean) {
        revokeTokenButton.isEnabled = !show
        refreshTokenButton.isEnabled = !show
        logoutButton.isEnabled = !show

        progressBar.isVisible = show
    }
}
