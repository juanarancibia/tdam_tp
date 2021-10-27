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

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder>{

    Context context;
    ArrayList<Photo> photos;

    public PhotosRecyclerViewAdapter(Context ctx, ArrayList<Photo> ps){
        this.context = ctx;
        this.photos = ps;
    }

    @NonNull
    @Override
    public PhotosRecyclerViewAdapter.PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.photos_list, parent, false);
        return new PhotosRecyclerViewAdapter.PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosRecyclerViewAdapter.PhotoViewHolder holder, int position) {
        Picasso.get().load(photos.get(position).getUrl()).fit().into(holder.img1);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     *
     */
    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img1;

        private RecyclerViewAdapter.ItemClickListener itemClickListener;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getLayoutPosition());
        }

        public void setItemClickListener(RecyclerViewAdapter.ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    public interface ItemClickListener {

        void onItemClick(View v,int pos);
    }
}
