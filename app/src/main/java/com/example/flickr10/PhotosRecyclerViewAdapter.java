package com.example.flickr10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder> implements View.OnClickListener{

    Context context;
    ArrayList<Photo> photos;

    private View.OnClickListener listener;

    public PhotosRecyclerViewAdapter(Context ctx, ArrayList<Photo> ps){
        this.context = ctx;
        this.photos = ps;
    }

    @NonNull
    @Override
    public PhotosRecyclerViewAdapter.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.photos_list, parent, false);

        view.setOnClickListener(this);

        return new PhotosRecyclerViewAdapter.PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosRecyclerViewAdapter.PhotoViewHolder holder, int position) {
        Picasso.get().load(photos.get(position).getUrl()).fit().into(holder.img1);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }


    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView img1;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.imageView);
        }
    }
}
