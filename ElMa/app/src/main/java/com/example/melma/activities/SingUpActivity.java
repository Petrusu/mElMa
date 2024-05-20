package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.melma.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SingUpActivity extends AppCompatActivity {
    Button singup;
    EditText email, log, pass, passAgain;
    ImageView eyeIconPassword, eyeIconPasswordAgain;
    boolean isPasswordVisible = false;
    boolean isPasswordAgainVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        log = findViewById(R.id.login);
        pass = findViewById(R.id.password);
        passAgain = findViewById(R.id.passwordAgain);
        singup = findViewById(R.id.singUp);
        eyeIconPassword = findViewById(R.id.eye_icon_password);
        eyeIconPasswordAgain = findViewById(R.id.eye_icon_password_again);

        // Установка начального цвета фильтра для иконки глаза (серый)
        eyeIconPassword.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        eyeIconPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    pass.setInputType(0x00000081); // Скрыть пароль
                    eyeIconPassword.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                } else {
                    pass.setInputType(0x00000091); // Показать пароль
                    eyeIconPassword.setColorFilter(null);
                }
                isPasswordVisible = !isPasswordVisible;
                pass.setSelection(pass.getText().length());
            }
        });

        // Установка начального цвета фильтра для иконки глаза (серый)
        eyeIconPasswordAgain.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        eyeIconPasswordAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordAgainVisible) {
                    passAgain.setInputType(0x00000081); // Скрыть пароль
                    eyeIconPasswordAgain.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                } else {
                    passAgain.setInputType(0x00000091); // Показать пароль
                    eyeIconPasswordAgain.setColorFilter(null);
                }
                isPasswordAgainVisible = !isPasswordAgainVisible;
                passAgain.setSelection(passAgain.getText().length());
            }
        });

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String login = log.getText().toString();
                            String password = pass.getText().toString();
                            String Email = email.getText().toString();

                            if(pass.getText().toString().equals(passAgain.getText().toString())){
                                String urlString = "http://194.146.242.26:7777/api/ForAllUsers/registration?login=" + login + "&email=" + Email + "&password=" + password;
                                URL url = new URL(urlString);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    InputStream inputStream = connection.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        response.append(line);
                                    }
                                    reader.close();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(SingUpActivity.this, VerifyCodeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SingUpActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    connection.disconnect();
                                }
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SingUpActivity.this, "Пароли не совпадают! Повторите попытку.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
