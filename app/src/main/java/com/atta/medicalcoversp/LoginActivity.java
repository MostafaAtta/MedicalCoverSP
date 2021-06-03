package com.atta.medicalcoversp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn;

    TextInputEditText emailText, passwordText;

    String email, password;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private static final String TAG = "LoginFragment";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailText = findViewById(R.id.loginEmail);
        passwordText = findViewById(R.id.loginPass);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginBtn = findViewById(R.id.button_login);
        loginBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == loginBtn){
            email = emailText.getText().toString().trim();
            password = passwordText.getText().toString().trim();

            if(!verify()){
                return;
            }
            checkUser();

        }

    }


    public boolean verify(){
        boolean valid = true;

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty() || email.equals("")){
            emailText.setError("Enter you Email");
            valid = false;
        }else if (!email.matches(emailPattern)){
            emailText.setError("Enter a valid Email");
            valid = false;
        } else if(password.isEmpty() || password.equals("")){
            passwordText.setError("Enter you Password");
            valid = false;
        } else if(password.length() < 6 || password.length() >10){
            passwordText.setError("password must be between 6 and 10 alphanumeric characters");
            valid = false;
        }

        return valid;
    }

    private void checkUser(){
        db.collection("Users").whereEqualTo("email", email).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            user = documentSnapshot.toObject(User.class);
                            user.setId(documentSnapshot.getId());
                        }

                        if (user.getType() == 0 || user.getType() == 1 ||
                                user.getType() == 2 || user.getType() == 3
                                || user.getType() == 4) {
                            login();
                        } else {
                            Toast.makeText(LoginActivity.this, "You are not authorized", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(LoginActivity.this, "User not found, Please Register", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show());

    }

    public void login(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            SessionManager.getInstance(LoginActivity.this).login(user);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // ...
                        }

                        // ...
                    }
                });

    }
}