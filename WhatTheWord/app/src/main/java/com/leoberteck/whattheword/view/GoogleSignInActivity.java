package com.leoberteck.whattheword.view;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.leoberteck.whattheword.R;

public abstract class GoogleSignInActivity extends AppCompatActivity {

    protected static final int RC_SIGN_IN = 9001;
    protected GoogleSignInAccount googleSignInAccount;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                this.googleSignInAccount = result.getSignInAccount();
            } else {
                String message = getString(
                        R.string.signin_error
                        , result.getStatus().getStatus() + " - " + result.getStatus().getStatusMessage());
                new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(message)
                        .setNeutralButton(R.string.ok, null).show();
            }
        }
    }

    protected void aquireSignIn(){
        if(googleSignInAccount == null){
            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (googleSignInAccount != null) {
                this.googleSignInAccount = googleSignInAccount;
            } else {
                signInSilently();
            }
        }
    }

    protected void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(
                this
                , GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(
                this
                , task -> {
                    if (task.isSuccessful()) {
                        GoogleSignInActivity.this.googleSignInAccount = task.getResult();
                    } else {
                        GoogleSignInActivity.this.signInExplicitly();
                    }
                });
    }

    protected void signInExplicitly() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build();

        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, gso);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
}
