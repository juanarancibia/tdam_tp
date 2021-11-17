package com.example.flickr10;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CommentDAO {
    @Query("SELECT * FROM Comment WHERE photo_id = :p_id")
    LiveData<List<Comment>> getAll(String p_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Comment> comments);
}
