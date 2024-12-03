package com.bazzu.bazzusportsandtips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BazengafbGames extends AppCompatActivity {
    private String db, selectedp, title;
    private DatabaseReference Dbref;
    private RecyclerView mrecycler;
    private LinearLayoutManager mlinearlayout;
    FrameLayout adcontanier;
    TextView loading;
    FirebaseRecyclerAdapter<TipsBazu, BazengaViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<TipsBazu> optselect;
    private final String TAG = BazengafbGames.class.getSimpleName();
    InterstitialAd mInterstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-8414079200987268/9356349338";
    RewardedInterstitialAd rewardedInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazengafb_games);
        db = getIntent().getExtras().getString("db");
        selectedp = getIntent().getExtras().getString("selected");
        title = getIntent().getExtras().getString("title");
        this.setTitle(title);
        Dbref = FirebaseDatabase.getInstance().getReference().child(db).child(selectedp);
        loading = findViewById(R.id.loadfbgames);
        mrecycler = findViewById(R.id.recyclerFB);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getApplicationContext());
        mrecycler.setLayoutManager(mlinearlayout);
        Query query = Dbref.limitToFirst(15);

        optselect = new FirebaseRecyclerOptions.Builder<TipsBazu>().setQuery(query, TipsBazu.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TipsBazu, BazengaViewHolder>(optselect) {
            @Override
            protected void onBindViewHolder(@NonNull BazengaViewHolder holder, int position, @NonNull TipsBazu model) {
                final String item_key = getRef(position).getKey();
                holder.setTitle(model.getTitle());
                holder.setDetails(model.getBody());
                holder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                holder.setOnClickListener(new BazengaViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        handleItemClick(view, item_key);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                interstADLoad();
                rewardsADLoad();

            }

            @NonNull
            @Override
            public BazengaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcardbazenga, parent, false);
                BazengaViewHolder viewHolder = new BazengaViewHolder(itemView);
                return viewHolder;
            }
        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStart() {
        interstADLoad();
        rewardsADLoad();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    public void rewardsADLoad() {
        runOnUiThread(() -> {
            RewardedInterstitialAd.load(this, getResources().getString(R.string.rewarded_ad_unit_id),
                    new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(RewardedInterstitialAd ad) {
                            Log.d(TAG, "Ad was loaded.");
                            rewardedInterstitialAd = ad;
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                        }
                    });
        });
    }

    public void interstADLoad() {
        runOnUiThread(() -> {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this, getResources().getString(R.string.interstitial_ad_unit_id), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            Log.i(TAG, "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });
        });
    }

    private void handleItemClick(View view, String item_key) {
        if ("Daily 2+ Odds".equals(title) || "Super Singles".equals(title)) {
            if (rewardedInterstitialAd != null) {
                final boolean[] userEarnedReward = {false};
                rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad dismissed fullscreen content.");
                        rewardedInterstitialAd = null;
                        if (userEarnedReward[0]) {
                            startBazengaPostDetailsActivity(view, item_key);
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.e(TAG, "Ad failed to show fullscreen content.");
                        rewardedInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });
                rewardedInterstitialAd.show(BazengafbGames.this, rewardItem -> {
                    Log.d(TAG, "The user earned the reward.");
                    userEarnedReward[0] = true;
                });
            } else {
                Log.d(TAG, "The rewarded interstitial ad wasn't ready yet.");
                Toast.makeText(BazengafbGames.this, "No video available at the moment try again later.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mInterstitialAd != null) {
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad dismissed fullscreen content.");
                        mInterstitialAd = null;
                        startBazengaPostDetailsActivity(view, item_key);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.e(TAG, "Ad failed to show fullscreen content." + adError);
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });
                mInterstitialAd.show(BazengafbGames.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                startBazengaPostDetailsActivity(view, item_key);
            }
        }
    }

    private void startBazengaPostDetailsActivity(View view, String item_key) {
        Intent intent = new Intent(view.getContext(), BazengaPostDetails.class);
        intent.putExtra("postkey", item_key);
        intent.putExtra("dbs", db);
        intent.putExtra("selection", selectedp);
        intent.putExtra("title", title);
        startActivity(intent);
    }

}