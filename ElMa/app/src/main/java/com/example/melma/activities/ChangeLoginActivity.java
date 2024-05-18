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

import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeLoginActivity extends AppCompatActivity {

    String token;
    EditText oldLogin, newLogin;
    Button change, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        oldLogin = findViewById(R.id.current_login);
        newLogin = findViewById(R.id.new_login);
        change = findViewById(R.id.change_login_button);
        back = findViewById(R.id.back_btn);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!oldLogin.getText().toString().equals(newLogin.getText().toString())){
                    ChangeLogin();
                }
                else {
                    //если пароли не совпадают
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangeLoginActivity.this, "Логины совпадают! Повторите попытку.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profIntent = new Intent(ChangeLoginActivity.this, ProfileActivity.class);
                profIntent.putExtra("token", token);
                startActivity(profIntent);
            }
        });
    }
    private void ChangeLogin() {
        // Получаем экземпляр Retrofit
        Retrofit retrofit = ElMaAPI.getClient();
        // Создаем реализацию API интерфейса
        APIInterface api = retrofit.create(APIInterface.class);

        String login = newLogin.getText().toString();
        // Создаем вызов для изменения пароля
        Call<Void> call = api.ChangeLogin(login, "Bearer " + token);

        // Асинхронный вызов
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Уведомление пользователя об успешной смене пароля
                    Toast.makeText(getApplicationContext(), "Логин успешно изменен", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Уведомление пользователя об ошибке при изменении пароля
                    Toast.makeText(getApplicationContext(), "Ошибка при изменении логина: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                // Уведомление пользователя об ошибке сети
                Toast.makeText(getApplicationContext(), "Ошибка сети: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}