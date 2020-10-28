package com.example.uploadimages.networkUtils;

public class ServerResponse<T> {
    private boolean status;
    private String message;
    private T data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return message;
    }

    public void setStatusMessage(String statusMessage) {
        this.message = statusMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
