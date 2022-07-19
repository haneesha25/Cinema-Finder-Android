package com.example.cinemafinderandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etloginemail;
    EditText etloginpassword;
    Button login;
    TextView etregister, forgotpassword;
    FirebaseAuth firebaseAuth;
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

        etregister.setText(Html.fromHtml(text));

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


            }
        });

        etregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


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
                        if(useremail.equals("admin@gmail.com")){
                            startActivity(new Intent(LoginActivity.this,AdminActivity.class));
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
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