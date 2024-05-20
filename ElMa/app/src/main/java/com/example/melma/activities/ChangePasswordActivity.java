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

import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {

    String token;
    EditText oldPass, newPass, againNewPass;
    Button change, back;
    ImageView eyeIconCurrent, eyeIconNew, eyeIconRepeat;
    boolean isCurrentPasswordVisible = false;
    boolean isNewPasswordVisible = false;
    boolean isRepeatPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        oldPass = findViewById(R.id.current_password);
        newPass = findViewById(R.id.new_password);
        againNewPass = findViewById(R.id.repeat_password);
        change = findViewById(R.id.change_password_button);
        back = findViewById(R.id.back_btn);

        eyeIconCurrent = findViewById(R.id.eye_icon_current);
        eyeIconNew = findViewById(R.id.eye_icon_new);
        eyeIconRepeat = findViewById(R.id.eye_icon_repeat);
        // Установка начального цвета фильтра для иконки глаза (серый)
        eyeIconCurrent.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        eyeIconCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentPasswordVisible) {
                    oldPass.setInputType(0x00000081); // Скрыть пароль
                    eyeIconCurrent.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                } else {
                    oldPass.setInputType(0x00000091); // Показать пароль
                    eyeIconCurrent.setColorFilter(null);
                }
                isCurrentPasswordVisible = !isCurrentPasswordVisible;
                oldPass.setSelection(oldPass.getText().length());
            }
        });

        // Установка начального цвета фильтра для иконки глаза (серый)
        eyeIconNew.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        eyeIconNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewPasswordVisible) {
                    newPass.setInputType(0x00000081); // Скрыть пароль
                    eyeIconNew.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                } else {
                    newPass.setInputType(0x00000091); // Показать пароль
                    eyeIconNew.setColorFilter(null);
                }
                isNewPasswordVisible = !isNewPasswordVisible;
                newPass.setSelection(newPass.getText().length());
            }
        });

        // Установка начального цвета фильтра для иконки глаза (серый)
        eyeIconRepeat.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        eyeIconRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRepeatPasswordVisible) {
                    againNewPass.setInputType(0x00000081); // Скрыть пароль
                    eyeIconRepeat.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                } else {
                    againNewPass.setInputType(0x00000091); // Показать пароль
                    eyeIconRepeat.setColorFilter(null);
                }
                isRepeatPasswordVisible = !isRepeatPasswordVisible;
                againNewPass.setSelection(againNewPass.getText().length());
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getText().toString().equals(againNewPass.getText().toString())){
                    ChangePass();
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this, "Пароли не совпадают! Повторите попытку.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profIntent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                profIntent.putExtra("token", token);
                startActivity(profIntent);
            }
        });
    }

    private void ChangePass() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);
        String pass = newPass.getText().toString();
        Call<Void> call = api.ChangePass(pass, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка при изменении пароля: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Ошибка сети: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
