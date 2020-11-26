package com.copincomics.copinapp.pages.pMyPage

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.NoticeAdapter
import com.copincomics.copinapp.data.NoticeList
import com.copincomics.copinapp.pages.BaseActivity
import kotlinx.android.synthetic.main.activity_customer_service.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_rv)
        init()
        findViewById<TextView>(R.id.title).text = "Notice"

        btn_finish.setOnClickListener {
            finish()
        }

        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        rv.addItemDecoration(SpacesItemVerticalDecoration((10 * DP).toInt()))
        // 구분선 넣기
        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.simple_divider)!!)
        rv.addItemDecoration(itemDecorator)


        update()
    }

    override fun onResume() {
        super.onResume()
        update()
    }


    fun update() {
        dialog.show()
        try {
            net.notice.listNotice().enqueue(object : Callback<NoticeList> {
                override fun onResponse(call: Call<NoticeList>, response: Response<NoticeList>) {
                    rv.adapter = NoticeAdapter(response.body()!!.body.list, this@NoticeActivity, DP)
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<NoticeList>, t: Throwable) {
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