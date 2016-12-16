package com.example.saar.reads;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

/**
 * Created by Saar on 13-12-2016.
 */

public class LogIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    // initialise variables
    SignInButton button = (SignInButton)findViewById(R.id.sign_in_button);
    GoogleApiClient myclient;

    private static final String TAG = "LogIn";
    private static final int RC_SIGN_IN = 9001;

    // sign in when the button is clicked
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        myclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent signin = Auth.GoogleSignInApi.getSignInIntent(myclient);
                startActivityForResult(signin, RC_SIGN_IN);
            }
        });
    }

    // google sign in
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // move on unless something goes wrong
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                handle(account);
            }
            else {
                Toast.makeText(this, "Failed to sign in.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // get variables and move to main
    private void handle(GoogleSignInAccount account){
        String name = account.getDisplayName();
        SharedPreferences prefs = getSharedPreferences("Username", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("name", name);
        edit.apply();

        startActivity(new Intent(LogIn.this, MainActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result){
        Log.d(TAG, "onConnectionFailed:" + result);
    }

    // We only want user to log in once
    @Override
    protected void onStart(){
        myclient.connect();
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(myclient);
        if (!(opr.isDone())){
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                    handle(account);
                }
            });
        }
        else {
            Log.d(TAG, "Cached credential check.");
            GoogleSignInResult result = opr.get();
            GoogleSignInAccount account = result.getSignInAccount();
            handle(account);
        }
    }

    // disconnect at the end
    @Override
    protected void onStop(){
        myclient.disconnect();
        super.onStop();
    }
}
