package com.example.flickr10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_TIMEOUT = 9000000;

    RecyclerView recyclerView;
    RequestQueue requestQueue;

    ArrayList<GalleryModel> galleries = new ArrayList<GalleryModel>();
    ArrayList<ArrayList<PhotoModel>> photosList = new ArrayList<ArrayList<PhotoModel>>();

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
                        GalleryModel galleryModel = new GalleryModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        galleryModel.setTitle(jsonObject.getJSONObject("title").getString("_content"));
                        galleryModel.setDescription(jsonObject.getJSONObject("description").getString("_content"));
                        galleryModel.setId(jsonObject.getString("id"));
                        galleries.add(galleryModel);
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

    private void getPhotosData(ArrayList<GalleryModel> gals){
        String url = "https://www.flickr.com/services/rest/?method=flickr.galleries.getPhotos&api_key=ff7e5e14cdd9399f18ea1e728ceffa43&gallery_id=%s&extras=date_upload&format=json&nojsoncallback=1";
        String photoUrl = "https://live.staticflickr.com/%s/%s_%s.jpg";

        ArrayList<JsonObjectRequest> requests = new ArrayList<>();
        for(int j = 0; j < gals.size(); j++) {
            String formatedUrl = String.format(url, gals.get(j).getId());
            JsonObjectRequest jsonPhotoRequest = new JsonObjectRequest(Request.Method.GET, formatedUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray photosArray = response.getJSONObject("photos").getJSONArray("photo");
                        ArrayList<PhotoModel> photoModels = new ArrayList<>();
                        for (int k = 0; k < photosArray.length(); k++) {
                            PhotoModel photoModel = new PhotoModel();
                            JSONObject photoObject = photosArray.getJSONObject(k);
                            photoModel.setId(photoObject.getString("id"));
                            photoModel.setSecret(photoObject.getString("secret"));
                            photoModel.setServer(photoObject.getString("server"));
                            photoModel.setTitle(photoObject.getString("title"));
                            photoModel.setOwner(photoObject.getString("owner"));
                            photoModel.setUrl(String.format(photoUrl,
                                    photoModel.getServer(),
                                    photoModel.getId(),
                                    photoModel.getSecret()
                            ));
                            photoModel.setDateUpload(new Date(photoObject.getLong("dateupload")));

                            photoModels.add(photoModel);
                        }
                        photosList.add(photoModels);
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

