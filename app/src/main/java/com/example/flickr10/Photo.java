package com.example.flickr10;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    public String Id;
    public String Secret;
    public String Server;
    public String Title;
    public String Url;
    public String Owner;
    public ArrayList<Comment> Comments;

    public String getId() {
        return Id;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
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

    public ArrayList<Comment> getComments() {
        return Comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        Comments = comments;
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
