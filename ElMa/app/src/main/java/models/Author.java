package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Author {

    @SerializedName("authorsname")
    @Expose
    private String authorsname;

    public String getAuthorsname() {
        return authorsname;
    }

    public void setAuthorsname(String authorsname) {
        this.authorsname = authorsname;
    }

}
