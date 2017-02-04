package com.example.user.movie;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KV on 2/2/17.
 */

public class Review {
    private String id;
    private String author;
    private String content;

    public Review() {

    }

    public Review(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.author = trailer.getString("author");
        this.content = trailer.getString("content");
    }
    public Review(String author,String content)
    {
        this.author=author;
        this.content=content;
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}
