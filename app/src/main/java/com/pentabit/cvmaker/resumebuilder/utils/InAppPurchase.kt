package com.pentabit.galleryvault.utils.inapp

import android.app.Activity
import android.content.Context
import androidx.annotation.Keep
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.pentabit.pentabitessentials.in_app.callbacks.InAppPurchaseCallBack
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogManager
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogType


@Keep
internal object InAppPurchase {
    private lateinit var billingClient: BillingClient
    private var skuDetails: SkuDetails? = null
    private var isConsumable: Boolean = true
    private var CURRENT_SKU = "remove_ads"
    private lateinit var callback: InAppPurchaseCallBack
    private val boostList = ArrayList<Purchase>()

    fun startBillingFlow(
        context: Context,
        skuId: String,
        isConsumable: Boolean,
        inAppPurchaseCallBack: InAppPurchaseCallBack
    ) {
        InAppPurchase.isConsumable = isConsumable
        CURRENT_SKU = skuId
        callback = inAppPurchaseCallBack
        setUpBillingClient(context)
    }

    private fun setUpBillingClient(context: Context) {
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                if ((billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED && purchases != null) ||
                    (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null)
                )
                    handlePurchaseList(purchases)
            }
            .enablePendingPurchases()
            .build()
        startConnection(context)
    }

    private fun handlePurchaseList(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (!isConsumable && purchase.purchaseState == Purchase.PurchaseState.PURCHASED && purchase.originalJson.contains(
                    CURRENT_SKU
                )
            )
                handleNonConsumablePurchase(purchase)
            else if (isConsumable && purchase.purchaseState == Purchase.PurchaseState.PURCHASED && purchase.originalJson.contains(
                    CURRENT_SKU
                )
            ) {
                boostList.add(purchase)
                handleConsumablePurchase(purchase)
            }
        }
        boostList.clear()
    }

    private fun startConnection(context: Context) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryAvaliableProducts(context)
                }
            }

            override fun onBillingServiceDisconnected() {

            }
        })
    }

    private fun queryAvaliableProducts(context: Context) {
        val skuList = ArrayList<String>()
        skuList.add(CURRENT_SKU)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                for (skuDetails in skuDetailsList) {
                    InAppPurchase.skuDetails = skuDetails
                    InAppPurchase.skuDetails?.let {
                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(it)
                            .build()
                        billingClient.launchBillingFlow(
                            context as Activity,
                            billingFlowParams
                        ).responseCode
                    }
                }
            }
        }
    }

    private fun handleNonConsumablePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken).build()
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) {
                callback.isAppPurchased(true, 0, purchase.purchaseToken, purchase.orderId)
            }
        } else
            callback.isAppPurchased(true, 0, purchase.purchaseToken, purchase.orderId)
    }

    private fun handleConsumablePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken).build()
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) {
                consumePurchase(purchase, billingClient)
                callback.isAppPurchased(
                    true,
                    purchase.quantity,
                    purchase.purchaseToken,
                    purchase.orderId
                )
            }
        } else
            callback.isAppPurchased(true, 0, purchase.purchaseToken, purchase.orderId)
    }

    fun consumePurchase(purchase: Purchase, billingClient: BillingClient) {
        billingClient.consumeAsync(
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        ) { billingResult: BillingResult, _: String? ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK)
                AppsKitSDKLogManager.getInstance().log(
                    AppsKitSDKLogType.INFO, billingResult.debugMessage)
            else AppsKitSDKLogManager.getInstance().log(
                AppsKitSDKLogType.ERROR, billingResult.debugMessage)
        }
    }
}