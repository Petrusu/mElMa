package com.example.melma.more;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.melma.R;

import java.util.Collections;
import java.util.List;

import models.Book;

public class BookGridAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books;

    public BookGridAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.book_grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.bookImage);
            holder.titleTextView = convertView.findViewById(R.id.bookTitle);
            holder.authorsTextView = convertView.findViewById(R.id.bookAuthors);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book currentBook = (Book) getItem(position);

        holder.titleTextView.setText(currentBook.getTitle());
        String authorsText = TextUtils.join(", ", currentBook.getAuthors() != null ? currentBook.getAuthors() : Collections.emptyList());
        holder.authorsTextView.setText(authorsText);

        if (currentBook.getImage() != null) {
            byte[] decodedString = Base64.decode(currentBook.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte);
        } else {
            // Обработка случая, когда изображение отсутствует
            holder.imageView.setImageResource(R.mipmap.elma); // Или другое заглушечное изображение
        }

        return convertView;
    }

    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView authorsTextView;
    }
}
