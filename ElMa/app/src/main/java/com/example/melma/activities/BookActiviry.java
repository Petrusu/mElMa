package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Book;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.melma.more.BookGridAdapter;
import com.example.melma.more.BookInfoDialog;
import com.example.melma.R;

import java.util.ArrayList;
import java.util.List;

public class BookActiviry extends AppCompatActivity{
    ArrayList<Book> books;
    ImageButton filtrermenu, favoriteBtn, profileBtn;
    String token;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_activiry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        filtrermenu = findViewById(R.id.filterbtn);
        favoriteBtn = findViewById(R.id.favoritesButton);
        profileBtn = findViewById(R.id.profileButton);

        filtrermenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.filterbtn){
                    showPopupMenu(v);
                }
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActiviry.this, FavoriteActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActiviry.this, ProfileActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        gridView = findViewById(R.id.bookGrid);
        getAllBooksFromAPI();
    }
    private void getAllBooksFromAPI() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<ArrayList<Book>> call = api.GetBooks();

        call.enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                if(response.isSuccessful()){
                    books = response.body(); // Сохраняем список книг
                    fillBookGrid(books);
                    setGridViewClickListener(); // Устанавливаем обработчик нажатия на элемент списка
                } else {
                    // Обработка ошибки при неудачном ответе от сервера
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                // Обработка ошибки при неудачном выполнении запроса
            }
        });
    }
    private void fillBookGrid(List<Book> books) {
        BookGridAdapter adapter = new BookGridAdapter(this, books);
        gridView.setAdapter(adapter);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.autors) {  //Выбор авторов
                            return true;
                        }else if(item.getItemId() == R.id.catalog){
                            return true;
                        }else if(item.getItemId() == R.id.editors){
                            return true;
                        }else if(item.getItemId() == R.id.themes){
                            return true;
                        }
                        return false;
                    }
                });
        popupMenu.show();
    }
    private void setGridViewClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (books != null && position < books.size()) {
                    Book selectedBook = books.get(position);
                    int bookId = selectedBook.getBookId();
                    showBookInfoDialog(bookId);
                }
            }
        });
    }
    private void showBookInfoDialog(int bookId) {
        BookInfoDialog dialog = new BookInfoDialog(this, bookId, token);
        dialog.show();
    }
}