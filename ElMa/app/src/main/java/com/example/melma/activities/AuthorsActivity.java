package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.melma.R;

import java.util.ArrayList;

import models.Author;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthorsActivity extends AppCompatActivity {
    ListView authorsListView;
    ArrayList<String> authors;
    ArrayList<String> selectedAuthors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authorsListView = findViewById(R.id.authorsListView);
        selectedAuthors = new ArrayList<>();

        Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(v -> applySelection());

        fetchAuthorsFromAPI();
    }

    private void fetchAuthorsFromAPI() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<ArrayList<Author>> call = api.GetAuthors();

        call.enqueue(new Callback<ArrayList<Author>>() {
            @Override
            public void onResponse(Call<ArrayList<Author>> call, Response<ArrayList<Author>> response) {
                if (response.isSuccessful()) {
                    authors = new ArrayList<>();
                    for (Author author : response.body()) {
                        authors.add(author.getAuthorsname());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AuthorsActivity.this, R.layout.author_list_item, R.id.author_name, authors);
                    authorsListView.setAdapter(adapter);
                    authorsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } else {
                    Toast.makeText(AuthorsActivity.this, "Не удалось получить авторов.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Author>> call, Throwable t) {
                Toast.makeText(AuthorsActivity.this, "Не удалось получить авторов.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applySelection() {
        for (int i = 0; i < authorsListView.getCount(); i++) {
            if (authorsListView.isItemChecked(i)) {
                selectedAuthors.add(authors.get(i));
            }
        }
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedAuthors", selectedAuthors);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
