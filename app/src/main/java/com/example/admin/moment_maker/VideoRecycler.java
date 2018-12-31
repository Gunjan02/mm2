package com.example.admin.moment_maker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoRecycler extends AppCompatActivity {
    private DatabaseReference ref;
    private ProgressBar mprogress;
    private VideoAdapter mAdapter;
    TextView noUsersText;
    ListView allVideos;
    ArrayList<VideoDetails> al = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recycler);
        noUsersText = findViewById(R.id.noUsersText);
        mprogress = findViewById(R.id.progressbar3);
        allVideos= findViewById(R.id.rv2);
        ref=FirebaseDatabase.getInstance().getReference("VideoHugs");
        try {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot video : dataSnapshot.getChildren()) {
                            VideoDetails video_details = new VideoDetails(video.child("vidId").getValue().toString(),
                                    video.child("name").getValue().toString(),
                                    video.child("date").getValue().toString(),
                                    video.child("videoUrl").getValue().toString());
                            al.add(video_details);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(al.size() <1){
                        mprogress.setVisibility(View.GONE);
                        noUsersText.setVisibility(View.VISIBLE);
                        allVideos.setVisibility(View.GONE);
                    }
                    else {
                        mprogress.setVisibility(View.GONE);
                        noUsersText.setVisibility(View.GONE);
                        allVideos.setVisibility(View.VISIBLE);
                        mAdapter = new VideoAdapter(VideoRecycler.this,al);
                        allVideos.setAdapter(mAdapter);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.wtf("error = ",databaseError.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
