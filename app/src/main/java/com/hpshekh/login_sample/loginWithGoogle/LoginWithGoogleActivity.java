package com.hpshekh.login_sample.loginWithGoogle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hpshekh.login_sample.R;
import com.hpshekh.login_sample.databinding.ActivityLoginWithGoogleBinding;

public class LoginWithGoogleActivity extends AppCompatActivity {

    private static final String TAG = "hpTag";
    ActivityLoginWithGoogleBinding binding;

    //////////////////////////////////////
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    //////////////////////////////////////

//    private FirebaseAuth.AuthStateListener mAuthStateListener;
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private GoogleSignInClient mGoogleSignInClient;
//    private int RC_SIGN_IN = 1;

    //for SHA certificate fingerprints
    //for me the shortest way is to type "./gradlew signingReport" in the terminal command line.
    //P.s : if you are in Windows use ".\gradlew signingReport" instead.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginWithGoogleBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.progressCircular.setVisibility(View.GONE);

        binding.loginBtn.setOnClickListener(view -> {
//            signIn();
            signIn();
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI(currentUser);
        }
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        binding.progressCircular.setVisibility(View.VISIBLE);
    }

    private void updateUI(FirebaseUser user) {
        Log.d(TAG, String.valueOf(user));
        Log.d(TAG, user.getDisplayName());//GMAILNAME
        Log.d(TAG, user.getEmail());
        Log.d(TAG, String.valueOf(user.getPhotoUrl()));
        binding.progressCircular.setVisibility(View.GONE);
        Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show();
    }


//    ///////////////////////////////////------- Gmail Login ----------/////////////////////////////
//    private void signIn() {
//        binding.progressCircular.setVisibility(View.VISIBLE);
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        } else {
//            binding.progressCircular.setVisibility(View.GONE);
//        }
//    }
//
//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//
//            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
//            //Toast.makeText(Google.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
//            FirebaseGoogleAuth(acc);
//        } catch (ApiException e) {
//            Toast.makeText(LoginWithGoogleActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
//            FirebaseGoogleAuth(null);
//            binding.progressCircular.setVisibility(View.GONE);
//        }
//    }
//
//    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
//        //check if the account is null
//        if (acct != null) {
//            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        //Toast.makeText(Google.this, "Successful", Toast.LENGTH_SHORT).show();
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        updateUI(user);
//                    } else {
//                        Toast.makeText(LoginWithGoogleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                        updateUI(null);
//                        binding.progressCircular.setVisibility(View.GONE);
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(LoginWithGoogleActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
//            binding.progressCircular.setVisibility(View.GONE);
//        }
//    }
//
//    private void updateUI(FirebaseUser fUser) {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        if (account != null) {
//            Log.d(TAG, String.valueOf(account));
//            Log.d(TAG, account.getDisplayName());//GMAILNAME
//            Log.d(TAG, account.getGivenName());
//            Log.d(TAG, account.getFamilyName());
//            Log.d(TAG, account.getEmail());
//            Log.d(TAG, account.getId());
//            Log.d(TAG, String.valueOf(account.getPhotoUrl()));
//            binding.progressCircular.setVisibility(View.GONE);
//            Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show();
//        } else {
//            binding.progressCircular.setVisibility(View.GONE);
//        }
//    }
//    //////////////////////////////////////////////////////////////////////////////////////////////
}