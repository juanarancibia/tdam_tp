package com.example.flickr10;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PhotoModel implements Serializable {
    public String Id;
    public String Secret;
    public String Server;
    public String Title;
    public String Url;
    public String Owner;
    public Date DateUpload;
    public String LocalPath;

    public PhotoModel(String id, String secret, String server, String title, String url, String owner, Date dateUpload, String localPath) {
        Id = id;
        Secret = secret;
        Server = server;
        Title = title;
        Url = url;
        Owner = owner;
        DateUpload = dateUpload;
        LocalPath = localPath;
    }

    public String getLocalPath() {
        return LocalPath;
    }

    public void setLocalPath(String localPath) {
        LocalPath = localPath;
    }

    public ArrayList<CommentModel> commentModels;

    public Date getDateUpload() {
        return DateUpload;
    }

    public void setDateUpload(Date dateUpload) {
        DateUpload = dateUpload;
    }

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

    public ArrayList<CommentModel> getComments() {
        return commentModels;
    }

    public void setComments(ArrayList<CommentModel> commentModels) {
        this.commentModels = commentModels;
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

    public PhotoModel(){

    }

}
