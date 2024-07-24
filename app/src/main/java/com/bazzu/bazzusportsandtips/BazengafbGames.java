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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
    InterstitialAd interstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-8414079200987268/9356349338";

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
                        if (interstitialAd != null) {
                            interstitialAd.show(BazengafbGames.this);
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    Log.d("TAG", "Ad closed.");
                                    Intent intent = new Intent(view.getContext(), BazengaPostDetails.class);
                                    intent.putExtra("postkey", item_key);
                                    intent.putExtra("dbs",db);
                                    intent.putExtra("selection", selectedp);
                                    intent.putExtra("title",title);
                                    startActivity(intent);
                                    interstADLoad();

                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                }

                            });
                        }
                        else {
                            Log.d("TAG", "Ad did not load.");
                            Intent intent = new Intent(view.getContext(), BazengaPostDetails.class);
                            intent.putExtra("postkey", item_key);
                            intent.putExtra("dbs",db);
                            intent.putExtra("selection", selectedp);
                            intent.putExtra("title",title);
                            startActivity(intent);
                            interstADLoad();

                        }


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                interstADLoad();

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);

    }
    public void interstADLoad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        BazengafbGames.this.interstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                       BazengafbGames.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        BazengafbGames.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;

                        @SuppressLint("DefaultLocale") String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.d("TAG", error);

                    }
                });
    }

}