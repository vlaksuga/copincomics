package com.copincomics.copinapp.pages.pPay

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.CellClickListener
import com.copincomics.copinapp.data.GetMe
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.tools.SpacesItemVerticalDecoration
import kotlinx.android.synthetic.main.activity_pay.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NativePayActivity : BaseActivity(), CellClickListener, PurchasesUpdatedListener {

    val agent = InappBilling()
    lateinit var rv: RecyclerView
    lateinit var pay_coin: TextView
    lateinit var apkey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        init()
        TAG = "BBB Payment : "
        findViewById<TextView>(R.id.title).text = "Purchase Coins"
        btn_finish.setOnClickListener { finish() }


        val GooglePlayKey = resources.getString(R.string.googleid)

        pay_coin = findViewById(R.id.pay_coin)

        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.addItemDecoration(SpacesItemVerticalDecoration((13 * DP).toInt()))


        agent.init(this)

        updateUI()

    }


    private fun updateUI() {
        dialog.show()

        // 리스트 확인후 컨슘
        agent.checkProductUnconsume()

        // 코인 업데이트
        net.accountAPIs.getMe().enqueue(object : Callback<GetMe> {
            override fun onResponse(call: Call<GetMe>, response: Response<GetMe>) {
                val ret = response.body()!!.body
                // 코인 업데이트
                pay_coin.text = ret.coin
                log("coin ${ret.coin}")
                apkey = ret.apkey

                // 상품리스트 업데이트
                agent.getInventory()
            }

            override fun onFailure(call: Call<GetMe>, t: Throwable) {
                exceptionFlow(t.toString())
            }
        })


    }

    override fun onCellClickListener(position: Int, value: String) {
        try {
            val dataSorted = agent.productDetailList!!.sortedBy { it.priceAmountMicros }
            agent.launchBillingFlow(dataSorted[position])

        } catch (e: Exception) {
            // 예외처리
        }
    }

    // 구매 완료(리스너)
    override fun onPurchasesUpdated(billingResult: BillingResult, purchaseList: MutableList<Purchase>?) {
        Log.d(TAG, "onPurchasesUpdated")

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            if (purchaseList != null) {
                for (purchase in purchaseList) {
                    // todo : send token to backend server
                    agent.sendBackend(purchase.purchaseToken, purchase.sku)
                    purchase.accountIdentifiers
                    purchase.orderId
                    purchase.signature
                    Log.d(TAG, "!!!! ${purchase.orderId}")
                    Log.d(TAG, "!!!! ${purchase.packageName}")
                    Log.d(TAG, "!!!! ${purchase.purchaseToken}")
                    Log.d(TAG, "!!!! ${purchase.signature}")
                    //                handlePurchase(purchase) //  소비성 컨슘
                    //                acknowledgePurchase(purchase.purchaseToken) 비소비성 컨슘
                    showToast("Success")

                }
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.

        } else {
            Log.d(TAG, "else ")
            Log.d(TAG, "${purchaseList?.size} ")

            // Handle any other error codes.
        }

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }


}