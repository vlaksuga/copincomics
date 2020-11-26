package com.copincomics.copinapp.pages.pSetting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.GetMe
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.Entry
import com.copincomics.copinapp.pages.pAccount.ChangePWActivity
import com.copincomics.copinapp.pages.pAccount.ChangeProfileActivity
import com.copincomics.copinapp.tools.GlideApp
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.merge_bottom_footer.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        init()
        findViewById<TextView>(R.id.title).text = "Setting"


        btn_finish.setOnClickListener {
            finish()
        }

        chpw_layout.visibility = View.VISIBLE
        chpw_layout.setOnClickListener {
            val intent = Intent(this, ChangePWActivity::class.java)
            startActivity(intent)
        }


        btn_setting_edit_profile.setOnClickListener {
            val intent = Intent(this, ChangeProfileActivity::class.java)
            startActivity(intent)
        }


        //  alert setting
        setting_event_alert.setOnClickListener {
            when (getAppPreference("setting_event_alert")) {
                "" -> {
                    putAppPreference("setting_event_alert", "false")
                }
                "true" -> {
                    putAppPreference("setting_event_alert", "false")
                }
                "false" -> {
                    putAppPreference("setting_event_alert", "true")
                }
            }
            update()
        }

        setting_update_alert.setOnClickListener {
            when (getAppPreference("setting_update_alert")) {
                "" -> {
                    putAppPreference("setting_update_alert", "false")
                }
                "true" -> {
                    putAppPreference("setting_update_alert", "false")
                }
                "false" -> {
                    putAppPreference("setting_update_alert", "true")
                }
            }
            update()
        }

        setting_night_alert.setOnClickListener {
            when (getAppPreference("setting_night_alert")) {
                "" -> {
                    putAppPreference("setting_night_alert", "false")
                }
                "true" -> {
                    putAppPreference("setting_night_alert", "false")
                }
                "false" -> {
                    putAppPreference("setting_night_alert", "true")
                }
            }
            update()
        }


        // logout
        setting_logout.setOnClickListener {
            dialog.show()
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                putAppPreference("isLogin", "false")
                putAppPreference("lt", "")
                putAppPreference("t", "")
                putAppPreference("nick", "")
                dialog.dismiss()

                val intent = Intent(applicationContext, Entry::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        footer_privacy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://copincomics.com/m_privacy.html"))
            startActivity(intent)
        }

        footer_terms.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://copincomics.com/m_terms.html"))
            startActivity(intent)
        }

        footer_copyright.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://copincomics.com/m_copyright.html"))
            startActivity(intent)
        }

        ic_ins.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/copin.webtoon/"))
            startActivity(intent)
        }
        ic_face.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/copincomics"))
            startActivity(intent)
        }
        ic_tw.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/copin_webtoon"))
            startActivity(intent)
        }


        update()
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    fun update() {
        // 계정 정보 업데이트. nick, coin,
        dialog.show()
        net.accountAPIs.getMe().enqueue(object : Callback<GetMe> {
            override fun onResponse(call: Call<GetMe>, response: Response<GetMe>) {
                val ret = response.body()!!.body

                my_user_name.text = ret.nick

                // profile img
                val media = ret.profileimg
                if (media !== null) {
                    GlideApp.with(this@SettingActivity)
                        .load(media)
                        .into(my_user_thumb)
                } else {
                    // 실패시 처리
                    my_user_thumb.visibility = View.INVISIBLE
                }


                if (ret.type != "password") {
                    chpw_layout.visibility = View.GONE
                } else {
                    chpw_layout.visibility = View.VISIBLE
                }

                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetMe>, t: Throwable) {
                dialog.dismiss()
                exceptionFlow(t.toString())
            }
        })


        val state_setting_event_alert = getAppPreference("setting_event_alert")
        val state_setting_update_alert = getAppPreference("setting_update_alert")
        val state_setting_night_alert = getAppPreference("setting_night_alert")

        Log.d(TAG, "event_notification : $state_setting_event_alert")
        Log.d(TAG, "series_notification : $state_setting_update_alert")
        Log.d(TAG, "night_notification : $state_setting_night_alert")


//        val intentCommand = Intent(this, MyFirebaseMessagingService::class.java)
//        val bundle = Bundle()
//        bundle.putString("state_setting_event_alert", state_setting_event_alert)
//        bundle.putString("state_setting_night_alert", state_setting_night_alert)
//        bundle.putString("state_setting_update_alert", state_setting_update_alert)
//        intentCommand.putExtras(bundle)
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            applicationContext.startForegroundService(intentCommand)
//        }

//        val a = getSystemService(MyFirebaseMessagingService::class.java)
//        a.state_setting_event_alert = state_setting_event_alert
//        a.state_setting_night_alert = state_setting_night_alert
//        a.state_setting_update_alert = state_setting_update_alert
//        a.show()

        // init setting
        if ((state_setting_event_alert == "") or (state_setting_event_alert == "true")) {
            setting_event_alert.setImageResource(R.mipmap.switch_on)
        } else {
            setting_event_alert.setImageResource(R.mipmap.switch_off)
        }

        if ((state_setting_update_alert == "") or (state_setting_update_alert == "true")) {
            setting_update_alert.setImageResource(R.mipmap.switch_on)
        } else {
            setting_update_alert.setImageResource(R.mipmap.switch_off)
        }

        if ((state_setting_night_alert == "") or (state_setting_night_alert == "true")) {
            setting_night_alert.setImageResource(R.mipmap.switch_on)
        } else {
            setting_night_alert.setImageResource(R.mipmap.switch_off)
        }

    }


}