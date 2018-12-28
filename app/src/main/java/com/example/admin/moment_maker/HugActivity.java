package com.example.admin.moment_maker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HugActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private RecyclerView mRecyclerView;
    private static final int REQUEST_INVITE = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    DatabaseReference ref;
    List<UserDetails> userslist;
    private ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hug);
        mRecyclerView=(RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref=FirebaseDatabase.getInstance().getReference("Hugs");
        mprogress=(ProgressBar)findViewById(R.id.progressbar2);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(AppInvite.API)
                .enableAutoManage(this,this).build();
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient,this,autoLaunchDeepLink);

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"connectionFailed"+connectionResult);
        Toast.makeText(getApplicationContext(),"Google play services error",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<UserDetails,ViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<UserDetails, ViewHolder>(
                        UserDetails.class,
                        R.layout.hug_listview,
                        ViewHolder.class,
                        ref
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, UserDetails model, int position) {

                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(),model.getDate(),model.getImageUrl());
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void sendInvite(View view) {
        Intent intent = new AppInviteInvitation.IntentBuilder("Send App Invite Quickstart Invitation")
                .setMessage("Hi friends pls record a video for the birthday boy")
                .setDeepLink(Uri.parse("https://example.com/record/bday_video"))
                .setCustomImage(Uri.parse("https://www.google.com/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwjdxvfRj7jfAhXOfysKHdLkC58QjRx6BAgBEAU&url=https%3A%2F%2Ftwitter.com%2Fhashtag%2Fbdaygirl&psig=AOvVaw1REPNgaGcB8ITo9cdSAP5P&ust=1545729052714028"))
                .setCallToActionText("Install").build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(TAG,"onActivityResult: requestCode="+requestCode+", resultCode="+ requestCode);
            if(requestCode == REQUEST_INVITE){
                if(requestCode == RESULT_OK){
                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode,data);
                    Log.d(TAG,"Sent {ids.length} invitations");
                } else {
                    Toast.makeText(getApplicationContext(),"sending invitation failed",Toast.LENGTH_SHORT).show();
                }
            }
        }
}
