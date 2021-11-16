package com.example.flickr10;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(primaryKeys = {"photo_id","comment_id"})
public class Comment {

    @ColumnInfo(name = "photo_id")
    @NonNull
    public String photo_id;

    @ColumnInfo(name = "comment_id")
    @NonNull
    public String comment_id;

    @ColumnInfo(name = "realname")
    public String RealName;

    @ColumnInfo(name = "content")
    public String Content;

    @ColumnInfo(name = "authorname")
    public String AuthorName;

    @ColumnInfo(name = "datecreate")
    public Long DateCreate;
}
