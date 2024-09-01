package com.pentabit.cvmaker.resumebuilder.views.activities;


import static com.pentabit.cvmaker.resumebuilder.utils.Constants.REMOVE_ADS_ID;
import static com.pentabit.cvmaker.resumebuilder.utils.Constants.SKU_SUBS;
import static com.pentabit.cvmaker.resumebuilder.utils.Constants.SKU_SUBS_1;
import static com.pentabit.cvmaker.resumebuilder.utils.Constants.SKU_SUBS_2;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.common.collect.ImmutableList;
import com.pentabit.cvmaker.resumebuilder.databinding.ActivitySubscriptionScreenBinding;
import com.pentabit.cvmaker.resumebuilder.models.SubscriptionPackageModel;
import com.pentabit.cvmaker.resumebuilder.utils.FreeTaskManager;
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs;
import com.pentabit.cvmaker.resumebuilder.utils.Security;
import com.pentabit.cvmaker.resumebuilder.utils.Utils;
import com.pentabit.cvmaker.resumebuilder.views.adapter.SubscriptionAdapter;
import com.pentabit.pentabitessentials.firebase.AppsKitSDK;
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKEventCreator;
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKEventType;
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogManager;
import com.pentabit.pentabitessentials.utils.AppsKitSDKThreadHandler;
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SubscriptionActivity extends AdBaseActivity implements PurchasesUpdatedListener {

    ActivitySubscriptionScreenBinding binding;
    private BillingClient billingClient;
    List<String> skuKeys = new ArrayList<>();
    List<SubscriptionPackageModel> packagesList = new ArrayList<>();
    SubscriptionAdapter adapter = new SubscriptionAdapter();
    String lastSelected = SKU_SUBS_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        init();
        skuKeys.add(SKU_SUBS);
        skuKeys.add(SKU_SUBS_1);
        skuKeys.add(SKU_SUBS_2);

        binding.infoMsg.setMovementMethod(LinkMovementMethod.getInstance());
        checkInternetAndManageUI();
    }


    private void changeVisibilitiesOfLoadingAndRetry(boolean isInternet) {
        binding.loading.setVisibility(isInternet ? View.VISIBLE : View.GONE);
        binding.internetNotAvailable.setVisibility(isInternet ? View.GONE : View.VISIBLE);
    }

    private void onRestorePurchase(boolean showToast) {
        BillingClient billingClient =
                BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {

                }).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(), (billingResult1, purchases) -> {
                        if (!purchases.isEmpty()) {
                            boolean isRestored = false;
                            for (Purchase p : purchases) {
                                if (p.getSkus().contains(SKU_SUBS) || p.getSkus().contains(SKU_SUBS_1) || p.getSkus().contains(SKU_SUBS_2)) {
                                    if (p.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                        FreeTaskManager.getInstance().proPurchased();
                                        isRestored = true;
                                    } else {
                                        FreeTaskManager.getInstance().unSubscribeProSub();
                                        isRestored = false;
                                    }
                                }
                            }
                            if (showToast && isRestored)
                                AppsKitSDKUtils.makeToast("Pro subscription restored successfully");
                            if (!isRestored) {
                                runOnUiThread(() -> checkForRemoveAds(billingClient, showToast));
                            }
                        } else {
                            FreeTaskManager.getInstance().unSubscribeProSub();
                            runOnUiThread(() -> checkForRemoveAds(billingClient, showToast));
                        }
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // do nothing
            }
        });
    }

    public void getPrices(@NonNull List<String> packageIds, BillingClient billingClient, SubscriptionsPricesListener listener) {
        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
        final Map<String, List<String>> packagesPrices = new HashMap<>();
        for (String val : packageIds)
            productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId(val).setProductType(BillingClient.ProductType.SUBS).build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder().setProductList(ImmutableList.copyOf(productList)).build();

        BillingResult billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.queryProductDetailsAsync(params, (billingResult1, productDetailsList) -> {
                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK && !productDetailsList.isEmpty()) {
                    AppsKitSDKThreadHandler.getInstance().runOnUIThread(() -> listener.onSubscriptionPriceFormatFinalize(AppsKitSDKUtils.removeNumbersAndPeriods(productDetailsList.get(0).getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice())));
                    for (ProductDetails productDetails : productDetailsList)
                        savePriceInMap(productDetails, packageIds, packagesPrices);
                    AppsKitSDKThreadHandler.getInstance().runOnUIThread(() -> listener.onSubscriptionPricesExtracted(packagesPrices));
                } else
                    AppsKitSDKThreadHandler.getInstance().runOnUIThread(listener::onSubscriptionsNotFount);

            });
        } else
            AppsKitSDKThreadHandler.getInstance().runOnUIThread(() -> listener.onErrorInFetchingSubscriptions(billingResult.getResponseCode(), billingResult.getDebugMessage()));
    }

    private void savePriceInMap(ProductDetails productDetails, @NonNull List<String> packageIds, Map<String, List<String>> packagesPrices) {
        for (String val : packageIds) {
            if (productDetails.getProductId().equals(val) && (productDetails.getSubscriptionOfferDetails() != null)) {
                final List<String> lst = new ArrayList<>();
                for (ProductDetails.PricingPhase model : productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList()) {
                    lst.add(model.getFormattedPrice());
                }
                packagesPrices.put(val, lst);
            }
        }
    }

    private void checkForRemoveAds(BillingClient billingClient, boolean showToast) {
        billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(), (billingResult1, purchases) -> {
            if (!purchases.isEmpty()) {
                boolean restored = false;
                for (Purchase p : purchases) {
                    for (String id : p.getSkus()) {
                        if (id.equals(REMOVE_ADS_ID)) {
                            restored = p.getPurchaseState() == Purchase.PurchaseState.PURCHASED;
                            FreeTaskManager.getInstance().removeAdPurchased(restored);
                            AppsKitSDK.getInstance().setRemoveAdsStatus(restored);
                        }
                    }
                }
                if (showToast && restored)
                    AppsKitSDKUtils.makeToast("Remove Ads restored successfully");
                else AppsKitSDKUtils.makeToast("No purchases to restore");
            } else {
                FreeTaskManager.getInstance().removeAdPurchased(false);
                AppsKitSDK.getInstance().setRemoveAdsStatus(false);
                if (showToast)
                    AppsKitSDKUtils.makeToast("No purchases to restore");
            }
        });
    }

    private void init() {
        binding.recyclerView.setAdapter(adapter);
        binding.retry.setOnClickListener(v -> checkInternetAndManageUI());
        binding.subscribe.setOnClickListener(v -> {
            AppsKitSDKLogManager.getInstance().log(
                    this,
                    AppsKitSDKEventCreator.getInstance()
                            .createEvent(screen.getID(), AppsKitSDKEventType.BUTTON, lastSelected + "Subscription")
            );
            onSubscriptionSelected(lastSelected);
        });
        binding.crossIconIv.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void checkInternetAndManageUI() {
        if (Utils.isInternetThroughWiFi() != Utils.InternetConnectionType.NO_INTERNET) {
            initBillingClient();
        } else {
            changeVisibilitiesOfLoadingAndRetry(false);
        }
    }

    private void initBillingClient() {
        changeVisibilitiesOfLoadingAndRetry(true);
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases().setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    getPrices(skuKeys, billingClient, new SubscriptionsPricesListener() {
                        @Override
                        public void onSubscriptionPriceFormatFinalize(String s) {
                        }

                        @Override
                        public void onSubscriptionPricesExtracted(Map<String, List<String>> map) {
                            packagesList.add(manageSubscriptionPriceDisplay(SKU_SUBS, "Weekly Pro", map.get(SKU_SUBS), "per week"));
                            packagesList.add(manageSubscriptionPriceDisplay(SKU_SUBS_2, "Yearly Pro", map.get(SKU_SUBS_2), "per year"));
                            packagesList.add(manageSubscriptionPriceDisplay(SKU_SUBS_1, "Monthly Pro", map.get(SKU_SUBS_1), "per month"));
                            addDiscounts(packagesList);
                            AppsKitSDKUtils.setVisibility(true, binding.subscribe);
                            AppsKitSDKUtils.setVisibility(false, binding.loading);

                            adapter.submitList(packagesList);

                            binding.recyclerView.setOnItemSelectedListener(position -> {
                                lastSelected = packagesList.get(position).getId();
                                AppsKitSDKLogManager.getInstance().log(SubscriptionActivity.this, AppsKitSDKEventCreator.getInstance().createEvent(screen.getID(), AppsKitSDKEventType.TAB, packagesList.get(position).getId()));
                                adapter.updateLayoutAsSelected(position);
                            });

//                            try {
//                                new LinearSnapHelper().attachToRecyclerView(binding.recyclerView);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            if (packagesList.size() > 1) {
                                binding.recyclerView.scrollToPosition(1);
                            }
                        }

                        @Override
                        public void onSubscriptionsNotFount() {

                        }

                        @Override
                        public void onErrorInFetchingSubscriptions(int i, String s) {

                        }
                    });
                    billingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                            (billingResult1, purchases) -> {
                                if (!purchases.isEmpty()) {
                                    handlePurchases(purchases);
                                    //check item in purchase list
                                    for (Purchase p : purchases) {
                                        if (p.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                            if (p.getOriginalJson().contains(SKU_SUBS)) {
                                                FreeTaskManager.getInstance().proPurchased();
                                            }
                                            Toast.makeText(SubscriptionActivity.this, "Restart App to enable subscription",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.e("test", "Subscribe onBillingSetupFinished");
                                        }
                                    }
                                }
                            }
                    );
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                changeVisibilitiesOfLoadingAndRetry(false);
            }
        });
    }

    private void addDiscounts(List<SubscriptionPackageModel>... lsts) {
        for (List<SubscriptionPackageModel> lst : lsts) {
            if (lst != null && !lst.isEmpty())
                for (SubscriptionPackageModel model : lst) {
                    model.setDiscount(calculateDiscountPercentage(lst.get(0), model));
                }
        }
    }

    public String calculateDiscountPercentage(SubscriptionPackageModel basePriceModel, SubscriptionPackageModel modelPriceModel) {
        try {
            double weeklyPrice = Float.parseFloat(basePriceModel.getPrice().replaceAll("[^0-9.]", ""));

            String price = modelPriceModel.getPrice().replaceAll("[^0-9.]", "");
            if (price == null || price.isEmpty())
                price = "0";
            double currentModelPrice = Float.parseFloat(price);

            if (modelPriceModel.getId().equals(SKU_SUBS_1)) {
                double monthlyPriceAgainstWeeks = (weeklyPrice * 4);
                return ((int) (((monthlyPriceAgainstWeeks - currentModelPrice) / monthlyPriceAgainstWeeks) * 100)) + "";
            } else if (modelPriceModel.getId().equals(SKU_SUBS_2)) {
                double yearlyPriceAgainstWeeks = (weeklyPrice * 52);
                return ((int) (((yearlyPriceAgainstWeeks - currentModelPrice) / yearlyPriceAgainstWeeks) * 100)) + "";
            } else {
                return "0";
            }

        } catch (Exception e) {
            return "0";
        }

    }

    public static double calculate(double weeklyPrice) {
        return weeklyPrice * 52;
    }

    public static double calculateDiscount(double calculatedPrice, double givenPrice) {
        if (calculatedPrice == 0) {
            return 0;
        }
        return ((calculatedPrice - givenPrice) / calculatedPrice) * 100;
    }


    private SubscriptionPackageModel manageSubscriptionPriceDisplay(String id, String name, List<String> priceLst, String timePeriod) {
        String itemPrice = "";
        for (String model : priceLst) {
            if (!model.equals("Free"))
                itemPrice = model;
        }
        return new SubscriptionPackageModel(id, name, itemPrice, "", timePeriod, priceLst.size() > 1);
    }

    private void initiatePurchase(final String PRODUCT_ID) {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        BillingResult billingResult = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            if (PRODUCT_ID.equals(SKU_SUBS) || PRODUCT_ID.equals(SKU_SUBS_1)) {
                Log.e("test ", "Subscribe ITEM_ALREADY_OWNED");
                FreeTaskManager.getInstance().proPurchased();
            }
            Log.e("test", "Subscribe ITEM_ALREADY_OWNED");
            Toast.makeText(this, "Please restart the app to enable subscription",
                    Toast.LENGTH_SHORT).show();
        }
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            billingClient.querySkuDetailsAsync(params.build(),
                    (billingResult1, skuDetailsList) -> {
                        if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                Log.e("TAG_INAPP", "skuDetailsList : " + skuDetailsList.get(0).getPrice());
                                Log.e("TAG", "initiatePurchase:" + skuDetailsList.get(0).getPrice());
                                billingClient.launchBillingFlow(this, flowParams);
                            } else {
                                Log.e("Error ", "Subscribe Item " + PRODUCT_ID + " not Found");
                            }
                        } else {
                            Log.e("Error ", billingResult1.getDebugMessage());
                        }
                    });
        } else {
            Log.e("Error ", "Sorry Subscription not Supported. Please Update Play Store");
        }
    }

    void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    continue;//skip current iteration only because other items in purchase list must be checked if present
                }
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && (purchase.getProducts().get(0).contains(SKU_SUBS) || purchase.getProducts().get(0).contains(SKU_SUBS_1))) {
                            Log.e("test ", "Subscribe:: " + purchase.getOrderId());
                            runOnUiThread(() -> Toast.makeText(SubscriptionActivity.this, "Please restart the app to enable subscription", Toast.LENGTH_SHORT).show());
                            FreeTaskManager.getInstance(getApplicationContext()).proPurchased();
                        }
                    });
                }
            }
        }

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        //if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            handlePurchases(list);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            billingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                    (billingResult1, purchases) -> {
                        // Process the result
                        if (!purchases.isEmpty()) {
                            handlePurchases(purchases);
                        }
                    });
        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.e("Error ", "Purchase Canceled");
        }
        // Handle any other error msgs
        else {
            Log.e("Error ", billingResult.getDebugMessage());
        }
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            //for old playconsole
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            //for new play console
            //To get key go to Developer Console > Select your app > Monetize > Monetization setup
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0Z23KptD5ua81l6lDapNhy8udp8Z3PSua7eMs2Qt+B6OeBt+9Iy5XZPGl/uSns7faEzxibXR6vE3IaV9RhIoO7r+gRNh16NtjfmO6DzntEuRNaV2Vf9GoQ1TxFWkuGDX5Mw8jzOHmEFvRbxLjJvMwJy1VFToRvybAiw/EvHsUlHMAThB7voT54M2d3ybkrKOHKGlT8Jhcb/leaeBWiIYK8ri1GJAqZJW1Bt6S3lB///VPLSnDLIydmhMtOmUsZTtH4NZtYvvGpficayq+6yAvuc1f7ftrOPUv7xFazrHVDWaeIn1bQ4FdUsiKF9BoVz3BjxjUct3Fg+e0DSCxS6nTQIDAQAB";
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected View getSnackBarView() {
        return binding.myCoordinatorLayout2;
    }

    @Override
    protected void onInternetConnectivityChange(Boolean isInternetAvailable) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }

    @NonNull
    @Override
    protected ScreenIDs getScreenId() {
        return ScreenIDs.SUBSCRIPTION;
    }

    @Override
    protected boolean isPortrait() {
        return true;
    }

    public void onSubscriptionSelected(String val) {
        if (FreeTaskManager.getInstance(getApplicationContext()).isProPurchased()) {
            Toast.makeText(this, "Already Subscribed", Toast.LENGTH_SHORT).show();
            return;
        }
        if (billingClient.isReady()) {
            initiatePurchase(val);
        } else {
            billingClient = BillingClient.newBuilder(this)
                    .enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(val);
                    } else {
                        Log.e("Error ", billingResult.getDebugMessage());
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    // do nothing
                }
            });
        }
    }

    private interface SubscriptionsPricesListener {
        void onSubscriptionPriceFormatFinalize(String var1);

        void onSubscriptionPricesExtracted(Map<String, List<String>> var1);

        void onSubscriptionsNotFount();

        void onErrorInFetchingSubscriptions(int var1, String var2);
    }
}