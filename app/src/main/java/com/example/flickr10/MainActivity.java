package com.example.flickr10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_TIMEOUT = 9000000;

    AppDatabase db;

    RecyclerView recyclerView;
    RequestQueue requestQueue;

    ArrayList<GalleryModel> galleries = new ArrayList<GalleryModel>();
    ArrayList<ArrayList<PhotoModel>> photosList = new ArrayList<ArrayList<PhotoModel>>();

    ProgressDialog spinner;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(getApplicationContext());



        recyclerView  = findViewById(R.id.reciclerView);

        spinner = ProgressDialog.show(this, "Flickr APP", "Loading data...", true);

        if(isNetworkAvailable()){
            requestQueue = Volley.newRequestQueue(this);

            getGalleriesData();
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                String channelId = "channel_1";
                NotificationChannel channel = new NotificationChannel(channelId, "Notifications",
                        NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);


                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.flickr)
                        .setContentTitle("Flickr APP")
                        .setContentText("No es posible actualizar el listado de directorios desde la web. Se caragarán desde Base de Datos")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(1, builder.build());
            }
            getGalleriesDB();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getGalleriesDB() {
        db.galleryDAO().getAll().observe(this, raw_gals -> {
            if(raw_gals == null){
                return;
            }
            ArrayList<GalleryModel> mapped_galleries = new ArrayList<GalleryModel>();
            for(int i = 0; i<raw_gals.size();i++){
                Gallery curr_gal = raw_gals.get(i);
                mapped_galleries.add(new GalleryModel(
                        curr_gal.gallery_id,
                        curr_gal.Title,
                        curr_gal.Description
                ));
            }
            galleries = mapped_galleries;

            getPhotosDBData();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getPhotosDBData(){
        for(int i = 0; i < galleries.size(); i++){
            db.photoDao().getAll(galleries.get(i).getId()).observe(this, raw_photos -> {
                if(raw_photos == null){
                    return;
                }
                ArrayList<PhotoModel> mapped_photos = new ArrayList<>();
                for(int j = 0; j<raw_photos.size(); j++){
                    Photo curr_photo = raw_photos.get(j);
                    mapped_photos.add(new PhotoModel(
                            curr_photo.photo_id,
                            curr_photo.Secret,
                            curr_photo.Server,
                            curr_photo.Title,
                            curr_photo.Url,
                            curr_photo.Owner,
                            new Date(curr_photo.DateUpload),
                            curr_photo.LocalPath
                    ));
                }

                galleries.stream().filter(g -> g.getId().equals(raw_photos.get(0).gallery_id))
                .findFirst().get().setPhotos(mapped_photos);

                if(raw_photos.get(0).gallery_id.equals(galleries.get(galleries.size()-1).getId())){
                    initializeRecyclerView();
                }
            });
        }
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
                        prepareGalleryPhoto();
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

    private void prepareGalleryPhoto(){
        if(galleries.size() == photosList.size()) {
            for (int i = 0; i < galleries.size(); i++) {
                galleries.get(i).setPhotos(photosList.get(i));
            }

            initializeRecyclerView();
        }
    }

    private void initializeRecyclerView(){
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, galleries, isNetworkAvailable());

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
        spinner.dismiss();

        if(isNetworkAvailable()) {
            saveGalleries();
            savePhotos();
        }
    }

    private void savePhotos() {
        new Thread(){
            @Override
            public void run(){
                try{
                    List<Photo> photos = new ArrayList<Photo>();
                    for(int i = 0; i<galleries.size();i++){
                        for(int j = 0; j<photosList.get(i).size();j++){
                            Photo photo_to_add = new Photo();
                            photo_to_add.photo_id = galleries.get(i).getPhotos().get(j).getId();
                            photo_to_add.gallery_id = galleries.get(i).getId();
                            photo_to_add.Title = galleries.get(i).getPhotos().get(j).getTitle();
                            photo_to_add.Server = galleries.get(i).getPhotos().get(j).getServer();
                            photo_to_add.Owner = galleries.get(i).getPhotos().get(j).getOwner();
                            photo_to_add.Url = galleries.get(i).getPhotos().get(j).getUrl();
                            photo_to_add.DateUpload = galleries.get(i).getPhotos().get(j).getDateUpload().getTime();
                            photo_to_add.LocalPath = galleries.get(i).getId() + "_" +
                                                    galleries.get(i).getPhotos().get(j).getId();
                            photos.add(photo_to_add);
                            photo_to_add.saveImage();
                        }
                    }
                    db.photoDao().insertAll(photos);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void saveGalleries() {
        new Thread(){
            @Override
            public void run(){
                try{
                    List<Gallery> gals = new ArrayList<Gallery>();
                    for(int i = 0; i<galleries.size();i++){
                        Gallery gallery_to_add = new Gallery();
                        gallery_to_add.gallery_id = galleries.get(i).getId();
                        gallery_to_add.Title = galleries.get(i).getTitle();
                        gallery_to_add.Description = galleries.get(i).getDescription();
                        gals.add(gallery_to_add);
                    }

                    long[] result = db.galleryDAO().insertAll(gals);
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

