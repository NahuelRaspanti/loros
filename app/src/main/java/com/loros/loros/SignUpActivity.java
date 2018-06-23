package com.loros.loros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SignUpActivity extends AppCompatActivity{

    @BindView(R.id.email) EditText inputEmail;
    @BindView(R.id.password) EditText inputPassword;
    @BindView(R.id.name) EditText inputName;
    @BindView(R.id.email_text_input) TextInputLayout mEmail;
    @BindView(R.id.name_text_input) TextInputLayout mName;
    @BindView (R.id.password_text_input) TextInputLayout mPassword;
    @BindView(R.id.sign_in_button) Button btnSignIn;
    @BindView(R.id.sign_up_button) Button btnSignUp;
    @BindView(R.id.btn_reset_password) Button btnResetPassword;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private FirebaseAuth auth;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
        ButterKnife.bind(this);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, RecoverPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String name = inputName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("INGRESÁ UN EMAIL!");
                    //Toast.makeText(getApplicationContext(), "INGRESÁ UN EMAIL!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    mName.setError("INGRESÁ UN NOMBRE!");
                    //Toast.makeText(getApplicationContext(), "INGRESÁ UN NOMBRE!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("INGRESÁ UNA CONTRASEÑA!");
                    //Toast.makeText(getApplicationContext(), "INGRESÁ UNA CONTRASEÑA!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.length() > 15) {
                    Toast.makeText(getApplicationContext(), "EL NOMBRE DEBE TENER MENOS DE 15 CARACTERES!", Toast.LENGTH_SHORT).show();
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "CONTRASEÑA DEMASIADO CORTA, INGRESÁ UN MÍNIMO DE 6 CARACTERES", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "USUARIO CREADO CON ÉXITO", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "FALLÓ EL REGISTRO." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference usersRef = database.child("users/" + auth.getUid());
                                    usersRef.setValue(new User(name, "student", email));
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @OnTextChanged(value = R.id.email, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void resetEmailField(Editable e) {
        mEmail.setError(null);
    }

    @OnTextChanged(value = R.id.name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void resetNameField(Editable e) {
        mName.setError(null);
    }

    @OnTextChanged(value = R.id.password, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void resetPasswordField(Editable e) {
        mPassword.setError(null);
    }




}
