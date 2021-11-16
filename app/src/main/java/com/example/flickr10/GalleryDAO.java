package com.example.flickr10;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface GalleryDAO {
    @Query("SELECT * FROM Gallery")
    LiveData<List<Gallery>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Gallery> galerries);
}
