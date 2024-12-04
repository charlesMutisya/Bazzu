package com.bazzu.bazzusportsandtips;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BazengaApp extends MultiDexApplication
        implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    private static final String TAG = "BazengaApp";
    private static BazengaApp mInstance;
    private static Context context;

    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);

        FirebaseApp.initializeApp(this);
        mInstance = this;
        context = getApplicationContext();
        BazengaApp.context = getApplicationContext();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        appOpenAdManager = new AppOpenAdManager();
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(BazengaForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(BazengaForceUpdateChecker.KEY_CURRENT_VERSION, BuildConfig.VERSION_NAME);
        remoteConfigDefaults.put(BazengaForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=" + getPackageName());

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);
        //firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.fetchAndActivate();
                            //Log.d(TAG, "remote config is fetched. " + firebaseRemoteConfig.getAll());
                        }
                    }
                });
        Log.d(TAG, "BazengaApp onCreate: Application started");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        Log.d(TAG, "BazengaApp attachBaseContext: MultiDex installed");
    }

    public static synchronized BazengaApp getInstance() {
        return mInstance;
    }

    /**
     * DefaultLifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        Log.d(TAG, "BazengaApp onStart: App moved to foreground");
        // Show the ad (if available) when the app moves to foreground.
        appOpenAdManager.showAdIfAvailable(currentActivity);
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    /**
     * Load an app open ad.
     *
     * @param activity the activity that shows the app open ad
     */
    public void loadAd(@NonNull Activity activity) {
        // We wrap the loadAd to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.loadAd(activity);
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(
            @NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    /**
     * Inner class that loads and shows app open ads.
     */
    private class AppOpenAdManager {
        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        /**
         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
         */
        private long loadTime = 0;

        /**
         * Constructor.
         */
        public AppOpenAdManager() {
            Log.d(TAG, "AppOpenAdManager: Initialized");
        }

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        private void loadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                Log.d(TAG, "AppOpenAdManager loadAd: Ad is already loading or available");
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    getString(R.string.app_open_ad_unit_id),
                    request,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();
                            Log.d(TAG, "AppOpenAdManager onAdLoaded: Ad loaded successfully");
                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;
                            Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                        }
                    });
        }

        /**
         * Check if ad was loaded more than n hours ago.
         */
        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        /**
         * Check if ad exists and can be shown.
         */
        private boolean isAdAvailable() {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            // Empty because the user will go back to the activity that shows the ad.
                        }
                    });
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity                 the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(currentActivity);
                return;
            }

            Log.d(TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(TAG, "onAdDismissedFullScreenContent.");

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "onAdShowedFullScreenContent.");
                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);
        }
    }
}
