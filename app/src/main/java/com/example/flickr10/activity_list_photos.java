package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class activity_list_photos extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Photo> photos;
    String galleryTitle;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photos);

        recyclerView = findViewById(R.id.photos_recycler_view);

        Bundle extra = getIntent().getBundleExtra("PhotosBundle");
        this.photos = (ArrayList<Photo>) extra.getSerializable("Photos");

        this.galleryTitle = getIntent().getExtras().getString("GalleryTitle");
        textView1 = findViewById(R.id.gallery_title);
        textView1.setText(this.galleryTitle);

        PhotosRecyclerViewAdapter adapter = new PhotosRecyclerViewAdapter(this, photos);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}