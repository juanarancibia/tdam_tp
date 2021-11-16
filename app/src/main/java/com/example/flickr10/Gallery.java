package com.example.flickr10;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Gallery {
    @PrimaryKey
    @NonNull
    public String gallery_id;

    @ColumnInfo(name = "title")
    public String Title;

    @ColumnInfo(name = "description")
    public String Description;
}
