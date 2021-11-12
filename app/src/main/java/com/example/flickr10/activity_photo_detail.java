package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class activity_photo_detail extends AppCompatActivity {

    RecyclerView recyclerView;
    Photo photo;
    ImageView imgView;
    RequestQueue requestQueue;
    FloatingActionButton fab_backward;
    FloatingActionButton fab_www;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        photo = (Photo) getIntent().getExtras().get("Photo");

        imgView = findViewById(R.id.photo_detail_image);
        Picasso.get().load(photo.getUrl()).fit().into(imgView);

        fab_backward = (FloatingActionButton) findViewById(R.id.floatingActionButton_photo_detail);
        fab_backward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab_www = (FloatingActionButton) findViewById(R.id.floatingActionButton_www);
        fab_www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);

                String url = "https://www.flickr.com/photos/" + photo.getOwner() + "/" + photo.getId();
                i.putExtra("Url", url);

                view.getContext().startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.photo_detail_recycler_view);

        requestQueue = Volley.newRequestQueue(this);
        getComments(photo.Id);
    }

    private void getComments(String photoId){
        String url = "https://www.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=ff7e5e14cdd9399f18ea1e728ceffa43&photo_id=%s&format=json&nojsoncallback=1";
        String formatedUrl = String.format(url, photoId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, formatedUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Comment> comments = new ArrayList<>();
                    JSONArray commentsArray = response.getJSONObject("comments").getJSONArray("comment");
                    for (int i = 0; i < commentsArray.length(); i++) {
                        JSONObject comment = commentsArray.getJSONObject(i);

                        comments.add(new Comment(
                                comment.getString("realname"),
                                comment.getString("_content"),
                                comment.getString("authorname"),
                                new Date((long) comment.getLong("datecreate"))));
                    }
                    photo.setComments(comments);

                    initializeRecyclerView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void initializeRecyclerView() {
        CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(this, photo.getComments());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}