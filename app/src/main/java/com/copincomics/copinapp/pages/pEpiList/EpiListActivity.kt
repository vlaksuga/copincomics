package com.copincomics.copinapp.pages.pEpiList

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.pAccount.LoginActivity
import com.copincomics.copinapp.pages.pMyPage.PromotionActivity
import com.copincomics.copinapp.pages.pPay.NativePayActivity


class EpiListActivity : BaseActivity() {

    lateinit var webView: WebView
    var curUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epi_list)
        init()

        val intent = intent
        val id = intent.extras!!.getString("id")

        TAG = "BBB Toon"
        webView = findViewById(R.id.webview)


        val cookieManager = CookieManager.getInstance()
//        cookieManager.removeAllCookies()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
        Log.d(TAG, "t : ${getAppPreference("t")}")
        cookieManager.setCookie("copincomics.com", "copinandroid=${getAppPreference("t")}")
        cookieManager.setCookie("acw.adb0t.com", "copinandroid=${getAppPreference("t")}")


        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.settings.useWideViewPort = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.setSupportMultipleWindows(false)
        webView.settings.javaScriptCanOpenWindowsAutomatically = false


        webView.webViewClient = object : WebViewClient() {
            var flag = true

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                flag = true
                Log.d(TAG, "on Page Start")

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(TAG, "on Page Finished")
                val c = getAppPreference("deviceId")
                val t = getAppPreference("t")
                val d = "android"
                val v = "1.1"
                webView.loadUrl("javascript:setAndroid('$t','$c','$d','$v')")
                if (flag) {
                    flag = false
                    webView.loadUrl("javascript:loadPage11()")
                }

            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                super.shouldOverrideUrlLoading(view, request)
                try {
                    Log.d(TAG, request!!.url.toString())
                    view!!.loadUrl(request.url.toString())
                } catch (e: Exception) {
                    exceptionFlow(e.toString())
                }
                return false
            }
        }


        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                super.onReceivedTouchIconUrl(view, url, precomposed)
                Log.d(TAG, "touch url : ${url.toString()} ")
            }

            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                Log.d(TAG, "create : ${webView.url.toString()} ")

                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d(TAG, "onProgressChanged : ${webView.url.toString()} $newProgress")

            }

            override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                Log.d(TAG, "onJsBeforeUnload : ${webView.url.toString()} ${url.toString()} ${result.toString()}")
                return super.onJsBeforeUnload(view, url, message, result)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                Log.d(TAG, "onReceivedTitle : ${webView.url.toString()} ${title.toString()} ")
            }
        }

//
//        webView.webChromeClient = object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                super.onProgressChanged(view, newProgress)
//                if(newProgress == 100){
//                    Log.d(TAG, "")
//
//                }
//            }
//        }
//
//            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
//                val newWebView = WebView(this@EpiListActivity)
//                val webSettings = newWebView.settings
//                webSettings.javaScriptEnabled = true
//
//
//                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
//            }
//
//            fun aonCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
//
//                    val newWebView = WebView(this@EpiListActivity)
//                    val webSettings = newWebView.settings
//                    webSettings.javaScriptEnabled = true
//
//
//                    // Other configuration comes here, such as setting the WebViewClient
//                    val dialog = Dialog(this@EpiListActivity)
//                    dialog.setContentView(newWebView)
//                    dialog.show()
//                    newWebView.webChromeClient = object : WebChromeClient() {
//                        override fun onCloseWindow(window: WebView) {
//                            dialog.dismiss()
//                        }
//                    }
//                    (resultMsg.obj as WebViewTransport).webView = newWebView
//                    resultMsg.sendToTarget()
//
//                return true
//            }
//
//        }


//        webView.webViewClient = object : WebViewClient() {
//
//            override fun onPageFinished(view: WebView?, url: String?) {
////                super.onPageFinished(view, url)
//
//            }
//
//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                super.shouldOverrideUrlLoading(view, request)
//                try {
//                    Log.d(TAG, request!!.url.toString())
//                    view!!.loadUrl(request.url.toString())
//                } catch (e: Exception) {
//                    exceptionFlow(e.toString())
//                }
//                return false
//            }
//        }


//        webView.webChromeClient = object : WebChromeClient() {
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
////                super.onProgressChanged(view, newProgress)
//                if (newProgress == 100) {
//                    Log.d(TAG, "on Page Finished ${view!!.url}, ${newProgress} ")
//                    Log.d(TAG, "finish!!!  ")
//                    val c = getAppPreference("deviceId")
//                    val t = getAppPreference("t")
//                    val d = "android"
//                    val v = "1.1"
//
//                    Log.d(TAG, "t : $t")
//                    Log.d(TAG, "c : $c")
//                    Log.d(TAG, "d : $d")
//                    Log.d(TAG, "v : $v")
//                    Log.d(TAG, "v : ${getAppPreference("appVersion")}")
//                    webView.loadUrl("javascript:setAndroid('$t','$c','$d','$v')")
//                    webView.loadUrl("javascript:loadPage11()")
//                }
//            }
//        }


        webView.addJavascriptInterface(
            object {
                @JavascriptInterface
                fun gogo(string: String) {
//                Toast.makeText(this@EpiListActivity, string, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "gogogo $string")
                }

                @JavascriptInterface
                fun goCoinShop() {
//                Toast.makeText(this@EpiListActivity, "go coin shop", Toast.LENGTH_SHORT).show()
                    goToCoinShop()
                }

                @JavascriptInterface
                fun goPromotion() {
//                Toast.makeText(this@EpiListActivity, "go Promotion", Toast.LENGTH_SHORT).show()
                    goToPromotion()
                }

                @JavascriptInterface
                fun goHome() {
//                Toast.makeText(this@EpiListActivity, "go Home", Toast.LENGTH_SHORT).show()
                    goToHome()
                }

                @JavascriptInterface
                fun goLogin() {
//                Toast.makeText(this@EpiListActivity, "go Home", Toast.LENGTH_SHORT).show()
                    goToLogin()
                }


            }, "AndroidCopin"
        )


//        webView.loadUrl("https://copincomics.com/")
//        webView.loadUrl("https://copincomics.com/?c=toon&k=$id?fdhjskahjfkehsiuahjk")
        webView.loadUrl("https://acw.adb0t.com/?c=toon&k=$id")
        Log.d(TAG, "url :  https://copincomics.com/?c=toon&k=$id")

//        webView.loadUrl("https://copincomics.com/ttt.html")

    }

    override fun onBackPressed() {
        if ("m=view" in curUrl) {
            webView.loadUrl("javascript:goBack()")
            Log.d(TAG, " GO back call ")
        } else {
            Log.d(TAG, "back btn")
            super.onBackPressed()
        }
    }


    fun goToCoinShop() {
//        val intent = Intent(applicationContext, HomeActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("openAcitivity", "coinshop")
//        intent.putExtras(bundle)
        if (getAppPreference("isLogin") == "true") {
            startActivity(Intent(this, NativePayActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
//        finish()

    }

    fun goToPromotion() {
        if (getAppPreference("isLogin") == "true") {
            startActivity(Intent(this, PromotionActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun goToHome() {
        finish()
    }
}