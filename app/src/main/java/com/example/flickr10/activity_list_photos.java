package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class activity_list_photos extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<PhotoModel> photoModels;
    String galleryTitle;
    TextView textView1;
    FloatingActionButton fab;

    FloatingActionButton fab_order;
    FloatingActionButton fab_order_name;
    FloatingActionButton fab_order_date;
    Boolean order_menu_open = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photos);

        recyclerView = findViewById(R.id.photos_recycler_view);

        Bundle extra = getIntent().getBundleExtra("PhotosBundle");
        this.photoModels = (ArrayList<PhotoModel>) extra.getSerializable("Photos");

        this.galleryTitle = getIntent().getExtras().getString("GalleryTitle");
        textView1 = findViewById(R.id.gallery_title);
        textView1.setText(this.galleryTitle);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_photo_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab_order = (FloatingActionButton) findViewById(R.id.floatingActionButton_order_list);
        fab_order_name = (FloatingActionButton) findViewById(R.id.floatingActionButton_order_name);
        fab_order_date = (FloatingActionButton) findViewById(R.id.floatingActionButton_order_calendar);

        fab_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!order_menu_open){
                    showFABMenu();
                } else {
                    closeFabMenu();
                }
            }
        });

        PhotosRecyclerViewAdapter adapter = new PhotosRecyclerViewAdapter(this, photoModels);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), activity_photo_detail.class);

                i.putExtra("Photo", photoModels.get(recyclerView.getChildAdapterPosition(view)));

                view.getContext().startActivity(i);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        fab_order_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(photoModels, (o2, o1) -> (o1.getTitle().compareTo(o2.getTitle())));
                Collections.reverse(photoModels);
                adapter.notifyDataSetChanged();
            }
        });

        fab_order_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(photoModels, (o2, o1) -> (o1.getDateUpload().compareTo(o2.getDateUpload())));
                Collections.reverse(photoModels);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showFABMenu(){
        order_menu_open = true;

        fab_order_name.animate().translationY(-getResources().getDimension(R.dimen.std_55));
        fab_order_date.animate().translationY(-getResources().getDimension(R.dimen.std_105));
    }

    private void closeFabMenu(){
        order_menu_open = false;

        fab_order_name.animate().translationY(0);
        fab_order_date.animate().translationY(0);
    }
}