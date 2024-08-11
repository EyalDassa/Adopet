package com.example.adopet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adopet.Utilities.DbManager;
import com.example.adopet.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class SignInActivity extends AppCompatActivity {


    private Button signup_BTN_signup;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        findViews();
        signup_BTN_signup.setOnClickListener(v -> login());

    }

    private void login() {
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()
                ))
                .build();

        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {

            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DbManager.checkNewUser(uid, res -> {
                if (res) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    SignInActivity.this.startActivity(intent);
                    SignInActivity.this.finish();
                } else {
                    Bundle bundle = new Bundle();
                    String email = user.getEmail();
                    String phone = user.getPhoneNumber();
                    String name = user.getDisplayName();
                    bundle.putString("EMAIL", email);
                    bundle.putString("PHONE", phone);
                    bundle.putString("NAME", name);
                    Intent intent = new Intent(SignInActivity.this, DetailsActivity.class);
                    intent.putExtras(bundle);
                    SignInActivity.this.startActivity(intent);
                    SignInActivity.this.finish();
                }
            });

        } else if (response == null) {
            Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
        } else {
            int errorCode = response.getError().getErrorCode();
            switch (errorCode) {
                case ErrorCodes.NO_NETWORK:
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    break;
                case ErrorCodes.UNKNOWN_ERROR:
                    Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void findViews() {
        signup_BTN_signup = findViewById(R.id.signup_BTN_signup);
    }
}