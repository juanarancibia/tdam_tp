package com.example.flickr10;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(primaryKeys = {"photo_id", "gallery_id"})
public class Photo {

    @ColumnInfo(name = "photo_id")
    @NonNull
    public String photo_id;

    @ColumnInfo(name = "gallery_id")
    @NonNull
    public String gallery_id;

    @ColumnInfo(name = "secret")
    public String Secret;

    @ColumnInfo(name = "server")
    public String Server;

    @ColumnInfo(name = "title")
    public String Title;

    @ColumnInfo(name = "url")
    public String Url;

    @ColumnInfo(name = "owner")
    public String Owner;

    @ColumnInfo(name = "dateupload")
    public Long DateUpload;

}
