package com.ivanvelickovski.webfactorydemoapp.Model;

public class Anagram {
    private String title;
    private String description;
    private int titlePosition;
    private int descriptionPosition;

    public Anagram(String title, String description, int titlePosition, int descriptionPosition) {
        this.title = title;
        this.description = description;
        this.titlePosition = titlePosition;
        this.descriptionPosition = descriptionPosition;
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

    public int getTitlePosition() {
        return titlePosition;
    }

    public void setTitlePosition(int titlePosition) {
        this.titlePosition = titlePosition;
    }

    public int getDescriptionPosition() {
        return descriptionPosition;
    }

    public void setDescriptionPosition(int descriptionPosition) {
        this.descriptionPosition = descriptionPosition;
    }
}
