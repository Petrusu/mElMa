package com.example.melma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import models.Book;
import models.BookPageResponse;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.melma.more.BookGridAdapter;
import com.example.melma.more.BookInfoDialog;
import com.example.melma.R;
import com.example.melma.more.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

public class BookActiviry extends AppCompatActivity {
    private ArrayList<Book> books = new ArrayList<>();
    private BookGridAdapter adapter;
    private ImageButton filterMenuButton, favoriteBtn, profileBtn;
    private String token;
    private GridView gridView;
    private ArrayList<String> selectedAuthors = new ArrayList<>();
    private ArrayList<String> selectedEditors = new ArrayList<>();
    private ArrayList<String> selectedThemes = new ArrayList<>();

    private String currentSearchQuery = "";
    private EditText searchInput;
    private Button resetButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

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

        filterMenuButton = findViewById(R.id.filterbtn);
        favoriteBtn = findViewById(R.id.favoritesButton);
        profileBtn = findViewById(R.id.profileButton);
        resetButton = findViewById(R.id.resetButton);
        searchInput = findViewById(R.id.searchInput);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);
        gridView = findViewById(R.id.bookGrid);

        adapter = new BookGridAdapter(this, books);
        gridView.setAdapter(adapter);

        filterMenuButton.setOnClickListener(this::showPopupMenu);
        favoriteBtn.setOnClickListener(v -> startActivity(new Intent(BookActiviry.this, FavoriteActivity.class).putExtra("token", token)));
        profileBtn.setOnClickListener(v -> startActivity(new Intent(BookActiviry.this, ProfileActivity.class).putExtra("token", token)));
        resetButton.setOnClickListener(v -> resetFilters());

        swipeRefreshLayout.setOnRefreshListener(this::refreshBooks);

        gridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                if (!isLastPage) {
                    loadBooks(currentPage, 20);  // 20 - количество книг на странице
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = (Book) adapter.getItem(position);
                showBookInfoDialog(selectedBook);
            }
        });

        loadBooks(currentPage, 20);  // Загрузка первой страницы с 20 книгами

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                filterBooks();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadBooks(int page, int size) {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);
        Call<BookPageResponse> call = api.getBooks(page, size);

        call.enqueue(new Callback<BookPageResponse>() {
            @Override
            public void onResponse(Call<BookPageResponse> call, Response<BookPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookPageResponse pageResponse = response.body();
                    if (page == 1) {
                        books.clear();
                    }
                    books.addAll(pageResponse.getBooks());
                    adapter.notifyDataSetChanged();
                    currentPage++;
                    isLastPage = currentPage > pageResponse.getTotalPages();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BookPageResponse> call, Throwable t) {
                isLoading = false;
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshBooks() {
        currentPage = 1;
        isLastPage = false;
        loadBooks(currentPage, 20);
    }

    private void resetFilters() {
        selectedAuthors.clear();
        selectedEditors.clear();
        selectedThemes.clear();
        currentSearchQuery = "";
        searchInput.setText("");
        refreshBooks();
    }

    private void filterBooks() {
        String[] keywords = currentSearchQuery.toLowerCase().split("\\s+");
        ArrayList<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            boolean matchesSearch = true;
            for (String keyword : keywords) {
                boolean keywordMatches = book.getTitle().toLowerCase().contains(keyword) ||
                        (book.getBbk() != null && book.getBbk().toLowerCase().contains(keyword)) ||
                        (book.getAuthors() != null && book.getAuthors().stream().anyMatch(author -> author.toLowerCase().contains(keyword))) ||
                        (book.getAuthorBook() != null && book.getAuthorBook().stream().anyMatch(author -> author.toLowerCase().contains(keyword))) ||
                        (book.getThemesName() != null && book.getThemesName().stream().anyMatch(theme -> theme.toLowerCase().contains(keyword)));
                if (!keywordMatches) {
                    matchesSearch = false;
                    break;
                }
            }
            boolean matchesAuthors = selectedAuthors.isEmpty() ||
                    (book.getAuthors() != null && selectedAuthors.stream().anyMatch(author -> book.getAuthors().contains(author)));

            boolean matchesEditors = selectedEditors.isEmpty() ||
                    (book.getEditors() != null && selectedEditors.stream().anyMatch(editor -> book.getEditors().contains(editor)));

            boolean matchesThemes = selectedThemes.isEmpty() ||
                    (book.getThemesName() != null && selectedThemes.stream().anyMatch(theme -> book.getThemesName().contains(theme)));

            if (matchesSearch && matchesAuthors && matchesEditors && matchesThemes) {
                filteredBooks.add(book);
            }
        }
        fillBookGrid(filteredBooks);
    }

    private void fillBookGrid(List<Book> books) {
        adapter.updateBooks(books);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.autors) {  // Выбор авторов
                Intent authorsIntent = new Intent(BookActiviry.this, AuthorsActivity.class);
                startActivityForResult(authorsIntent, 1);
                return true;
            } else if (item.getItemId() == R.id.editors) {  // Выбор редакторов
                Intent editorsIntent = new Intent(BookActiviry.this, EditorActivity.class);
                startActivityForResult(editorsIntent, 2);
                return true;
            } else if (item.getItemId() == R.id.themes) {  // Выбор тем
                Intent themesIntent = new Intent(BookActiviry.this, ThemesActivity.class);
                startActivityForResult(themesIntent, 3);
                return true;
            } else if (item.getItemId() == R.id.catalog) {
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    selectedAuthors = data.getStringArrayListExtra("selectedAuthors");
                    break;
                case 2:
                    selectedEditors = data.getStringArrayListExtra("selectedEditors");
                    break;
                case 3:
                    selectedThemes = data.getStringArrayListExtra("selectedThemes");
                    break;
            }
            filterBooks();
        }
    }

    private void showBookInfoDialog(Book book) {
        BookInfoDialog dialog = new BookInfoDialog(this, book.getBookId(), token);
        dialog.show();
    }
}
