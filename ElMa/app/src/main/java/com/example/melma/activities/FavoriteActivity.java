package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melma.more.BookInfoDialog;
import com.example.melma.more.FavoriteAdapter;
import com.example.melma.R;

import java.util.ArrayList;
import java.util.List;

import models.Favorite;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoriteActivity extends AppCompatActivity {

    String token;
    ArrayList<Favorite> favorites;
    RecyclerView recyclerView;
    ImageButton homeBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        homeBtn = findViewById(R.id.homeButton);
        profileBtn = findViewById(R.id.profileButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, BookActiviry.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        getFavoriteFromAPI();
    }

    private void getFavoriteFromAPI() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<ArrayList<Favorite>> call = api.Favorite("Bearer " + token);

        call.enqueue(new Callback<ArrayList<Favorite>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                if (response.isSuccessful()) {
                    favorites = response.body();
                    fillFavorite(favorites);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorite>> call, Throwable throwable) {
                // Обработка ошибок
            }
        });
    }

    private void fillFavorite(List<Favorite> favorites) {
        FavoriteAdapter adapter = new FavoriteAdapter(this, favorites, new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Favorite favorite) {
                int bookId = favorite.getIdBook();
                showBookInfoDialog(bookId);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showBookInfoDialog(int bookId) {
        BookInfoDialog dialog = new BookInfoDialog(this, bookId, token);
        dialog.show();
    }
    public void updateFavorites() {
        getFavoriteFromAPI();
    }
}
