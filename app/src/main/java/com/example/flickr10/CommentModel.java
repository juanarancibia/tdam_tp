package com.example.flickr10;

import java.util.Date;

public class CommentModel {
    public String Id;
    public String Realname;
    public String Content;
    public String Authorname;
    public Date DateCreate;

    public CommentModel(String id, String realname, String content, String authorname, Date dateCreate) {
        Id = id;
        Realname = realname;
        Content = content;
        Authorname = authorname;
        DateCreate = dateCreate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRealname() {
        return Realname;
    }

    public void setRealname(String realname) {
        Realname = realname;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getAuthorname() {
        return Authorname;
    }

    public void setAuthorname(String authorname) {
        Authorname = authorname;
    }

    public Date getDateCreate() {
        return DateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        DateCreate = dateCreate;
    }
}
