package com.example.urlshortener;

public class Link {
    private String url;
    private String hashId;

    public Link(String url, String hashId) {
        this.url = url;
        this.hashId = hashId;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
