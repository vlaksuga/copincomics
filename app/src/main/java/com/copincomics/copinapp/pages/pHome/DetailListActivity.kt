package com.copincomics.copinapp.pages.pHome

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.ToonListAdapter
import com.copincomics.copinapp.data.PageMain
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.tools.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_back_rv.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_back_rv)
        init()
        TAG = "BBB Detail List"
        findViewById<TextView>(R.id.title).text = "New Updates"

        btn_finish.setOnClickListener { finish() }

        rv.layoutManager = GridLayoutManager(this, 2)
        rv.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                (18 * DP).toInt(),
                (15 * DP).toInt(),
                (16 * DP).toInt()
            )
        )

        update()

    }

    fun update() {
        dialog.show()
        try {
            net.serviceAPIs.pageMain()
                .enqueue(object : Callback<PageMain> {
                    override fun onResponse(call: Call<PageMain>, response: Response<PageMain>) {
                        val ret = response.body()!!.body
                        rv.adapter = ToonListAdapter(R.layout.section_a, ret.listRecent, this@DetailListActivity, DP, 154, 154, "all", "1x1")
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<PageMain>, t: Throwable) {
                        exceptionFlow(t.toString())
                    }
                }
                )
        } catch (e: Exception) {
            dialog.dismiss()
            exceptionFlow(e.toString())
        }
    }
}