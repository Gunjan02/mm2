package com.example.admin.moment_maker;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ShowVideo extends AppCompatActivity {
Intent intent;
private String uname,bdaygirl;
private StorageReference storageReference,videoRef;
File localfile;
ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        mProgressBar = findViewById(R.id.progressbar3);
        uname=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        storageReference=FirebaseStorage.getInstance().getReference("Videos");
        intent = getIntent();
        bdaygirl = intent.getStringExtra("bdaygirl");
        videoRef = storageReference.child(uname+" wishes "+bdaygirl);
        try {
            localfile=File.createTempFile("UserVideo","mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendVideo(View view) {
        final Intent i=new Intent(Intent.ACTION_SEND);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Intent.EXTRA_TEXT,"On Your special day, here is a surprise!!");
        i.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(localfile));
        i.setType("video/3gpp");
        startActivity(Intent.createChooser(i,"Share video via"));
    }

    public void playVideo(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        try {
            videoRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    mProgressBar.setVisibility(View.GONE);
                    VideoView videoView=(VideoView)findViewById(R.id.videoView);
                    videoView.setVideoURI(Uri.fromFile(localfile));
                    videoView.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ShowVideo.this, "Video failed to download", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Failed to create temp file", Toast.LENGTH_SHORT).show();
        }
    }
}
