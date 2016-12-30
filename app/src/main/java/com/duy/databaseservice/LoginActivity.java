package com.duy.databaseservice;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.databaseservice.data.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignup, btnLogin, btnReset;
    private CheckBox swAutoLogin, swSavePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isAutoLogin = Preferences.getBoolean(this, Preferences.AUTO_LOGIN);
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
//        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
//        btnReset = (Button) findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();
       // autoLogin();
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//            }
//        });
//
//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
//            }
//        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog(getString(R.string.logining), false);

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                hideProgress();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });

            }
        });
        swAutoLogin = (CheckBox) findViewById(R.id.sw_auto_login);
        swAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences.putBoolean(LoginActivity.this, Preferences.AUTO_LOGIN, b);
            }
        });

        swSavePassword = (CheckBox) findViewById(R.id.sw_remember_pass);
        swSavePassword.setChecked(Preferences.getBoolean(this, Preferences.REMEMBER_PASS));
        swSavePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Preferences.putBoolean(LoginActivity.this, Preferences.REMEMBER_PASS, b);
            }
        });

        inputEmail.setText(Preferences.getString(this, Preferences.EMAIL));
        if (Preferences.getBoolean(this, Preferences.REMEMBER_PASS)) {
            inputPassword.setText(Preferences.getString(this, Preferences.PASSWORD));
        }
        swAutoLogin.setChecked(Preferences.getBoolean(this, Preferences.AUTO_LOGIN));

        if (isAutoLogin) {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, R.string.enter_email_address, Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressDialog(getString(R.string.logining), false);
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgress();
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    inputPassword.setError(getString(R.string.minimum_password));
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
        }
    }

    private void autoLogin() {
        if (auth != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Preferences.putString(this, Preferences.EMAIL, inputEmail.getText().toString());
        Preferences.putString(this, Preferences.PASSWORD, inputPassword.getText().toString());
        Preferences.putString(this, Preferences.AUTO_LOGIN, swAutoLogin.getText().toString());
        hideProgress();
    }
}