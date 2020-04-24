package com.example.loginapp;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("body")
    private String text;

    public Post(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
