package com.example.flickr10;

import java.util.Date;

public class Comment {
    public String Realname;
    public String Content;
    public String Authorname;
    public Date DateCreate;

    public Comment(String realname, String content, String authorname, Date dateCreate) {
        Realname = realname;
        Content = content;
        Authorname = authorname;
        DateCreate = dateCreate;
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
