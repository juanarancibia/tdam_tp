package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.List;

public class activity_photo_detail extends AppCompatActivity {

    public static final int DEFAULT_TIMEOUT = 9000000;

    RecyclerView recyclerView;
    PhotoModel photoModel;
    ImageView imgView;
    RequestQueue requestQueue;
    FloatingActionButton fab_backward;
    FloatingActionButton fab_www;

    AppDatabase db;

    ProgressDialog spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        db = AppDatabase.getDatabase(getApplicationContext());

        spinner = ProgressDialog.show(this, "Flickr APP", "Loading data...", true);

        photoModel = (PhotoModel) getIntent().getExtras().get("Photo");

        imgView = findViewById(R.id.photo_detail_image);
        Picasso.get().load(photoModel.getUrl()).fit().into(imgView);

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

                String url = "https://www.flickr.com/photos/" + photoModel.getOwner() + "/" + photoModel.getId();
                i.putExtra("Url", url);

                view.getContext().startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.photo_detail_recycler_view);

        if(isNetworkAvailable()){
            requestQueue = Volley.newRequestQueue(this);
            getComments(photoModel.Id);
        } else {
            getCommentsDB(photoModel.Id);
        }

    }

    private void getCommentsDB(String id) {
        db.commentDAO().getAll(id).observe(this, raw_comments -> {
            if(raw_comments == null){
                return;
            }
            ArrayList<CommentModel> mapped_comments = new ArrayList<CommentModel>();
            for(int i = 0; i<raw_comments.size(); i++){
                Comment curr_comm = raw_comments.get(i);
                mapped_comments.add(new CommentModel(
                        curr_comm.comment_id,
                        curr_comm.RealName,
                        curr_comm.Content,
                        curr_comm.AuthorName,
                        new Date(curr_comm.DateCreate)
                ));
            }
            photoModel.setComments(mapped_comments);
            initializeRecyclerView();
        });
    }

    private void getComments(String photoId){
        String url = "https://www.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=ff7e5e14cdd9399f18ea1e728ceffa43&photo_id=%s&format=json&nojsoncallback=1";
        String formatedUrl = String.format(url, photoId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, formatedUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<CommentModel> commentModels = new ArrayList<>();
                    JSONArray commentsArray = response.getJSONObject("comments").getJSONArray("comment");
                    for (int i = 0; i < commentsArray.length(); i++) {
                        JSONObject comment = commentsArray.getJSONObject(i);

                        commentModels.add(new CommentModel(
                                comment.getString("id"),
                                comment.getString("realname"),
                                comment.getString("_content"),
                                comment.getString("authorname"),
                                new Date((long) comment.getLong("datecreate"))));
                    }
                    photoModel.setComments(commentModels);

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

        requestQueue.add(jsonObjectRequest).setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));;
    }

    private void initializeRecyclerView() {
        CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(this, photoModel.getComments());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinner.dismiss();

        if(isNetworkAvailable()){
            saveComments();
        }
    }

    private void saveComments() {
        new Thread(){
            @Override
            public void run(){
                try{
                    List<Comment> comms = new ArrayList<Comment>();
                    ArrayList<CommentModel> comments = photoModel.getComments();
                    for(int i = 0; i<comments.size();i++){
                        Comment comment_to_add = new Comment();
                        comment_to_add.photo_id = photoModel.getId();
                        comment_to_add.comment_id = comments.get(i).getId();
                        comment_to_add.RealName = comments.get(i).getRealname();
                        comment_to_add.Content = comments.get(i).getContent();
                        comment_to_add.AuthorName = comments.get(i).getAuthorname();
                        comment_to_add.DateCreate = comments.get(i).getDateCreate().getTime();

                        comms.add(comment_to_add);
                    }

                    db.commentDAO().insertAll(comms);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

        }.start();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}