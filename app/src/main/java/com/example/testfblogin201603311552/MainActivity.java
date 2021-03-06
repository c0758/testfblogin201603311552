package com.example.testfblogin201603311552;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {



    private static final String TAG = "SignInActivity";
    private static final String TAG2 = "SignInActivity2";

    CallbackManager callbackManager;
    //private AccessToken accessToken;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView;

    // connect
    private EditText targetIP;
    String str1 = "0", str2 = "0";
    private Button btn;
    private TextView tv1, tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //accessToken之後或許還會用到 先存起來

                Profile profile = Profile.getCurrentProfile();
                Log.d("FBXXX", "getName:" + profile.getName());
                Log.d("FBXXX", "getId:" + profile.getId());
                Log.d("FBXXX", "getLinkUri:" + profile.getLinkUri());
                Log.d("FBXXX", "getFirstName:" + profile.getFirstName());
                Log.d("FBXXX", "getMiddleName:" + profile.getMiddleName());
                Log.d("FBXXX", "getMiddleName:" + profile.getMiddleName());
                Log.d("FBXXX", "getProfilePictureUri:" + profile.getProfilePictureUri(100, 100));

                Log.d("FBXXX", "getUserId:" + loginResult.getAccessToken().getUserId());
                Log.d("FBXXX", "getToken:" + loginResult.getAccessToken().getToken());
                Log.d("FBXXX", "getApplicationId:" + loginResult.getAccessToken().getApplicationId());


            }

            @Override
            public void onCancel() {
                Log.d("FBXXX", "CANCEL");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FBXXX", error.toString());
            }
        });

        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));


        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]
        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

// Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

///////////////////
        targetIP = (EditText) findViewById(R.id.targetIP);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        btn = (Button) findViewById(R.id.connectBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Thread t = new thread();
                t.start();
                try {

                    t.sleep(3000);
                } catch (InterruptedException e) {
                }
                tv1.setText(str1);
                tv2.setText(str2);
            }
        });

    }
    class thread extends Thread {
        public void run() {
            try {
                System.out.println("Waitting to connect......");
                String server = targetIP.getText().toString();
                int servPort = 1026;
                Socket socket = new Socket(server, servPort);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                System.out.println("Connected!!");
                byte[] rebyte = new byte[80];
                in.read(rebyte);
                str2 = "(Client端)接收的文字:" + new String(new String(rebyte));

                String str = "android client string 20160413";
                str1 = "(Client端)傳送的文字:" + str;
                byte[] sendstr = new byte[80];
                System.arraycopy(str.getBytes(), 0, sendstr, 0, str.length());
                out.write(sendstr);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FBXXX", "onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            GoogleSignInAccount acct = result.getSignInAccount();
            if(acct==null) return; // Log.i("error","acct= "+acct);

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Log.d("GOOGLEXXX", "  0: " + personName + "  1: " + personEmail + "  2: " + personId + "  3: " + personPhoto);

        }


    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("FBXXX", "onResume");
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("FBXXX", "onPause");
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //fortest
}
