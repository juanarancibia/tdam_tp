package com.example.flickr10;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhotoListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Photo> photos;
    String galleryTitle;
    TextView textView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_list);

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
