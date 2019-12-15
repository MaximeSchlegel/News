package com.centrale.news.DataModels;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Article {

    private String sourceId;
    private String author;
    private String title;
    private String description;
    private String articleUrl;
    private String imageUrl;
    private String publicationDate;
    private String content;

    private Bitmap image;

    public Article(String sourceId, String author, String title, String description, String articleUrl, String imageUrl, String publicationDate, String content) {
        this.sourceId = sourceId;
        this.author = author;
        this.title = title;
        this.description = description;
        this.articleUrl = articleUrl;
        this.imageUrl = imageUrl;
        this.publicationDate = publicationDate;
        this.content = content;
        this.image = null;
    }

    public Article(JSONObject article) {
        try {
            this.sourceId = article.getJSONObject("source").get("id").toString();
            this.author = article.get("author").toString();
            this.title = article.get("description").toString();
            this.description = article.get("description").toString();
            this.articleUrl = article.get("url").toString();
            this.imageUrl = article.get("urlToImage").toString();
            this.publicationDate = article.get("publishedAt").toString();
            this.content = article.get("content").toString();
            this.image = null;
        } catch (JSONException e) {
            Log.e("Article", e.getMessage());
        }
    }

    public String getSourceId() {
        return sourceId;
    }
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getArticleUrl() {
        return articleUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getPublicationDate() {
        return publicationDate;
    }
    public String getContent() {
        return content;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Article : {" +
                "\n  sourceId :'" + sourceId +
                "\n  author='" + author +
                "\n  title='" + title +
                "\n  description='" + description +
                "\n  articleUrl='" + articleUrl +
                "\n  imageUrl='" + imageUrl +
                "\n  publicationDate='" + publicationDate +
                "\n  content='" + content +
                "\n}";
    }


}
