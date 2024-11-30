package com.example.javaa6;

public class Products {
    private String description;
    private String title;

    public Products(String description,String title) {
        this.description = description;
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
}