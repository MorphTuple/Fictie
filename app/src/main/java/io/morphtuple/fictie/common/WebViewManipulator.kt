package io.morphtuple.fictie.common

import android.webkit.WebView
import android.webkit.WebViewClient

fun WebView.changeBackground(color: String) = manipulateBodyStyle("background", color)
fun WebView.changeTextColor(color: String) = manipulateBodyStyle("color", color)

fun WebView.manipulateBodyStyle(key: String, value: String) {
    this.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            view?.loadUrl("javascript:document.body.style.setProperty(\"${key}\", \"${value}\")")
        }
    }
}
