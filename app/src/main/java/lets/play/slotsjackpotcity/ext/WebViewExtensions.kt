package lets.play.slotsjackpotcity.ext

import android.webkit.WebSettings
import android.webkit.WebView

fun WebView.setSett(property: String, value: Boolean) {
    when(property) {
        "allowContentAccess" -> settings.allowContentAccess = value
        "allowFileAccess" -> settings.allowFileAccess = value
        "javaScriptCanOpenWindowsAutomatically" -> settings.javaScriptCanOpenWindowsAutomatically = value
        "allowFileAccessFromFileURLs" -> settings.allowFileAccessFromFileURLs = value
        "domStorageEnabled" -> settings.domStorageEnabled = value
        "javaScriptEnabled" -> settings.javaScriptEnabled = value
        "databaseEnabled" -> settings.databaseEnabled = value
        "allowUniversalAccessFromFileURLs" -> settings.allowUniversalAccessFromFileURLs = value
        "useWideViewPort" -> settings.useWideViewPort = value
        "loadWithOverviewMode" -> settings.loadWithOverviewMode = value
    }
}

fun WebView.setSett(property: String, value: Int) {
    when(property) {
        "mixedContentMode" -> settings.mixedContentMode = value
        "cacheMode" -> settings.cacheMode = value
    }
}

fun WebView.setSett(property: String, value: String) {
    when(property) {
        "userAgentString" -> settings.userAgentString = value
    }
}