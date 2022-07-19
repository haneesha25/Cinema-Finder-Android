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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etname, etemail, etphone, etcpassword, etpassword;
    Button register;
    TextView txtlogin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    String text = "<font color=#000>Have an account?   </font> <font color=##f65c5c>Sign Up</font>";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore dbroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname = findViewById(R.id.name);
        etemail = findViewById(R.id.email);
        etphone = findViewById(R.id.phone);
        etcpassword = findViewById(R.id.cpassword);
        etpassword = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txtlogin = findViewById(R.id.txtlogin);

        txtlogin.setText(Html.fromHtml(text));

        firebaseAuth = FirebaseAuth.getInstance();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dbroot = FirebaseFirestore.getInstance();

        register.setOnClickListener(view -> {
            createuser();
        });

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
    private void createuser(){
        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String phone = etphone.getText().toString();

        String cpassword = etcpassword.getText().toString();
        String password = etpassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            etname.setError("Name can not be empty");
            etname.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            etemail.setError("Email can not be empty");
            etemail.requestFocus();
        }
        else if(!email.trim().matches(emailPattern)){
            etemail.setError("Email format is not proper");
            etemail.requestFocus();
        }
        else if(TextUtils.isEmpty(phone)){
            etphone.setError("Phone Number can not be empty");
            etphone.requestFocus();
        }
        else if(!phone.matches(pattern)){
            etphone.setError("Phone Number is not valid");
            etphone.requestFocus();
        }
        else if(TextUtils.isEmpty(cpassword)){
            etcpassword.setError("Create Password can not be empty");
            etcpassword.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            etpassword.setError("Password can not be empty");
            etpassword.requestFocus();
        }
        else if (!password.equals(cpassword)){
            etpassword.setError("Password is not matched");
            etpassword.requestFocus();
        }else{
            Map<String,String> items = new HashMap<>();
            items.put("fullName",name);
            items.put("email",email);
            items.put("contactNumber",phone);
            items.put("password",password);

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        dbroot.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(items);
                        Toast.makeText(RegisterActivity.this, "User registered sucessfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Registration Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}