package models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Book {

    @SerializedName("BookId")
    @Expose
    private Integer bookId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("SeriesName")
    @Expose
    private String seriesName;
    @SerializedName("Annotation")
    @Expose
    private String annotation;
    @SerializedName("Publisher")
    @Expose
    private String publisher;
    @SerializedName("PlaceOfPublication")
    @Expose
    private String placeOfPublication;
    @SerializedName("YearOfPublication")
    @Expose
    private String yearOfPublication;
    @SerializedName("BBK")
    @Expose
    private String bbk;
    @SerializedName("Author")
    @Expose
    private Object author;
    @SerializedName("Editor")
    @Expose
    private Object editor;
    @SerializedName("ThemeIds")
    @Expose
    private List<Object> themeIds;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Authors")
    @Expose
    private List<String> authors;
    @SerializedName("Editors")
    @Expose
    private List<String> editors;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPlaceOfPublication() {
        return placeOfPublication;
    }

    public void setPlaceOfPublication(String placeOfPublication) {
        this.placeOfPublication = placeOfPublication;
    }

    public String getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public String getBbk() {
        return bbk;
    }

    public void setBbk(String bbk) {
        this.bbk = bbk;
    }

    public Object getAuthor() {
        return author;
    }

    public void setAuthor(Object author) {
        this.author = author;
    }

    public Object getEditor() {
        return editor;
    }

    public void setEditor(Object editor) {
        this.editor = editor;
    }

    public List<Object> getThemeIds() {
        return themeIds;
    }

    public void setThemeIds(List<Object> themeIds) {
        this.themeIds = themeIds;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getEditors() {
        return editors;
    }

    public void setEditors(List<String> editors) {
        this.editors = editors;
    }

}