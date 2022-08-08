package com.example.cinemafinderandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


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

public class LoginActivity extends AppCompatActivity {

    EditText etloginemail;
    EditText etloginpassword;
    Button login;
    TextView etregister, forgotpassword;
    FirebaseAuth firebaseAuth;
    ImageView googlelogo;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    String text = "<font color=#000> Don't have an account? </font> <font color=#f65c5c> Sign up </font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etloginemail = findViewById(R.id.loginemail);
        etloginpassword = findViewById(R.id.loginpassword);
        login = findViewById(R.id.login);
        etregister = findViewById(R.id.txtregister);
        forgotpassword = findViewById(R.id.forgotpassword);
        googlelogo = findViewById(R.id.googlelogin);

        etregister.setText(Html.fromHtml(text));

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        firebaseAuth = FirebaseAuth.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        login.setOnClickListener(view -> {
            Loginuser();
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etloginemail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    etloginemail.setError("Email can not be empty");
                    etloginemail.requestFocus();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(LoginActivity.this, "password reset link sent successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        etregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        googlelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlesignIn();
            }
        });

    }

    private void googlesignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

                /*SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = googleCredential.getGoogleIdToken();*/

                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                //  AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(LoginActivity.this, "User Loggedin", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.


                                }
                            }
                        });

                finish();
                Intent i = new Intent(LoginActivity.this,AdminHome.class);
                startActivity(i);
            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void Loginuser(){
        String email = etloginemail.getText().toString();
        String password = etloginpassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            etloginemail.setError("Email can not be empty");
            etloginemail.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            etloginpassword.setError("Password can not be empty");
            etloginpassword.requestFocus();
        }

        else{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User Loggedin successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String useremail = user.getEmail().toString();
                        if(useremail.equals("admin@gmail.com")|| useremail.equals("dhruviadmin@gmail.com")){
                            startActivity(new Intent(LoginActivity.this,AdminHome.class));
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this, ListMovies.class));
                        }

                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Login Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}