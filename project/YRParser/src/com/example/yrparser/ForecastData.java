package com.example.yrparser;

public class ForecastData {
    public int icon;
    public String title;
    public ForecastData(){
        super();
    }
    
    public ForecastData(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }
}