package com.example.flickr10;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(primaryKeys = {"photo_id","comment_id"})
public class Comment {

    @ColumnInfo(name = "photo_id")
    public int photo_id;

    @ColumnInfo(name = "comment_id")
    public int comment_id;

    @ColumnInfo(name = "realname")
    public int RealName;

    @ColumnInfo(name = "content")
    public String Content;

    @ColumnInfo(name = "authorname")
    public String AuthorName;

    @ColumnInfo(name = "datecreate")
    public Long DateCreate;
}
