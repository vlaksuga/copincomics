package com.copincomics.copinapp.pages.pMyPage

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.CellClickListener
import com.copincomics.copinapp.adapters.GiftAdapter
import com.copincomics.copinapp.data.EmptyReturn
import com.copincomics.copinapp.data.Gift
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.pages.dialogs.WarnDiag
import com.copincomics.copinapp.tools.SpacesItemVerticalDecoration
import kotlinx.android.synthetic.main.activity_back_rv.*
import kotlinx.android.synthetic.main.activity_my_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromotionActivity : BaseActivity(), CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion)

        init()

        findViewById<TextView>(R.id.title).text = "Gift Box"

        btn_finish.setOnClickListener {
            finish()
        }

        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.addItemDecoration(SpacesItemVerticalDecoration((10 * DP).toInt()))

//        btn_receive_all.setOnClickListener {
//            receiveAll()
//            update()
//        }
//        btn_receive_all.visibility = View.INVISIBLE

        update()
    }

    override fun onResume() {
        super.onResume()
        update()
    }


    fun update() {
        dialog.show()
        net.promotion.promotionList().enqueue(object : Callback<Gift> {
            override fun onResponse(call: Call<Gift>, response: Response<Gift>) {
                rv.adapter = GiftAdapter(response.body()!!.body.list, this@PromotionActivity, DP, 122, 0, this@PromotionActivity)
//                if (response.body()!!.body.list.isEmpty()) {
//                    btn_receive_all.visibility = View.INVISIBLE
//                }
                dialog.dismiss()

            }

            override fun onFailure(call: Call<Gift>, t: Throwable) {
                exceptionFlow(t.toString())
                dialog.dismiss()
            }

        })
    }

    override fun onCellClickListener(position: Int, value: String) {
        dialog.show()
        net.promotion.getTicket(promotionpkey = value).enqueue(object : Callback<EmptyReturn> {
            override fun onResponse(call: Call<EmptyReturn>, response: Response<EmptyReturn>) {
                if (response.body()?.head?.status == "ok") {
                    dialog.dismiss()
//                    Toast.makeText(this@PromotionActivity, "Gift Received", Toast.LENGTH_SHORT).show()
                    WarnDiag().build(this@PromotionActivity, "You have received rental coupons!").show()
                    update()
                }

            }

            override fun onFailure(call: Call<EmptyReturn>, t: Throwable) {
                exceptionFlow(t.toString())
                dialog.dismiss()
            }

        })
    }


    fun receiveAll() {
        dialog.show()
        net.promotion.getAllTicket().enqueue(object : Callback<EmptyReturn> {
            override fun onResponse(call: Call<EmptyReturn>, response: Response<EmptyReturn>) {
                if (response.body()?.head?.status == "ok") {
                    Toast.makeText(this@PromotionActivity, "Gift Received", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()

            }

            override fun onFailure(call: Call<EmptyReturn>, t: Throwable) {
                exceptionFlow(t.toString())
                dialog.dismiss()
            }
        })
    }
}