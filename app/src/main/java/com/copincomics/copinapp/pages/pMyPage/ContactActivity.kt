package com.copincomics.copinapp.pages.pMyPage

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.copincomics.copinapp.R
import com.copincomics.copinapp.data.EmptyReturn
import com.copincomics.copinapp.pages.BaseActivity
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_contact)
        init()
        TAG = "Contact"
        findViewById<TextView>(R.id.title).text = "1:1 Support "

        btn_finish.setOnClickListener {
            finish()
        }

        one_on_one_makecontact_btn.setOnClickListener {
            sendQuestion()
        }


    }

    fun sendQuestion() {
        dialog.show()
        try {
            val title = one_on_one_subject.text.toString()
            val txt = one_on_one_text.text.toString()
            Log.d(TAG, title)
            Log.d(TAG, txt)
            net.customerService.askQuestion("tx", title = title, text = txt).enqueue(object : Callback<EmptyReturn> {
                override fun onResponse(call: Call<EmptyReturn>, response: Response<EmptyReturn>) {
                    Toast.makeText(this@ContactActivity, response.body()?.head?.msg.toString(), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<EmptyReturn>, t: Throwable) {
                    exceptionFlow(t.toString())
                    dialog.dismiss()
                }

            })
        } catch (e: Exception) {
            dialog.dismiss()
            exceptionFlow(e.toString())
        }
    }
}