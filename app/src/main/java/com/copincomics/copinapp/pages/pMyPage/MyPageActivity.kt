package com.copincomics.copinapp.pages.pMyPage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.GetMe
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.pPay.NativePayActivity
import com.copincomics.copinapp.pages.pSetting.SettingActivity
import com.copincomics.copinapp.tools.GlideApp
import kotlinx.android.synthetic.main.activity_my_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageActivity : BaseActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 왼쪽에서 나오는 애니메이션
        overridePendingTransition(
            R.anim.slide_left_in,
            R.anim.slide_left_out
        )
        setContentView(R.layout.activity_my_page)
        findViewById<TextView>(R.id.title).text = "My Info"

        init()

        btn_finish.setOnClickListener {
            finish()
        }

        my_setting_btn.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        my_bill.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }

        my_library.setOnClickListener {
            val intent = Intent()
            intent.putExtra("result", "goLibrary")
            setResult(RESULT_OK, intent)
            finish()
        }


        my_gift.setOnClickListener {
            val intent = Intent(this, PromotionActivity::class.java)
            startActivity(intent)
        }

        my_purchase.setOnClickListener {
            val intent = Intent(this, NativePayActivity::class.java)
//            val intent = Intent(this, PayActivity::class.java)
            startActivity(intent)
        }

        my_service.setOnClickListener {
            val intent = Intent(this, CustomerServiceActivity::class.java)
            startActivity(intent)
        }


        my_notice.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
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
        try {
            net.accountAPIs.getMe().enqueue(object : Callback<GetMe> {
                override fun onResponse(call: Call<GetMe>, response: Response<GetMe>) {
                    val ret = response.body()!!.body

                    my_user_name.text = ret.nick
                    my_coin.text = ret.coin

                    // profile img
                    val media = ret.profileimg
                    if (media != null) {
                        GlideApp.with(this@MyPageActivity)
                            .load(media)
                            .into(my_user_thumb)
                    } else {
                        // 실패시 처리
                        my_user_thumb.setImageResource(R.drawable.ic_blank)
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<GetMe>, t: Throwable) {
                    dialog.dismiss()
                    exceptionFlow(t.toString())
                }
            })
        } catch (e: Exception) {
            dialog.dismiss()
            exceptionFlow(e.toString())
        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
    }
}