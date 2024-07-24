package com.bazzu.bazzusportsandtips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BazengaPostDetails extends AppCompatActivity {
   private DatabaseReference mRef;
    private String postKey;
    private TextView tvTitle, tvBody, tvTime;
    ProgressDialog pd;
    private String selection, db, title;
    private FrameLayout adcontainer;

    private final String TAG = BazengaPostDetails.class.getSimpleName();
    private FrameLayout mBannerParentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazenga_post_details);
        postKey = getIntent().getExtras().getString("postkey");
        db = getIntent().getExtras().getString("dbs");
        selection = getIntent().getExtras().getString("selection");
        title = getIntent().getExtras().getString("title");
        this.setTitle(title);
        tvBody = findViewById(R.id.tvBody);
        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.post_time);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        if (postKey != null) {

            mRef = FirebaseDatabase.getInstance().getReference().child(db).child(selection).child(postKey);

        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue().toString();
                String body = snapshot.child("body").getValue().toString();
                Long time = (Long) snapshot.child("time").getValue();

                if (title != null) {
                    tvTitle.setText(title.toUpperCase());
                    pd.dismiss();
                } else {
                    Toast.makeText(BazengaPostDetails.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
                if (body != null) {
                    tvBody.setText(body);

                }
                if (time != null) {
                    setTime(time);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        if (id == android.R.id.home){
            finish();
        }
        else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = " Hey! Check out this amazing Bazzu sports and tips app which covers major sports predictions. Download here https://play.google.com/store/apps/details?id=com.bazzu.bazzusportsandtips";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Bazzu Jackpot Predictions");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }
        else if (id == R.id.policy) {
        Uri uri = Uri.parse("https://bazengatips.blogspot.com/p/privacy-policy.html");
        Intent intentin = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intentin);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find policy page", Toast.LENGTH_SHORT).show();
        }
        }else if (id == R.id.feedback) {
            Intent intentfeed = new Intent(BazengaPostDetails.this, ContactFeedback.class);
            startActivity(intentfeed);
        }

        else if (id == R.id.abtback) {
        Uri uri = Uri.parse("https://bazengatips.blogspot.com/p/about-us.html");
        Intent intentin = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intentin);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find about page", Toast.LENGTH_SHORT).show();
        }
        }else if (id == R.id.copyright){
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
    public void setTime(Long time) {
        TextView txtTime = (TextView) findViewById(R.id.post_time);
        //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
        long elapsedTime;
        long currentTime = System.currentTimeMillis();
        int elapsed = (int) ((currentTime - time) / 1000);
        if (elapsed < 60) {
            if (elapsed < 2) {
                txtTime.setText("Just Now");
            } else {
                txtTime.setText(elapsed + " sec ago");
            }
        } else if (elapsed > 604799) {
            elapsedTime = elapsed / 604800;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " week ago");
            } else {

                txtTime.setText(elapsedTime + " weeks ago");
            }
        } else if (elapsed > 86399) {
            elapsedTime = elapsed / 86400;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " day ago");
            } else {
                txtTime.setText(elapsedTime + " days ago");
            }
        } else if (elapsed > 3599) {
            elapsedTime = elapsed / 3600;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " hour ago");
            } else {
                txtTime.setText(elapsedTime + " hours ago");
            }
        } else if (elapsed > 59) {
            elapsedTime = elapsed / 60;
            txtTime.setText(elapsedTime + " min ago");


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
