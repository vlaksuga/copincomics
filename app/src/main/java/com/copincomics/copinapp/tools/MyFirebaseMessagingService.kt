package com.copincomics.copinapp.tools

import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    var state_setting_event_alert: String = "true"
    var state_setting_update_alert: String = "true"
    var state_setting_night_alert: String = "true"


    fun show() {
        Log.d(TAG, "============ show =============")
        Log.d(TAG, "event_notification : ${this.state_setting_event_alert}")
        Log.d(TAG, "series_notification : ${this.state_setting_update_alert}")
        Log.d(TAG, "night_notification : ${this.state_setting_night_alert}")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "============ on create =============")

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "deviceId : $p0")
    }


    override fun getStartCommandIntent(p0: Intent?): Intent? {
        if (p0?.extras?.getString("state_setting_event_alert") != null) {
            Log.d(TAG, "============ before =============")
            Log.d(TAG, "event_notification : ${this.state_setting_event_alert}")
            Log.d(TAG, "series_notification : ${this.state_setting_update_alert}")
            Log.d(TAG, "night_notification : ${this.state_setting_night_alert}")


            state_setting_event_alert = p0.extras!!.getString("state_setting_event_alert")!!
            state_setting_update_alert = p0.extras!!.getString("state_setting_update_alert")!!
            state_setting_night_alert = p0.extras!!.getString("state_setting_night_alert")!!
            Log.d(TAG, "============ get =============")
            Log.d(TAG, "event_notification : $state_setting_event_alert")
            Log.d(TAG, "series_notification : $state_setting_update_alert")
            Log.d(TAG, "night_notification : $state_setting_night_alert")

            Log.d(TAG, "============ after =============")
            Log.d(TAG, "event_notification : ${this.state_setting_event_alert}")
            Log.d(TAG, "series_notification : ${this.state_setting_update_alert}")
            Log.d(TAG, "night_notification : ${this.state_setting_night_alert}")


            return null
        } else {
            Log.d(TAG, "no intent")
        }


        return super.getStartCommandIntent(p0)
    }


    override fun onMessageReceived(p0: RemoteMessage) {

        val remoteMessage = p0

        // 알림 설정에 따라 처리 플로우
        val noticeType = remoteMessage.data["type"]
        val pkey = remoteMessage.data["pkey"]
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        val data = Data.Builder()
            .putString("type", noticeType)
            .putString("pkey", pkey)
            .putString("title", title)
            .putString("body", body)
            .build()

        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).setInputData(data).build()

        WorkManager.getInstance()
            .beginWith(work)
            .enqueue()
    }


    companion object {
        val TAG = "BBB FCM : "
    }
}