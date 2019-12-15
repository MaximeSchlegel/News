package com.centrale.news.DataModels;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SourcesList {

    private ArrayList<Source> sources;
    private static SourcesList list = null;

    private SourcesList() {
        sources = new ArrayList<>();
    }

    public Source get(int i){
        return sources.get(i);
    }

    public ArrayList<Source> getAll() {
        return sources;
    }

    public void add(Source source){
        sources.add(source);
    }

    public void clear(){
        sources.clear();
    }

    public int size(){
        return sources.size();
    }

    public String toString() {
        return sources.toString();
    }

    public static SourcesList getInstance() {
        if (list == null) {
            list = new SourcesList();
        }
        return list;
    }
}
