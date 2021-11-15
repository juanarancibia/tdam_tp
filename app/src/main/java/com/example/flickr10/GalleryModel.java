package com.example.flickr10;

import java.util.ArrayList;


public class GalleryModel {
    public String Id;
    public String Title;
    public String Description;
    public ArrayList<PhotoModel> photoModels;

    public GalleryModel(){

    }

    public ArrayList<PhotoModel> getPhotos() {
        return photoModels;
    }

    public void setPhotos(ArrayList<PhotoModel> photoModels) {
        this.photoModels = photoModels;
    }

    public GalleryModel(String id, String title, String desc){
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
