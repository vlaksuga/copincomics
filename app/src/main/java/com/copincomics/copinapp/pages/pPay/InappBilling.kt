package com.copincomics.copinapp.pages.pPay

import android.app.AlertDialog
import android.util.Log
import com.android.billingclient.api.*
import com.copincomics.copinapp.adapters.NativeProductAdapter
import com.copincomics.copinapp.data.Confirm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InappBilling {

    var productIdsList = arrayListOf<String>("c10", "c30", "c100", "c500", "c1000")
    var productDetailList: MutableList<SkuDetails>? = null
    private lateinit var billingClient: BillingClient
    private lateinit var context: NativePayActivity
    private lateinit var dialog: AlertDialog

    fun init(ctx: NativePayActivity) {
        context = ctx
        dialog = context.dialog

        billingClient = BillingClient.newBuilder(context)
            .enablePendingPurchases()
            .setListener(context)
            .build()
    }

    // billing client 통신 체크
    // 상품 목록 받아오기
    fun getInventory() {
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
                context.showToast("Billing service disconnected...")
            }
        }
        )
    }

    // 상품 목록 불러오기
    private fun queryInventoryAsync() {
        val rv = context.rv
        val dialog = context.dialog

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

                        rv.adapter = NativeProductAdapter(dataSorted, context, context.DP, context)
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
    fun launchBillingFlow(item: SkuDetails) {
        val productid = item.sku
        val apkey = context.apkey

        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(item)
//                .setObfuscatedProfileId("asdf")
            .setObfuscatedAccountId("$apkey:$productid")
            .build()
//        billingClient.launchBillingFlow(context, flowParams)
        billingClient.launchBillingFlow(context, flowParams)
    }

    // 소비성 아이템 컨슘
    private fun consumePurchase(token: String) {
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(token)
                .build()

        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Handle the success of the consume operation.
            }
        }
    }

    // 백엔드 통신
    fun sendBackend(token: String, productid: String) {
        context.net.pay.confirm(token, productid).enqueue(object : Callback<Confirm> {
            override fun onResponse(call: Call<Confirm>, response: Response<Confirm>) {
                consumePurchase(token)
                context.dialog.dismiss()
            }

            override fun onFailure(call: Call<Confirm>, t: Throwable) {
                // todo : 예외처리
            }

        })
    }

    fun checkProductUnconsume() {
        val purchasesList = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList

        if (purchasesList != null) {
            for (p in purchasesList) {
                sendBackend(p.purchaseToken, p.sku)
            }
        }
    }
}