package com.example.flickr10;

import java.io.Serializable;

public class Photo implements Serializable {
    public String Id;
    public String Secret;
    public String Server;
    public String Title;
    public String Url;

    public String getId() {
        return Id;
    }

    public String getSecret() {
        return Secret;
    }

    public void setSecret(String secret) {
        Secret = secret;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setId(String id) {
        Id = id;
    }

    public Photo(){

    }

}
