package com.bazzu.bazzusportsandtips;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.bazzu.bazzusportsandtips.databinding.ActivityMainBinding;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BazengaForceUpdateChecker.OnUpdateNeededListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    TabLayout tabLayoutt;
    ViewPager2 viewPager20;
    private static final String TAG = MainActivity.class.getSimpleName();
    private AdView adView;
    private FrameLayout adContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        tabLayoutt = findViewById(R.id.vtabi);
        viewPager20 = findViewById(R.id.view_pager);
        adContainerView = findViewById(R.id.banHome);
        //MyAdapter adapter = new MyAdapter(this);
        BazengaViewPagerAdapter adapter = new BazengaViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager20.setAdapter(adapter);
        viewPager20.setCurrentItem(0);
        viewPager20.setOffscreenPageLimit(3);
        FirebaseMessaging.getInstance().subscribeToTopic("BazuJackpots");
        new TabLayoutMediator(tabLayoutt, viewPager20, (tab, position) -> {
            if (position == 0) {
                tab.setText("Football Tips");
            } else if (position == 1) {
                tab.setText("Other Sports");
            } else if (position == 2) {
                tab.setText("Jackpot Predictions");
            }
        }).attach();


        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(BazengaForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(BazengaForceUpdateChecker.KEY_CURRENT_VERSION, "1.0");
        remoteConfigDefaults.put(BazengaForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=com.bazzu.bazzusportsandtips");

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);
        //firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                    }


                });

        BazengaForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        /*List<String> testDeviceIds = Arrays.asList("");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);*/

        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = " Hey! Check out this amazing Bazzu  sports and tips app which covers major sports predictions. Download here https://play.google.com/store/apps/details?id=com.bazzu.bazzusportsandtips";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Bazzu Jackpot Predictions");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        } else if (id == R.id.feedback) {
            Intent intentfeed = new Intent(MainActivity.this, ContactFeedback.class);
            startActivity(intentfeed);
        } else if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.policy) {
            Uri uri = Uri.parse("https://bazengatips.blogspot.com/p/privacy-policy.html");
            Intent intentin = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intentin);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find policy page", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.settings) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        } else if (id == R.id.abtback) {
            Uri uri = Uri.parse("https://bazengatips.blogspot.com/p/about-us.html");
            Intent intentin = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intentin);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find about page", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.copyright) {
            Uri uri = Uri.parse("https://bazengatips.blogspot.com/p/copyright-notice.html");
            Intent intentin = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intentin);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find copyright page", Toast.LENGTH_SHORT).show();
            }

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateNeeded(String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version/app available")
                .setMessage("Please, update app to new version or download the new app on the redirect to continue.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.setCancelable(false);
        dialog.show();

    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    @Override
    protected void onStart() {
        BazengaForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (adView != null) {
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}