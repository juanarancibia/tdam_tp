package com.example.flickr10;

import java.util.ArrayList;
import java.util.List;

public class Gallery {
    public String Id;
    public String Title;
    public String Description;
    public ArrayList<Photo> Photos;

    public Gallery(){

    }

    public ArrayList<Photo> getPhotos() {
        return Photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        Photos = photos;
    }

    public Gallery(String id, String title, String desc){
        this.Id = id;
        this.Title = title;
        this.Description = desc;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
