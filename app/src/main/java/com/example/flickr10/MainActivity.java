package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_TIMEOUT = 9000000;

    RecyclerView recyclerView;
    RequestQueue requestQueue;

    ArrayList<Gallery> galleries = new ArrayList<Gallery>();
    ArrayList<ArrayList<Photo>> photosList = new ArrayList<ArrayList<Photo>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView  = findViewById(R.id.reciclerView);

        requestQueue = Volley.newRequestQueue(this);

        getGalleriesData();
    }

    private void getGalleriesData(){
        String get_galleries_url = "https://www.flickr.com/services/rest/?method=flickr.galleries.getList&api_key=ff7e5e14cdd9399f18ea1e728ceffa43&user_id=194072998%40N08&continuation=0&format=json&nojsoncallback=1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_galleries_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONObject("galleries").getJSONArray("gallery");
                    for(int i = 0; i < jsonArray.length(); i++){
                        Gallery gallery = new Gallery();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        gallery.setTitle(jsonObject.getJSONObject("title").getString("_content"));
                        gallery.setDescription(jsonObject.getJSONObject("description").getString("_content"));
                        gallery.setId(jsonObject.getString("id"));
                        galleries.add(gallery);
                    }
                    getPhotosData(galleries);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
           DEFAULT_TIMEOUT,
           DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
           DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(jsonObjectRequest);
    }

    private void getPhotosData(ArrayList<Gallery> gals){
        String url = "https://www.flickr.com/services/rest/?method=flickr.galleries.getPhotos&api_key=ff7e5e14cdd9399f18ea1e728ceffa43&gallery_id=%s&format=json&nojsoncallback=1";
        String photoUrl = "https://live.staticflickr.com/%s/%s_%s.jpg";

        ArrayList<JsonObjectRequest> requests = new ArrayList<>();
        for(int j = 0; j < gals.size(); j++) {
            String formatedUrl = String.format(url, gals.get(j).getId());
            JsonObjectRequest jsonPhotoRequest = new JsonObjectRequest(Request.Method.GET, formatedUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray photosArray = response.getJSONObject("photos").getJSONArray("photo");
                        ArrayList<Photo> photos = new ArrayList<>();
                        for (int k = 0; k < photosArray.length(); k++) {
                            Photo photo = new Photo();
                            JSONObject photoObject = photosArray.getJSONObject(k);
                            photo.setId(photoObject.getString("id"));
                            photo.setSecret(photoObject.getString("secret"));
                            photo.setServer(photoObject.getString("server"));
                            photo.setTitle(photoObject.getString("title"));
                            photo.setOwner(photoObject.getString("owner"));
                            photo.setUrl(String.format(photoUrl,
                                    photo.getServer(),
                                    photo.getId(),
                                    photo.getSecret()
                            ));
                            photos.add(photo);
                        }
                        photosList.add(photos);
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
            requestQueue.add(jsonPhotoRequest);
        }
    }

    private void initializeRecyclerView(){
        if(galleries.size() == photosList.size()) {
            for (int i = 0; i < galleries.size(); i++) {
                galleries.get(i).setPhotos(photosList.get(i));
            }
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, galleries);

            adapter.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(getApplicationContext(), activity_list_photos.class);

                    i.putExtra("GalleryTitle", galleries.get(recyclerView.getChildAdapterPosition(view)).getTitle());

                    Bundle extra = new Bundle();
                    extra.putSerializable("Photos", galleries.get(recyclerView.getChildAdapterPosition(view)).getPhotos());
                    i.putExtra("PhotosBundle", extra);

                    view.getContext().startActivity(i);
                }
            });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}

