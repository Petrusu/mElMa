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

public class VerifyCodeActivity extends AppCompatActivity {

    EditText number1, number2, number3, number4;
    Button chekBtn;
    int combinedNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        number1 = findViewById(R.id.digit1);
        number2 = findViewById(R.id.digit2);
        number3 = findViewById(R.id.digit3);
        number4 = findViewById(R.id.digit4);
        chekBtn = findViewById(R.id.chek);

        chekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String text1 = number1.getText().toString();
                            String text2 = number2.getText().toString();
                            String text3 = number3.getText().toString();
                            String text4 = number4.getText().toString();

                            String combinedText = text1 + text2 + text3 + text4;

                            combinedNumber = Integer.parseInt(combinedText);

                            String urlString = "http://194.146.242.26:7777/api/ForAllUsers/verify_email?_code=" + combinedText;
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
                                        // Открытие новой активити с передачей токена
                                        Intent intent = new Intent(VerifyCodeActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                // Обработка ошибки
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VerifyCodeActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            // Закрытие соединения
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}