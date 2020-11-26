package com.copincomics.copinapp.pages.pPay

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import com.copincomics.copinapp.R
import com.copincomics.copinapp.adapters.CellClickListener
import com.copincomics.copinapp.pages.BaseActivity
import com.copincomics.copinapp.tools.SpacesItemVerticalDecoration
import kotlinx.android.synthetic.main.activity_pay.*

class NativePayActivity_ : BaseActivity(), CellClickListener, PurchasesUpdatedListener {

    var productIdsList = arrayListOf<String>("c10", "c30", "c100", "c500", "c1000", "coin_100", "test_coin_100")
    var productDetailList: MutableList<SkuDetails>? = null

    private lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        init()
        TAG = "BBB Payment : "
        findViewById<TextView>(R.id.title).text = "Purchase Coins"
        btn_finish.setOnClickListener { finish() }


        val GooglePlayKey = resources.getString(R.string.googleid)

        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.addItemDecoration(SpacesItemVerticalDecoration((13 * DP).toInt()))


        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()


        pay_refresh.setOnClickListener {
            refresh()
        }

        update()

    }

    fun refresh() {
        val a = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        val b = billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, object : PurchaseHistoryResponseListener {
            override fun onPurchaseHistoryResponse(p0: BillingResult, p1: MutableList<PurchaseHistoryRecord>?) {
//                TODO("Not yet implemented")
                log("refresh")
                log(p0.toString())
                log(p1?.size.toString())
                if (p1 != null) {
                    for (p in p1) {
                        log(p.sku.toString())
                    }
                }
            }
        })

        val c = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

    }

    fun update() {

        dialog.show()

        // 상품 목록 받아오기
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
//                    queryInventoryAsync() // This is used to fetch purchased items from google play store
                    queryInventoryAsync()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                dialog.dismiss()
                showToast("Billing service disconnected...")
            }
        }
        )
    }


    fun queryInventoryAsync() {
        dialog.show()

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(productIdsList)
            .setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build(), object : SkuDetailsResponseListener {
            override fun onSkuDetailsResponse(p0: BillingResult, p1: MutableList<SkuDetails>?) {
                if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                    productDetailList = p1
                    if (productDetailList != null) {
                        val dataSorted = productDetailList!!.sortedBy { it.priceAmountMicros }

//                        rv.adapter = NativeProductAdapter(dataSorted, this, DP, this)
                    }

                    dialog.dismiss()

                } else {
                    dialog.dismiss()
//                    showToast("")
                }
            }
        })
    }

    // 구매 시작
    override fun onCellClickListener(position: Int, value: String) {
        try {
            val dataSorted = productDetailList!!.sortedBy { it.priceAmountMicros }

            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(dataSorted[position])
//                .setObfuscatedProfileId("asdf")
                .setObfuscatedAccountId("accountpkey:productid")
                .build()
            billingClient.launchBillingFlow(this, flowParams)

        } catch (e: Exception) {
        }
    }

    // 구매 완료시 리스너 함수
    override fun onPurchasesUpdated(billingResult: BillingResult, purchaseList: MutableList<Purchase>?) {
        Log.d(TAG, "onPurchasesUpdated")

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            for (purchase in purchaseList!!) {
//                    todo : send token to backend server
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
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            val a = purchaseList
            Log.d(TAG, "${a?.size}")

//            handlePurchase() //  소비성 컨슘

        } else {
            Log.d(TAG, "else ")
            Log.d(TAG, "${purchaseList?.size} ")

            // Handle any other error codes.
        }


    }

    // 소비성 컨슘
    fun handlePurchase(purchase: Purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
            }
        }
    }


    // 비소비성 consume
    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val debugMessage = billingResult.debugMessage
                showToast(debugMessage)
            }
        }
    }
}