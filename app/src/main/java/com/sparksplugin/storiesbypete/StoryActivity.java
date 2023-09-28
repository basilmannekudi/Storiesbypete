package com.sparksplugin.storiesbypete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class StoryActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView con;
    String name;
    String epi;
    String epn;
    ImageView mplay;
    ImageView mprev;
    ImageView mnext;
    ImageView mStop;
    ProgressBar mPb;
    TextView epi_count;
    final MediaPlayer[] mediaPlayer = {new MediaPlayer()};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story);

        con = findViewById(R.id.story);
        mplay = findViewById(R.id.play);
        mprev = findViewById(R.id.prev);
        mnext = findViewById(R.id.next);
        mStop = findViewById(R.id.stopp);
        mPb = findViewById(R.id.pb);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        epi = bundle.getString("epi");
        epn = bundle.getString("epn");
        epi_count = findViewById(R.id.epi_count);
        String val = epi+"/"+epn;
        epi_count.setText(val);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stories").child(name).child("episodes").child(epi).child("text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                con.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop.setVisibility(View.GONE);
                mplay.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.GONE);
                mediaPlayer[0].stop();
                int i = Integer.parseInt(epi);
                if(i>1)
                {
                    int s = i-1;
                    String h = String.valueOf(s);
                    Intent intent = new Intent(getApplicationContext(),StoryActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("epi",h);
                    intent.putExtra("epn",epn);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(StoryActivity.this, "First Episode", Toast.LENGTH_SHORT).show();

                }
            }
        });


        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop.setVisibility(View.GONE);
                mplay.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.GONE);
                mediaPlayer[0].stop();
                int i = Integer.parseInt(epi);
                int n = Integer.parseInt(epn);
                if(i<n)
                {
                    int s = i+1;
                    String h = String.valueOf(s);
                    Intent intent = new Intent(getApplicationContext(),StoryActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("epi",h);
                    intent.putExtra("epn",epn);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(StoryActivity.this, "Starting Over", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),StoryActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("epi", "1");
                    intent.putExtra("epn",epn);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();

                }
            }
        });
        mplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplay.setVisibility(View.GONE);
                mPb.setVisibility(View.VISIBLE);
                StorageReference audioRef = storageRef.child(name).child(epi).child("audio.mp3");
                audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // The download URL was successfully retrieved
                        String audioUrl = uri.toString();

                        try {
                            mediaPlayer[0].setDataSource(audioUrl);
                            mediaPlayer[0].prepare();
                            mediaPlayer[0].start();
                            mPb.setVisibility(View.GONE);
                            mStop.setVisibility(View.VISIBLE);
                            mediaPlayer[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mStop.setVisibility(View.GONE);
                                    mplay.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (IOException e) {
                            Toast.makeText(StoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // TODO: Use the audio URL to play the audio file
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // An error occurred while retrieving the download URL
                        mStop.setVisibility(View.GONE);
                        mplay.setVisibility(View.VISIBLE);
                        mPb.setVisibility(View.GONE);
                        Toast.makeText(StoryActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop.setVisibility(View.GONE);
                mplay.setVisibility(View.VISIBLE);
                mPb.setVisibility(View.GONE);
                mediaPlayer[0].stop();
                mediaPlayer[0].reset();
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (mediaPlayer[0] != null && mediaPlayer[0].isPlaying()) {
            mediaPlayer[0].stop();
            mediaPlayer[0].release();
            mediaPlayer[0]= null;
        }
        super.onBackPressed();
    }

}