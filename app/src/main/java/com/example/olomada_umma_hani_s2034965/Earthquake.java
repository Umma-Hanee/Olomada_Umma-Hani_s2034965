package com.example.olomada_umma_hani_s2034965;

public class Earthquake {

    private String title;
    private String link;
    private String date; // Changed from String to Date
    private String description;
    private double magnitude;

    public Earthquake(String title, String link, String date, String description, double magnitude) { // Changed parameter type from String to Date
        this.title = title;
        this.link = link;
        this.date = date;
        this.description = description;
        this.magnitude = magnitude;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n"
                + "Date: " + date + "\n"
                + "Link: " + link + "\n"
                + "Description: " + description + "\n"
                + "Magnitude: " + magnitude;
    }

    public String getLocation() {
        return getLocation();
    }
}