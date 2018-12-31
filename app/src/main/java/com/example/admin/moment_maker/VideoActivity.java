package com.example.admin.moment_maker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

public class VideoActivity extends AppCompatActivity {
    private Uri videoUri;
    private static final int REQUEST_CODE=101;
    private StorageReference videoRef,storageReference;
    Button send;
    private int mYear, mMonth, mDay;
    String bdaygirl,uname,bdate;
    private DatabaseReference mDatabase;
EditText editText1,editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        send=(Button)findViewById(R.id.sendbtn);
        editText1 = findViewById(R.id.editText);
        editText2 =findViewById(R.id.editText2);
        uname=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mDatabase=FirebaseDatabase.getInstance().getReference("VideoHugs");
        storageReference=FirebaseStorage.getInstance().getReference("Videos");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVideo();
            }
        });
    }


    public void upload(View view){
        if(videoUri!=null){
            UploadTask uploadTask=videoRef.putFile(videoUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(VideoActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(VideoActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                    if(!TextUtils.isEmpty(bdaygirl)&&!TextUtils.isEmpty(bdate)){

                        String id=mDatabase.push().getKey();
                        VideoDetails videoDetails=new VideoDetails(id,bdaygirl,bdate,taskSnapshot.getUploadSessionUri().toString());
                        mDatabase.child(id).setValue(videoDetails);

                    }else {
                        Toast.makeText(VideoActivity.this, "Please fill in the details!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    updateProgress(taskSnapshot);
                }
            });
        }
    }

    private void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long filesize=taskSnapshot.getTotalByteCount();
        long uploadbytes=taskSnapshot.getBytesTransferred();
        long progress=(100*uploadbytes)/filesize;
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.uploadProgressbar);
        progressBar.setProgress((int)progress);
    }

    public void record(View view){
        bdaygirl = editText1.getText().toString();
        bdate = editText2.getText().toString();
        videoRef=storageReference.child(uname+" wishes "+bdaygirl);
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void download(View view){
        try {
            final File localfile=File.createTempFile("UserVideo","mp3");
            videoRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(VideoActivity.this, "Downloaded Video", Toast.LENGTH_SHORT).show();
                    final VideoView videoView=(VideoView)findViewById(R.id.videoView);
                    videoView.setVideoURI(Uri.fromFile(localfile));
                    videoView.start();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(VideoActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Failed to create temp file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        videoUri=data.getData();
        if(requestCode==REQUEST_CODE){
            if(requestCode==RESULT_OK){
                Toast.makeText(this, "Video saved to: "+videoUri, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sendVideo() {

        final Intent i=new Intent(Intent.ACTION_SEND);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Intent.EXTRA_TEXT,"On Your special day, here is a surprise!!");
        i.putExtra(Intent.EXTRA_STREAM,videoUri);
        i.setType("video/mp3");
        startActivity(Intent.createChooser(i,"Share images via"));
    }

    public void date(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year); }}, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}
