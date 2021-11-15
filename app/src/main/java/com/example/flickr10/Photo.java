package com.example.flickr10;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(primaryKeys = {"photo_id", "gallery_id"})
public class Photo {

    @ColumnInfo(name = "photo_id")
    public int photo_id;

    @ColumnInfo(name = "gallery_id")
    public int gallery_id;

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
