package com.example.flickr10;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PhotoDAO {

    @Query("SELECT * FROM Photo WHERE gallery_id = :g_id ")
    LiveData<List<Photo>> getAll(String g_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Photo> photos);
}
