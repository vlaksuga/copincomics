package com.copincomics.copinapp.pages.pAccount

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.EmptyReturn
import com.copincomics.copinapp.data.GetMe
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.tools.GlideApp
import kotlinx.android.synthetic.main.activity_ch_profile.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ch_profile)
        init()
        findViewById<TextView>(R.id.title).text = "Edit Profile"
        btn_finish.setOnClickListener { finish() }

        my_user_thumb
        edit_profile_nick
        edit_profile_email
        edit_save_btn



        update()

        edit_save_btn.setOnClickListener {
//            val new_nick = edit_profile_nick.text.toString()
            updateNick()
        }


    }

    fun update() {
        // 계정 정보 업데이트. nick, coin,
        dialog.show()
        net.accountAPIs.getMe().enqueue(object : Callback<GetMe> {
            override fun onResponse(call: Call<GetMe>, response: Response<GetMe>) {
                val ret = response.body()!!.body

                edit_profile_nick.setText(ret.nick)
                edit_profile_email.text = ret.email

                // profile img
                val media = ret.profileimg
                putAppPreference("profileimage", media)
                putAppPreference("email", ret.email)
                if (media !== null) {
                    GlideApp.with(this@ChangeProfileActivity)
                        .load(media)
                        .into(my_user_thumb)

                } else {
                    // 실패시 처리
                    my_user_thumb.visibility = View.INVISIBLE
                }


                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetMe>, t: Throwable) {
                dialog.dismiss()
                exceptionFlow(t.toString())
            }
        })

    }


    fun updateNick() {
        val newNick = edit_profile_nick.text.toString()
        try {
            net.myAPIs.changeProfile(profileimage = getAppPreference("profileimage"), nick = newNick).enqueue(object : Callback<EmptyReturn> {
                override fun onResponse(call: Call<EmptyReturn>, response: Response<EmptyReturn>) {
                    if (response.body()!!.head.status == "ok") {
                        showToast("Nick change successful!")
                        finish()
                    }
                }

                override fun onFailure(call: Call<EmptyReturn>, t: Throwable) {
                    exceptionFlow(t.stackTraceToString())
                    showToast("Network Error! Try Again Please ")
                }
            })
        } catch (e: Exception) {
            exceptionFlow(e.stackTraceToString())
//            showToast("Network Error! Try Again Please ")
        }
    }

}