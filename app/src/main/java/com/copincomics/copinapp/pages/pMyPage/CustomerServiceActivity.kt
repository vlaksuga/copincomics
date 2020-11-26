package com.copincomics.copinapp.pages.pMyPage

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.QAAdapter
import com.copincomics.copinapp.data.MyQuestions
import com.copincomics.copinapp.pages.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_service.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerServiceActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_service)
        init()

        findViewById<TextView>(R.id.title).text = "One-on-one contact "

        btn_finish.setOnClickListener {
            finish()
        }

        btn_onetone.setOnClickListener {
            this.startActivity(Intent(this@CustomerServiceActivity, ContactActivity::class.java))
        }

        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        rv.addItemDecoration(SpacesItemVerticalDecoration((10 * DP).toInt()))
        // 구분선 넣기
        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.simple_divider)!!)
        rv.addItemDecoration(itemDecorator)

    }

    override fun onResume() {
        super.onResume()
        update()
    }


    fun update() {

        dialog.show()
        try {
            net.customerService.listMyQuestion().enqueue(object : Callback<MyQuestions> {
                override fun onResponse(call: Call<MyQuestions>, response: Response<MyQuestions>) {
                    rv.adapter = QAAdapter(response.body()!!.body.list, this@CustomerServiceActivity, DP)
                    dialog.dismiss()

                }

                override fun onFailure(call: Call<MyQuestions>, t: Throwable) {
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