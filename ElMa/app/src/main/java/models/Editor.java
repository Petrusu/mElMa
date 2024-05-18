package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Editor {

    @SerializedName("editorname")
    @Expose
    private String editorname;

    public String getEditorname() {
        return editorname;
    }

    public void setEditorname(String editorname) {
        this.editorname = editorname;
    }

}
