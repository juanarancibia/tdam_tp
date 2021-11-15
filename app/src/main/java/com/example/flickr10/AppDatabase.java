package com.example.flickr10;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Comment.class, Photo.class, Gallery.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract  CommentDAO commentDAO();
    public abstract PhotoDAO photoDao();
    public  abstract GalleryDAO galleryDAO();
}
