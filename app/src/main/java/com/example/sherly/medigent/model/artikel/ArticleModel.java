package com.example.sherly.medigent.model.artikel;

import java.util.ArrayList;

public class ArticleModel {
    Integer count;
    String status;
    ArrayList<DetailArticleModel> articles;

    public Integer getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<DetailArticleModel> getArticles() {
        return articles;
    }
}
