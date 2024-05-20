package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookPageResponse {
    @SerializedName("TotalBooks")
    @Expose
    private Integer totalBooks;

    @SerializedName("TotalPages")
    @Expose
    private Integer totalPages;

    @SerializedName("CurrentPage")
    @Expose
    private Integer currentPage;

    @SerializedName("Books")
    @Expose
    private List<Book> books;

    // Getters and Setters
    public Integer getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(Integer totalBooks) {
        this.totalBooks = totalBooks;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
