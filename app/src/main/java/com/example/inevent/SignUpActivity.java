package com.example.inevent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText mPassword;
    EditText mEmail;
    EditText mName;
    EditText mConfirmPassword;
    EditText mLocation;
    EditText mPhone;

    Button mSignUp;
    TextView mLogIn;

    public ProgressDialog dialog;

    DatabaseReference databaseUser;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        mName = findViewById(R.id.etName);
        mEmail = findViewById(R.id.etEmailSignUp);
        mPassword = findViewById(R.id.etPasswordSignUp);
        mConfirmPassword = findViewById(R.id.etConfirmPasswordSignUp);
        mLocation = findViewById(R.id.etLocation);
        mPhone = findViewById(R.id.etPhone);

        mSignUp = findViewById(R.id.btSignUp);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                createAccount(email,password);
            }
        });

        mLogIn = findViewById(R.id.tvGoToSignIn);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void click(View v){
        Intent intent;

        switch(v.getId()){
            case R.id.tvGoToSignIn:
                intent = new Intent(this,LogInActivity.class);
                startActivity(intent);
        }
    }

    private void createAccount(String email, String password) {
        //Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(SignUpActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                            addUser();
                            sendEmailVerification();
                            startActivity(new Intent(SignUpActivity.this, EmailVerificationActivity.class));
                            finish();
                        } else {
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }

    private void sendEmailVerification() {

        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Required.");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        String confirmPassword = mConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError("Required.");
            valid = false;
        } else {
            mConfirmPassword.setError(null);
        }

        if(!(password.equals(confirmPassword))){
            valid = false;
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    public void addUser(){
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String permission = "member";
        String city = mLocation.getText().toString();
        String phone = mPhone.getText().toString();

        String id = databaseUser.push().getKey();
        User user = new User(email,name,permission,city,phone);
        databaseUser.child(id).setValue(user);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
