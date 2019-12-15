package com.centrale.news.DataModels;

import java.util.ArrayList;
import java.util.Iterator;

public class ArticlesList {

    private ArrayList<Article> articles;
    private static ArticlesList list = null;

    private ArticlesList() {
        articles = new ArrayList<>();
    }

    public Article get(int i ) {
        return articles.get(i);
    }

    public ArrayList<Article> getAll() {
        return articles;
    }

    public void add(Article article) {
        articles.add(article);
    }

    public void addAll(Iterable<Article> articles) {
        for (Article article : articles) {
            this.articles.add(article);
        }
    }

    public void clear() {
        articles.clear();
    }

    public int size() {
        return articles.size();
    }

    public String toString() {
        return articles.toString();
    }

    public static ArticlesList getInstance() {
        if (list == null) {
            list = new ArticlesList();
        }
        return list;
    }
}