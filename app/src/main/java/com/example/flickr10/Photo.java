package com.example.flickr10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

@Entity(primaryKeys = {"photo_id", "gallery_id"})
public class Photo {

    @ColumnInfo(name = "photo_id")
    @NonNull
    public String photo_id;

    @ColumnInfo(name = "gallery_id")
    @NonNull
    public String gallery_id;

    @ColumnInfo(name = "secret")
    public String Secret;

    @ColumnInfo(name = "server")
    public String Server;

    @ColumnInfo(name = "title")
    public String Title;

    @ColumnInfo(name = "url")
    public String Url;

    @ColumnInfo(name = "owner")
    public String Owner;

    @ColumnInfo(name = "dateupload")
    public Long DateUpload;

    @ColumnInfo(name = "local_path")
    public String LocalPath;


    public void saveImage() {
        String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/" + "flickr_photos");
        dir.mkdir();

        File f = new File(dir, LocalPath);
        try {
            f.createNewFile();
        } catch (Exception e) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(f)) {
            Bitmap bitmap = getBitmapFromURL();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromURL() {
        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
