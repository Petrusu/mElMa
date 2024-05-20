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

import models.Editor;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditorActivity extends AppCompatActivity {
    ListView editorsListView;
    ArrayList<String> editors;
    ArrayList<String> selectedEditors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editorsListView = findViewById(R.id.editorsListView);
        selectedEditors = new ArrayList<>();

        Button applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(v -> applySelection());

        fetchEditorsFromAPI();
    }

    private void fetchEditorsFromAPI() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<ArrayList<Editor>> call = api.GetEditors();

        call.enqueue(new Callback<ArrayList<Editor>>() {
            @Override
            public void onResponse(Call<ArrayList<Editor>> call, Response<ArrayList<Editor>> response) {
                if (response.isSuccessful()) {
                    editors = new ArrayList<>();
                    for (Editor editor : response.body()) {
                        editors.add(editor.getEditorname());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditorActivity.this, R.layout.editor_list_item, R.id.editor_name, editors);
                    editorsListView.setAdapter(adapter);
                    editorsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                } else {
                    Toast.makeText(EditorActivity.this, "Не удалось загрузить редакторов.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Editor>> call, Throwable t) {
                Toast.makeText(EditorActivity.this, "Не удалось загрузить редакторов.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applySelection() {
        for (int i = 0; i < editorsListView.getCount(); i++) {
            if (editorsListView.isItemChecked(i)) {
                selectedEditors.add(editors.get(i));
            }
        }
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedEditors", selectedEditors);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
