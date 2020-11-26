package com.copincomics.copinapp.pages

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.CheckVersion
import com.copincomics.copinapp.data.RetLogin
import com.copincomics.copinapp.pages.pHome.HomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Entry : BaseActivity() {
    // Entry 플로우 정리

    // (loading img 보여주기)
    // get app version from preference or something
    // network connection check
    // if no connection, pop-up, exit.
    // min version check
    // if cur version < min version, pop-up, exit.

    // put basic device information to preference
    // get firebase instance id (device id)
    // get device resolution
    // _DP -> pref
    // get auto login setting

    // if autologin
    // login process
    // consume item
    // enter home activity

    // if from push message, check toon id, and start with intent payload

    var sVersionChk = false
    var sLoginSuccess = false
    var sUpdateDeviceId = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        TAG = "BBB entry"

        // slash image

        // push message 로 entry가 켜졋을 때 id 받기, 아니면 null
        val intent = intent
        val pkey = intent.extras?.getString("id")
        Log.d(TAG, "start_epipage_webtoonkey $pkey")


        init() // preference 객체 업데이트, dp 업데이트


        // check network
        val isOnline = verifyAvailableNetwork(this)
        if (!isOnline) {
            finish()
            return
        }

        // get device info
        updateDP()
        updateDeviceId()

        // get app version
        var pInfo: PackageInfo? = null
        try {
            pInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (pInfo == null) {
            finish()
        }
        val versionName = pInfo!!.versionName //Version Name
        val versionCode = pInfo.versionCode //Version Code


        Log.e("keshav", " VERSION_NAME -->" + versionName + "versionCode -> " + versionCode)

//        putAppPreference("appVersion_view", BuildConfig.VERSION_NAME) // 1.0.1 version
//        putAppPreference("appVersion", BuildConfig.VERSION_CODE.toString()) // 3 version code

        putAppPreference("appVersion_view", versionName) // 1.0.1 version
        putAppPreference("appVersion", versionCode.toString()) // 3 version code


        // minimum app version check
        try {
            net.accountAPIs.requestCheckVersion().enqueue(object : Callback<CheckVersion> {
                override fun onResponse(call: Call<CheckVersion>, response: Response<CheckVersion>) {
                    val minVersion = response.body()!!.body.ANDROIDMIN.toInt()
                    val curVersion = getAppPreference("appVersion").toInt()

                    if (minVersion <= curVersion) {
                        sVersionChk = true
                        executeNext()
                    } else {
                        // todo : version check pop-up 분기
                    }
                }

                override fun onFailure(call: Call<CheckVersion>, t: Throwable) {
                    exceptionFlow(t.toString())
                }
            })
        } catch (e: java.lang.Exception) {
            exceptionFlow(e.stackTraceToString())
        }

        // preference setting

        putAppPreference("isLogin", "false")
        val lt = getAppPreference("lt")

        // login by token
        if (lt != "") {
            try {
                net.accountAPIs.processLoginByToken(lt = lt).enqueue(object : Callback<RetLogin> {
                    override fun onResponse(call: Call<RetLogin>, response: Response<RetLogin>) {
                        try {
                            val head = response.body()!!.head
                            if (head.status.toString() != "error") {
                                val ret = response.body()!!.body
                                putAppPreference("lt", ret.t2)
                                putAppPreference("t", ret.token)
                                putAppPreference("nick", ret.userinfo.nick)
                                putAppPreference("isLogin", "true")
                                Log.d(TAG, "login token : ${ret.t2}")
                                Log.d(TAG, "acc token : ${ret.token}")
                            } else {
                                Log.d(TAG, "login error ")
                                Log.d(TAG, "${head.status}")
                                Log.d(TAG, "${head.msg}")
                                Log.d(TAG, "${getAppPreference("deviceId")}")
                            }
                            sLoginSuccess = true
                            executeNext()

                        } catch (t: Throwable) {
                            exceptionFlow(t.toString())
                            sLoginSuccess = true
                            executeNext()
                        }

                    }

                    override fun onFailure(call: Call<RetLogin>, t: Throwable) {
                        exceptionFlow(t.toString())
                        sLoginSuccess = true
                        executeNext()
                    }
                })
            } catch (e: java.lang.Exception) {
                exceptionFlow(e.stackTraceToString())
                sLoginSuccess = true
                executeNext()
            }

        } else {
            // 비로그인
            // todo : when 비로그인 세션키
            sLoginSuccess = true
            putAppPreference("lt", "")
            putAppPreference("t", "")
            putAppPreference("nick", "")
            putAppPreference("isLogin", "false")
            executeNext()
        }

//        Handler().postDelayed({
//            val intentId = Intent(applicationContext, HomeActivity::class.java)
//            val bundle = Bundle()
//            bundle.putString("id", pkey)
//            intentId.putExtras(bundle)
//            startActivity(intentId)
//            finish()
//        }, 1000)

    }

    // update firebase device id
    private fun updateDeviceId() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                putAppPreference("deviceId", "fail_to_get_FCM_instance_token")
                sUpdateDeviceId = true
                executeNext()

//                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            putAppPreference("deviceId", token)
            Log.d(TAG, "firebase instance id : $token")
            sUpdateDeviceId = true
            executeNext()

        })

        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)

    }

    // update DP info
    private fun updateDP() {
        val WITDTH = Resources.getSystem().displayMetrics.widthPixels
        val HEIGHT = Resources.getSystem().displayMetrics.heightPixels
        val DPI = Resources.getSystem().displayMetrics.densityDpi
        val WITDTHDP = WITDTH * 160 / DPI

        DP = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            WITDTHDP / 360F,
            resources.displayMetrics
        )        // 360dp 기준 환산

        with(sharedPreferences.edit()) {
            putFloat("DP", DP)
            commit()
        }
        Log.d(TAG, "w : $WITDTH h : $HEIGHT dpi : $DPI")

    }

    // network 확인
    private fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isOnline = networkInfo != null && networkInfo.isConnected
        Log.d(TAG, "isOnline : $isOnline")
        return isOnline
    }

    fun executeNext() {
        if (sVersionChk and sLoginSuccess and sUpdateDeviceId) {

            Log.d(TAG, "lt : " + getAppPreference("lt"))
            Log.d(TAG, "t : " + getAppPreference("t"))
            Log.d(TAG, "nick : " + getAppPreference("nick"))

            startActivity(Intent(application, HomeActivity::class.java))
            finish()
        }
    }

    override fun exceptionFlow(e: String) {
        super.exceptionFlow(e)
        finish()
    }

}