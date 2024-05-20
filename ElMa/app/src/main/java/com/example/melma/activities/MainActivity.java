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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button singin, singup;
    EditText log, pass;
    ImageView eyeIcon;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        log = findViewById(R.id.login);
        pass = findViewById(R.id.password);
        singin = findViewById(R.id.singIn);
        singup = findViewById(R.id.singUp);
        eyeIcon = findViewById(R.id.eye_icon);

        // Установка начального цвета фильтра для иконки глаза (серый)
        eyeIcon.setColorFilter(getResources().getColor(android.R.color.darker_gray));

        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Скрыть пароль
                    pass.setInputType(0x00000081); // Тип для скрытого пароля
                    eyeIcon.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                } else {
                    // Показать пароль
                    pass.setInputType(0x00000091); // Тип для видимого пароля
                    eyeIcon.setColorFilter(null); // Убрать фильтр
                }
                isPasswordVisible = !isPasswordVisible;
                pass.setSelection(pass.getText().length()); // Установить курсор в конец текста
            }
        });

        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Создание URL для запроса
                            String login = log.getText().toString();
                            String password = pass.getText().toString();
                            String urlString = "http://194.146.242.26:7777/api/ForAllUsers/login?login=" + login + "&password=" + password;
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

                                // Обработка ответа
                                String jsonResponse = response.toString();
                                JSONObject responseObject = new JSONObject(jsonResponse);

                                String token = responseObject.getJSONObject("loginResponse").getString("token");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Открытие новой активити с передачей токена
                                        Intent intent = new Intent(MainActivity.this, BookActiviry.class);
                                        intent.putExtra("token", token);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                // Обработка ошибки
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            // Закрытие соединения
                            connection.disconnect();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SingUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
