package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMsg {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("reasonPhrase")
    @Expose
    private String reasonPhrase;
    @SerializedName("headers")
    @Expose
    private List<Object> headers;
    @SerializedName("trailingHeaders")
    @Expose
    private List<Object> trailingHeaders;
    @SerializedName("requestMessage")
    @Expose
    private Object requestMessage;
    @SerializedName("isSuccessStatusCode")
    @Expose
    private Boolean isSuccessStatusCode;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public List<Object> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Object> headers) {
        this.headers = headers;
    }

    public List<Object> getTrailingHeaders() {
        return trailingHeaders;
    }

    public void setTrailingHeaders(List<Object> trailingHeaders) {
        this.trailingHeaders = trailingHeaders;
    }

    public Object getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(Object requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Boolean getIsSuccessStatusCode() {
        return isSuccessStatusCode;
    }

    public void setIsSuccessStatusCode(Boolean isSuccessStatusCode) {
        this.isSuccessStatusCode = isSuccessStatusCode;
    }

}