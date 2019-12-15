package com.centrale.news.DataModels;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Source {
    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;

    public Source(String id, String name, String description, String url, String category, String language, String country) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    public Source(JSONObject source) {
        try {
            this.id = source.get("id").toString();
            this.name = source.get("name").toString();
            this.description = source.get("description").toString();
            this.url = source.get("url").toString();
            this.category = source.get("category").toString();
            this.language = source.get("language").toString();
            this.country = source.get("country").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Source", e.getMessage());
        }
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getUrl() {
        return url;
    }
    public String getCategory() {
        return category;
    }
    public String getLanguage() {
        return language;
    }
    public String getCountry() {
        return country;
    }

    @NonNull
    @Override
    public String toString() {
        return "Source : {"+
                "\n  id : " + this.id +
                "\n  name : " + this.name +
                "\n  description : " + this.description +
                "\n  url : " + this.url +
                "\n  category : " + this.category +
                "\n  language : " + this.language +
                "\n  country : " + this.country +
                "\n}";
    }
}
