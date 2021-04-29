package com.coinbase.krypty

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton

class NewsActivity : AppCompatActivity() {

    //declare our webView object
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        webView = findViewById(R.id.news_webview)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        // our url for news feed
        webView.loadUrl("https://www.coindesk.com/news")

        //back button
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{backButtonPress()}
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun backButtonPress(){
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            finish()
        }
    }
}
