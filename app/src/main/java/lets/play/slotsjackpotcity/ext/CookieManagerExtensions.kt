package lets.play.slotsjackpotcity.ext

import android.webkit.CookieManager
import android.webkit.WebView

fun CookieManager.setAcceptCookie() {
    setAcceptCookie(true)
}

fun CookieManager.setAcceptThirdPartyCookies(wv: WebView) {
    setAcceptThirdPartyCookies(wv, true)
}