package com.bazzu.bazzusportsandtips;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class BazengaForceUpdateChecker {


        private static final String TAG = BazengaForceUpdateChecker.class.getSimpleName();

        public static final String KEY_UPDATE_REQUIRED = "forced_update_required_jp";
        public static final String KEY_CURRENT_VERSION = "forced_update_current_version_jp";
        public static final String KEY_UPDATE_URL = "forced_update_store_urls_jp";

        private OnUpdateNeededListener onUpdateNeededListener;
        private Context context;

        public interface OnUpdateNeededListener {
            void onUpdateNeeded(String updateUrl);
        }

        public static Builder with(@NonNull Context context) {
            return new Builder(context);
        }

        public BazengaForceUpdateChecker(@NonNull Context context,
                                         OnUpdateNeededListener onUpdateNeededListener) {
            this.context = context;
            this.onUpdateNeededListener = onUpdateNeededListener;
        }

        public void check() {
            final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

            if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
                String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
                String appVersion = getAppVersion(context);
                String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

                if (!TextUtils.equals(currentVersion, appVersion)
                        && onUpdateNeededListener != null) {
                    onUpdateNeededListener.onUpdateNeeded(updateUrl);
                }
            }
        }

        private String getAppVersion(Context context) {
            String result = "";

            try {
                result = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0)
                        .versionName;
                result = result.replaceAll("[a-zA-Z]|-", "");
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }

            return result;
        }

        public static class Builder {

            private Context context;
            private OnUpdateNeededListener onUpdateNeededListener;

            public Builder(Context context) {
                this.context = context;
            }

            public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
                this.onUpdateNeededListener = onUpdateNeededListener;
                return this;
            }

            public BazengaForceUpdateChecker build() {
                return new BazengaForceUpdateChecker(context, onUpdateNeededListener);
            }

            public BazengaForceUpdateChecker check() {
                BazengaForceUpdateChecker forceUpdateChecker = build();
                forceUpdateChecker.check();

                return forceUpdateChecker;
            }
        }
    }