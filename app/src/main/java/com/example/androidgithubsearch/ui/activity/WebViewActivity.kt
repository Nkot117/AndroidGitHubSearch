package com.example.androidgithubsearch.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidgithubsearch.R
import com.example.androidgithubsearch.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private var _binding: ActivityWebViewBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView: WebView = findViewById(R.id.webView)
        val url = intent.getStringExtra("url")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        url?.let {
            webView.loadUrl(it)
        }

        binding.backButton.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }

        binding.forwardButton.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }

        binding.reloadButton.setOnClickListener {
            webView.reload()
        }
    }

    companion object {
        fun createIntent(context: Context, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra("url", url)
            }
        }
    }
}
