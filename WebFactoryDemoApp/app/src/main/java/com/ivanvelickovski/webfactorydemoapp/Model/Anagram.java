package com.ivanvelickovski.webfactorydemoapp.Model;

public class Anagram {
    private String title;
    private String description;
    private int titlePosition;
    private int descriptionPosition;
    private VolumeInfo book;

    public Anagram(String title, String description, int titlePosition, int descriptionPosition, VolumeInfo book) {
        this.title = title;
        this.description = description;
        this.titlePosition = titlePosition;
        this.descriptionPosition = descriptionPosition;
        this.book = book;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleAnagramPosition() {
        return titlePosition;
    }

    public void setTitlePosition(int titlePosition) {
        this.titlePosition = titlePosition;
    }

    public int getDescriptionAnagramPosition() {
        return descriptionPosition;
    }

    public void setDescriptionPosition(int descriptionPosition) {
        this.descriptionPosition = descriptionPosition;
    }

    public VolumeInfo getBook() {
        return book;
    }

    public void setBook(VolumeInfo book) {
        this.book = book;
    }
}
