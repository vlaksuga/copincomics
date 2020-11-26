package com.copincomics.copinapp.tools

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.copincomics.copinapp.R
import com.copincomics.copinapp.pages.Entry
import java.util.*

class MyWorker(val appContext: Context, val workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    private val TAG = "BBB MyWorker"


    override fun doWork(): Result {
        Log.d(TAG, "Performing long running task in scheduled job")

        val s = appContext.getSharedPreferences("com.copincomics.preference", Context.MODE_PRIVATE)
        val setting_event_alert = s.getString("setting_event_alert", "")
        val setting_update_alert = s.getString("setting_update_alert", "")
        val setting_night_alert = s.getString("setting_night_alert", "")
        val isLogin = s.getString("isLogin", "")

        Log.d(TAG, "setting_event_alert :: $setting_event_alert")
        Log.d(TAG, "setting_update_alert :: $setting_update_alert")
        Log.d(TAG, "setting_night_alert :: $setting_night_alert")

        inputData.getString("")

        val noticeType = inputData.getString("type")
        val pkey = inputData.getString("pkey")
        val title = inputData.getString("title")
        val body = inputData.getString("body")


        val rightNow = Calendar.getInstance()
        val h = rightNow.get(Calendar.HOUR_OF_DAY)
        Log.d(MyFirebaseMessagingService.TAG, "hour of day :  $h")

        if ("isLogin" == "false") {
            Log.d("BBB notification ", "Not login user")
            return Result.success()
        }

        if ((noticeType == "event") and (setting_event_alert == "false")) {
            Log.d("BBB notification ", "event notifi false")
            return Result.success()

        } else if ((noticeType == "series") and (setting_update_alert == "false")) {
            return Result.success()

        } else if (((h < 8) or (h > 22)) and (setting_night_alert == "false")) {
            return Result.success()

        } else {
            Log.d("BBB notification ", "hour of day :  $h")

            val intent = Intent(appContext, Entry::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("id", pkey)

            val pendingIntent = PendingIntent.getActivity(
                appContext, 0, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val channelId = appContext.getString(R.string.default_notification_channel_id)
            val notificationBuilder = NotificationCompat.Builder(appContext, channelId)
                .setSmallIcon(R.mipmap.icon_circle)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)


            val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // android Oreo 알림 채널이 필요합니다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelId,
//                "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /*알림ID */, notificationBuilder.build())

        }


        return ListenableWorker.Result.success()
    }


    companion object
}