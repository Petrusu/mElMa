package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class ProfileActivity extends AppCompatActivity {

    String token;
    ImageButton homeBtn, favoriteBtn;
    Button chgLogin, chgPass, chgEmail, exitBtn;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        loginText = findViewById(R.id.profileLogin);
        getLogin(loginText);

        favoriteBtn = findViewById(R.id.favoritesButton);
        homeBtn = findViewById(R.id.homeButton);
        chgLogin = findViewById(R.id.changeLoginButton);
        chgPass = findViewById(R.id.changePasswordButton);
        chgEmail = findViewById(R.id.changeEmailButton);
        exitBtn = findViewById(R.id.ExitButton);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(exitIntent);
            }
        });

        favoriteBtn.setOnClickListener(v -> {
            Intent favIntent = new Intent(ProfileActivity.this, FavoriteActivity.class);
            favIntent.putExtra("token", token);
            startActivity(favIntent);
        });

        homeBtn.setOnClickListener(v -> {
            Intent homeIntent = new Intent(ProfileActivity.this, BookActiviry.class);
            homeIntent.putExtra("token", token);
            startActivity(homeIntent);
        });

        chgLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(ProfileActivity.this, ChangeLoginActivity.class);
            loginIntent.putExtra("token", token);
            startActivity(loginIntent);
        });

        chgPass.setOnClickListener(v -> {
            Intent passIntent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            passIntent.putExtra("token", token);
            startActivity(passIntent);
        });

        chgEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(ProfileActivity.this, ChangeEmailActivity.class);
            emailIntent.putExtra("token", token);
            startActivity(emailIntent);
        });
    }

    private void getLogin(TextView loginTextView) {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<String> call = api.GetUserLogin("Bearer " + token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String login = response.body();
                    loginTextView.setText(login);
                } else {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
               Toast.makeText(getApplicationContext(),  throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
