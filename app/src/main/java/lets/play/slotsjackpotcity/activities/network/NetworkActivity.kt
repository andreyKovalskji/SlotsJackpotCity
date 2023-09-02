package lets.play.slotsjackpotcity.activities.network


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lets.play.slotsjackpotcity.R
import lets.play.slotsjackpotcity.ext.*
import java.io.File
import java.io.IOException


class NetworkActivity : AppCompatActivity() {
    private lateinit var importantWebView: WebView
    private var mainFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mainCallback: Uri? = null
    private var receivedUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        importantWebView = findViewById(R.id.wv)
        receivedUrl = intent.getStringExtra("url")
        setWhatsNeed()
        receivedUrl?.let { url ->
            importantWebView.loadUrl(url)
        }
    }

    fun setWhatsNeed() {
        importantWebView.setSett("allowContentAccess", true)
        importantWebView.setSett("userAgentString", importantWebView.settings.userAgentString.replace("; wv", ""))
        importantWebView.setSett("allowFileAccess", true)
        importantWebView.setSett("javaScriptCanOpenWindowsAutomatically", true)
        importantWebView.setSett("allowFileAccessFromFileURLs", true)
        importantWebView.setSett("domStorageEnabled", true)
        importantWebView.setSett("javaScriptEnabled", true)
        importantWebView.setSett("databaseEnabled", true)
        importantWebView.setSett("allowUniversalAccessFromFileURLs", true)
        importantWebView.setSett("useWideViewPort", true)
        importantWebView.setSett("loadWithOverviewMode", true)
        importantWebView.setSett("mixedContentMode", 0)
        importantWebView.setSett("cacheMode", WebSettings.LOAD_DEFAULT)
        CookieManager.getInstance().setAcceptCookie()
        CookieManager.getInstance().setAcceptThirdPartyCookies(importantWebView)
        importantWebView.webChromeClient = getWebChromeClient()
        importantWebView.webViewClient = getWebViewClient()
    }

    @Suppress("DEPRECATION")
    val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        runBlocking {
            Log.i("Activity result", "isGranted? $isGranted")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var photoFile: File? = null
            launch(Dispatchers.IO) {
                try {
                    photoFile = File.createTempFile(
                        "temp_file",
                        ".jpg",
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    )
                } catch (ex: IOException) {
                    Log.e("PhotoFile", "I can't be created...", ex)
                }
                mainCallback = Uri.fromFile(photoFile)

                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile)
                )

                startActivityForResult(Intent(Intent.ACTION_CHOOSER).putExtra(Intent.EXTRA_INTENT, Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }).apply {
                    putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePictureIntent))
                }, 1)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        infix fun ValueCallback<Array<Uri>>.onReceiveValue(uri: Uri?) {
            if(uri != null) {
                onReceiveValue(arrayOf(uri))
            }
            else {
                onReceiveValue(uri)
            }
        }
        mainFilePathCallback?.let {c ->
            if (resultCode in -1..-1) {
                val d = data?.dataString
                if (data != null && d != null) {
                    c onReceiveValue Uri.parse(d)
                } else {
                    if (mainCallback != null) {
                        c onReceiveValue mainCallback!!
                    } else {
                        c onReceiveValue null
                    }
                }
            } else {
                c onReceiveValue null
            }
            mainFilePathCallback = null
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        importantWebView.saveState(outState)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (importantWebView.canGoBack()) {
            importantWebView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        importantWebView.restoreState(savedInstanceState)
    }

    private fun getWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                mainFilePathCallback = filePathCallback
                return true
            }
        }
    }

    private fun getWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val u = request.url
                var content = true
                return if (u.toString().contains("/")) {
                    if (u.toString().contains("http")) {
                        content = false
                    } else {
                        startActivity(Intent(Intent.ACTION_VIEW, u))
                    }
                    content
                } else content
            }

            override fun onReceivedLoginRequest(
                view: WebView,
                realm: String,
                account: String?,
                args: String
            ) {
                super.onReceivedLoginRequest(view, realm, account, args)
            }
        }
    }
}