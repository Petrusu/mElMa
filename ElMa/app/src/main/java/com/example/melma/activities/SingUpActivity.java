package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Создание URL для запроса
                            String login = log.getText().toString();
                            String password = pass.getText().toString();
                            String Email = email.getText().toString();

                            if(pass.getText().toString().equals(passAgain.getText().toString())){

                                String urlString = "http://194.146.242.26:7777/api/ForAllUsers/registration?login=" + login + "&email=" + Email + "&password=" + password;
                                URL url = new URL(urlString);

                                // Создание соединения
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                // Установка метода запроса
                                connection.setRequestMethod("POST");

                                // Получение ответа от сервера
                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    // Чтение ответа от сервера
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
                                            // Открытие новой активити
                                            Intent intent = new Intent(SingUpActivity.this, VerifyCodeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    // Обработка ошибки
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SingUpActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    // Закрытие соединения
                                    connection.disconnect();
                                }
                            }
                            else {
                                //если пароли не совпадают
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