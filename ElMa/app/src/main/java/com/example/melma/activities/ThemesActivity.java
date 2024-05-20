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

import models.Theme;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ThemesActivity extends AppCompatActivity {
    ListView themesListView;
    ArrayList<String> themes;
    ArrayList<String> selectedThemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_themes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        themesListView = findViewById(R.id.themesListView);
        selectedThemes = new ArrayList<>();

        Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(v -> applySelection());

        fetchThemesFromAPI();
    }

    private void fetchThemesFromAPI() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<ArrayList<Theme>> call = api.GetThemes();

        call.enqueue(new Callback<ArrayList<Theme>>() {
            @Override
            public void onResponse(Call<ArrayList<Theme>> call, Response<ArrayList<Theme>> response) {
                if (response.isSuccessful()) {
                    themes = new ArrayList<>();
                    for (Theme theme : response.body()) {
                        themes.add(theme.getThemesname());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ThemesActivity.this, R.layout.theme_list_item, R.id.theme_name, themes);
                    themesListView.setAdapter(adapter);
                    themesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } else {
                    Toast.makeText(ThemesActivity.this, "Не удалось загрузить темы.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Theme>> call, Throwable t) {
                Toast.makeText(ThemesActivity.this, "Не удалось загрузить темы.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applySelection() {
        for (int i = 0; i < themesListView.getCount(); i++) {
            if (themesListView.isItemChecked(i)) {
                selectedThemes.add(themes.get(i));
            }
        }
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedThemes", selectedThemes);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
