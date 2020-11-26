package com.copincomics.copinapp.pages.pPDpick

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.pAccount.LoginActivity
import com.copincomics.copinapp.pages.pEpiList.EpiListActivity
import com.copincomics.copinapp.pages.pMyPage.PromotionActivity
import com.copincomics.copinapp.pages.pPay.NativePayActivity
import kotlinx.android.synthetic.main.activity_pd_pick.*

class PdPickActivity : BaseActivity() {

    companion object {
        const val ROOT_URL = "https://acw.adb0t.com/?c=etc&m=pd"

    }
    var curUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pd_pick)
        init()

        val cid = intent.extras?.getString("id")


        webview.settings.domStorageEnabled = true
        webview.settings.javaScriptEnabled = true
        webview.settings.setSupportZoom(false)
        webview.settings.useWideViewPort = true
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.setSupportMultipleWindows(false)
        webview.settings.javaScriptCanOpenWindowsAutomatically = false
        webview.webViewClient = object : WebViewClient() {
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
                webview.loadUrl("javascript:setAndroid('$t','$c','$d','$v')")
                if (flag) {
                    flag = false
                    webview.loadUrl("javascript:loadPage11()")
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

        webview.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                super.onReceivedTouchIconUrl(view, url, precomposed)
                Log.d(TAG, "touch url : ${url.toString()} ")
            }

            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                Log.d(TAG, "create : ${webview.url.toString()} ")

                return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d(TAG, "onProgressChanged : ${webview.url.toString()} $newProgress")

            }

            override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                Log.d(TAG, "onJsBeforeUnload : ${webview.url.toString()} ${url.toString()} ${result.toString()}")
                return super.onJsBeforeUnload(view, url, message, result)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                Log.d(TAG, "onReceivedTitle : ${webview.url.toString()} ${title.toString()} ")
            }
        }

        webview.addJavascriptInterface(
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
        if(cid.isNullOrBlank()) {
            webview.loadUrl(ROOT_URL)
        } else {
            webview.loadUrl(ROOT_URL + "#cid" + cid)
        }



    }

    fun callEpiList(id: String) {
        val intent = Intent(this, EpiListActivity::class.java)
        val bundle = Bundle()
        bundle.putString("id", id)
        intent.putExtras(bundle)
        this.startActivity(intent)
        finish()
    }


    override fun onBackPressed() {
        if ("m=view" in curUrl) {
            webview.loadUrl("javascript:goBack()")
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
