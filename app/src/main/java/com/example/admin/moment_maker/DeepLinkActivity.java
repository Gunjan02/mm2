package com.example.admin.moment_maker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteReferral;

public class DeepLinkActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = DeepLinkActivity.class.getSimpleName();
    TextView deeptext,invitetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
        findViewById(R.id.button_ok).setOnClickListener(this);
        deeptext = findViewById(R.id.deep_link_text);
        invitetext = findViewById(R.id.invitation_id_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if(AppInviteReferral.hasReferral(intent)){
            processReferralIntent(intent);
        }
    }

    private void processReferralIntent(Intent intent) {
        String invitationId = AppInviteReferral.getInvitationId(intent);
        String deepLink = AppInviteReferral.getDeepLink(intent);
        Log.d(TAG,"Found Referral: "+invitationId+" : "+deepLink);
        deeptext.setText("Deep Link: {deepLink}");
        invitetext.setText("Invitation ID: {invitationId}");
    }

    @Override
    public void onClick(View view) {
        finish();
    }

}
