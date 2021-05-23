package com.example.urlshortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;


public class UrlModel {

    @Id
    @JsonIgnore
    private String id;

    private String shortId;

    private String destination;

    private String shortUrl;

    public UrlModel(String shortId, String destination) {
        this.shortId = shortId;
        this.destination = destination;
        this.shortUrl = String.format("http://localhost:8080/%s", this.shortId);
    }

    public UrlModel(){}

    public String getId() {
        return id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
