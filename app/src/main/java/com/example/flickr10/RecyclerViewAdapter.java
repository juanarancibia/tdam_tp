package com.example.flickr10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<Gallery> galleries;
    Context context;

    public RecyclerViewAdapter(Context ct, ArrayList<Gallery> gs){
        context = ct;
        galleries = gs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text1.setText(galleries.get(position).getTitle());

        Picasso.get().load(galleries.get(position).getPhotos().get(0).getUrl()).fit().into(holder.img1);
        Picasso.get().load(galleries.get(position).getPhotos().get(1).getUrl()).fit().into(holder.img2);
        Picasso.get().load(galleries.get(position).getPhotos().get(2).getUrl()).fit().into(holder.img3);


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent i = new Intent(context, PhotoListActivity.class);

//                i.putExtra("GalleryTitle", galleries.get(pos).getTitle());
//                i.putExtra("Photos", galleries.get(pos).getPhotos())

            }
        });
    }

    @Override
    public int getItemCount() {
        return galleries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text1;
        ImageView img1, img2, img3;

        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.album_title);
            img1 = itemView.findViewById(R.id.myImageView);
            img2 = itemView.findViewById(R.id.myImageView2);
            img3 = itemView.findViewById(R.id.myImageView3);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    public interface ItemClickListener {

        void onItemClick(View v,int pos);
    }
}