package com.example.flickr10;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;

@Dao
public interface PhotoDAO {

    @Query("SELECT * FROM Photo WHERE gallery_id = :g_id ")
    ArrayList<Photo> getAll(int g_id);
}
