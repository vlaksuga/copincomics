package com.copincomics.copinapp.pages.pMyPage

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.CoinHistoryAdapterNotExp
import com.copincomics.copinapp.adapters.LibraryHistoryAdapter
import com.copincomics.copinapp.data.Coinhistory
import com.copincomics.copinapp.data.Libraryhistory
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.tools.SpacesItemVerticalDecoration
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.merge_top_title_with_backbtn.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionActivity : BaseActivity() {
    var curposition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        init()
        findViewById<TextView>(R.id.title).text = "Transaction History"
        btn_finish.setOnClickListener { finish() }
        TAG = "BBB transaction history"
        tabs
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.addItemDecoration(SpacesItemVerticalDecoration((10 * DP).toInt()))

        // setting Tab
        tabs.apply {
//            removeAllTabs()
            addTab(newTab().setText("Purchase History"))
            addTab(newTab().setText("Used History"))
        }
        tabs.tabMode = TabLayout.MODE_FIXED

        // 구분선 넣기
        val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.simple_divider)!!)
        rv.addItemDecoration(itemDecorator)


//        tabs.getTabAt(1)!!.select()
        tabs.getTabAt(0)!!.select()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                try {
                    curposition = tab.position
                    update()
                } catch (e: Exception) {
                    exceptionFlow(e.toString())
                }
            }
        }
        )


//        update()
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    fun update() {
        dialog.show()
//        try {
        if (curposition == 0) {
            net.myAPIs.coinhistory().enqueue(object : Callback<Coinhistory> {
                override fun onResponse(call: Call<Coinhistory>, response: Response<Coinhistory>) {
                    val data = response.body()!!.body.list

                    rv.adapter = CoinHistoryAdapterNotExp(data, this@TransactionActivity, DP)
                    if (data.isEmpty()) {
                        transaction_caution.visibility = View.VISIBLE
                        transaction_caution_txt.visibility = View.VISIBLE
                    } else {
                        transaction_caution.visibility = View.INVISIBLE
                        transaction_caution_txt.visibility = View.INVISIBLE
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<Coinhistory>, t: Throwable) {
                    exceptionFlow(t.stackTraceToString())
                    dialog.dismiss()
                }
            })
        } else if (curposition == 1) {
            net.myAPIs.libraryhistory().enqueue(object : Callback<Libraryhistory> {
                override fun onResponse(call: Call<Libraryhistory>, response: Response<Libraryhistory>) {
                    val data = response.body()!!.body.list

                    rv.adapter = LibraryHistoryAdapter(data, this@TransactionActivity, DP)
                    if (data.isEmpty()) {
                        transaction_caution.visibility = View.VISIBLE
                        transaction_caution_txt.visibility = View.VISIBLE
                    } else {
                        transaction_caution.visibility = View.INVISIBLE
                        transaction_caution_txt.visibility = View.INVISIBLE
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<Libraryhistory>, t: Throwable) {
                    exceptionFlow(t.stackTraceToString())
                    dialog.dismiss()
                }
            })


            dialog.dismiss()
        } else {
            dialog.dismiss()
        }
//        } catch (t: Throwable) {
//            exceptionFlow(t.stackTraceToString())
//            dialog.dismiss()
//        }

    }
}