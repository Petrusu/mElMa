package com.example.melma.more;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.melma.R;
import com.example.melma.activities.FavoriteActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import models.Book;
import models.Favorite;
import retrofit.APIInterface;
import retrofit.ElMaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookInfoDialog extends Dialog {
    private int bookId;
    Button closeBtn;
    ImageButton favorite;
    String token;
    private Context context;

    public BookInfoDialog(@NonNull Context context, int bookId, String token) {
        super(context);
        this.context = context;
        this.bookId = bookId;
        this.token = token;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info_dialog);
        getCurrentBookInformation(bookId);
        closeBtn = findViewById(R.id.closeButton);
        favorite = findViewById(R.id.favoritesButton);

        checkIfFavorite();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });
    }

    private void checkIfFavorite() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<ArrayList<Favorite>> call = api.Favorite("Bearer " + token);
        call.enqueue(new Callback<ArrayList<Favorite>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Favorite> favorites = response.body();
                    boolean isFavorite = false;
                    if (favorites != null) {
                        for (Favorite favorite : favorites) {
                            if (favorite.getIdBook() == bookId) {
                                isFavorite = true;
                                break;
                            }
                        }
                    }
                    updateFavoriteButton(isFavorite);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {
                // Обработка ошибки
            }
        });
    }

    private void toggleFavorite() {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        boolean currentlyFavorite = favorite.getTag() != null && (boolean) favorite.getTag();
        Call<Void> call = currentlyFavorite ?
                api.RemoveBookFromFavorites(bookId, "Bearer " + token) :
                api.AddBookForFavorite(bookId, "Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    boolean newFavoriteState = !currentlyFavorite;
                    updateFavoriteButton(newFavoriteState);
                    String message = newFavoriteState ? "Книга успешно добавлена в избранное" : "Книга успешно удалена из избранного";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    if (!newFavoriteState) {
                        // Если книга была удалена из избранного, обновим список в FavoriteActivity
                        if (context instanceof FavoriteActivity) {
                            ((FavoriteActivity) context).updateFavorites();
                        }
                    }
                } else {
                    Toast.makeText(context, "Ошибка при изменении состояния избранного", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Ошибка при выполнении запроса", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteButton(boolean isFavorite) {
        int color = isFavorite ? android.R.color.holo_red_dark : android.R.color.darker_gray;
        favorite.setColorFilter(context.getResources().getColor(color));
        favorite.setTag(isFavorite);
    }

    private void getCurrentBookInformation(int bookId) {
        Retrofit retrofit = ElMaAPI.getClient();
        APIInterface api = retrofit.create(APIInterface.class);

        Call<Book> call = api.GetCurrentBookInfotmation(bookId);

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Book book = response.body();
                    if (book != null) {
                        updateDialogWithData(book);
                    } else {
                        // Обработка случая, если ответ пустой или не удалось распарсить
                    }
                } else {
                    // Обработка ошибки, если запрос не удался
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                // Обработка ошибок сети или других исключений
                if (t instanceof IOException) {
                    // Обработка ошибок сети
                    showErrorDialog("Ошибка сети: " + t.getMessage());
                } else {
                    // Обработка других исключений
                    showErrorDialog("Ошибка: " + t.getMessage());
                }
            }
        });
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ошибка");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateDialogWithData(Book book) {
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView seriesTextView = findViewById(R.id.seriesTextView);
        TextView publisherTextView = findViewById(R.id.bookPublisher);
        TextView authorsTextView = findViewById(R.id.bookAuthors);
        TextView editorsTextView = findViewById(R.id.bookEditors);
        TextView placePublicationTextView = findViewById(R.id.place_publication);
        TextView yearPublicationTextView = findViewById(R.id.year_publication);
        TextView themesTextView = findViewById(R.id.themes);
        TextView annotationTextView = findViewById(R.id.annotation);
        ImageView imageView = findViewById(R.id.image_book);

        titleTextView.setText(book.getTitle());
        if (book.getImage() != null) {
            byte[] decodedString = Base64.decode(book.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        } else {
            imageView.setImageResource(R.mipmap.elma); // Или другое заглушечное изображение
        }
        seriesTextView.setText(book.getSeriesName());
        publisherTextView.setText("Издательство: " + book.getPublisher());

        // Преобразуем авторов в строку
        String authorsText = TextUtils.join(", ", book.getAuthorBook() != null ? book.getAuthorBook() : Collections.emptyList());
        authorsTextView.setText("Авторы: " + authorsText);

        // Преобразуем редакторов в строку
        String editorsText = TextUtils.join(", ", book.getEditor() != null ? book.getEditor() : Collections.emptyList());
        editorsTextView.setText("Редакторы: " + editorsText);

        placePublicationTextView.setText("Место публикации: " + book.getPlaceOfPublication());
        yearPublicationTextView.setText("Год публикации: " + book.getYearOfPublication());

        // Преобразуем темы в строку
        String themesText = TextUtils.join(", ", book.getThemesName() != null ? book.getThemesName() : Collections.emptyList());
        themesTextView.setText("Темы: " + themesText);

        annotationTextView.setText("Аннотация: " + book.getAnnotation());
    }


}
