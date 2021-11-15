package com.example.flickr10;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;

@Dao
public interface CommentDAO {
    @Query("SELECT * FROM Comment WHERE photo_id = :p_id")
    ArrayList<Comment> getAll(int p_id);
}
