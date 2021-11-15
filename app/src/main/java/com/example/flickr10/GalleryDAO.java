package com.example.flickr10;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GalleryDAO {
    @Query("SELECT * FROM Gallery")
    ArrayList<Gallery> getAll();
}
