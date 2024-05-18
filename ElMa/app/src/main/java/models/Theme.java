package models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Theme {

    @SerializedName("ThemesId")
    @Expose
    private Integer themesId;
    @SerializedName("Themesname")
    @Expose
    private String themesname;
    @SerializedName("BookThemes")
    @Expose
    private List<Object> bookThemes;

    public Integer getThemesId() {
        return themesId;
    }

    public void setThemesId(Integer themesId) {
        this.themesId = themesId;
    }

    public String getThemesname() {
        return themesname;
    }

    public void setThemesname(String themesname) {
        this.themesname = themesname;
    }

    public List<Object> getBookThemes() {
        return bookThemes;
    }

    public void setBookThemes(List<Object> bookThemes) {
        this.bookThemes = bookThemes;
    }

}
